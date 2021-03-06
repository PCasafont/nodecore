// VeriBlock PoP Miner
// Copyright 2017-2019 Xenios SEZC
// All rights reserved.
// https://www.veriblock.org
// Distributed under the MIT software license, see the accompanying
// file LICENSE or http://www.opensource.org/licenses/mit-license.php.

package nodecore.miners.pop.tasks;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import nodecore.miners.pop.InternalEventBus;
import nodecore.miners.pop.Threading;
import nodecore.miners.pop.contracts.*;
import nodecore.miners.pop.events.FilteredBlockAvailableEvent;
import nodecore.miners.pop.events.PoPMiningOperationCompletedEvent;
import nodecore.miners.pop.events.TransactionConfirmedEvent;

public class ProcessManager {
    private final NodeCoreService nodeCoreService;
    private final BitcoinService bitcoinService;

    @Inject
    public ProcessManager(NodeCoreService nodeCoreService, BitcoinService bitcoinService) {
        this.nodeCoreService = nodeCoreService;
        this.bitcoinService = bitcoinService;

        InternalEventBus.getInstance().register(this);
    }

    public void shutdown() {
        InternalEventBus.getInstance().unregister(this);
    }

    public void submit(PoPMiningOperationState state) {
        BaseTask task = new GetPoPInstructionsTask(nodeCoreService, bitcoinService);
        Threading.TASK_POOL.submit(() -> executeTask(task, state));
    }

    public void restore(PoPMiningOperationState state) {
        BaseTask task = new RestoreTask(nodeCoreService, bitcoinService);
        Threading.TASK_POOL.submit(() -> executeTask(task, state));
    }

    private void executeTask(BaseTask task, PoPMiningOperationState state) {
        TaskResult result = task.execute(state);
        if (result.isSuccess()) {
            doNext(result.getNext(), result.getState());
        } else {
            handleFail(result.getState());
        }
    }

    private void handleFail(PoPMiningOperationState state) {
        InternalEventBus.getInstance().post(new PoPMiningOperationCompletedEvent(state.getOperationId()));
    }

    private void doNext(BaseTask next, PoPMiningOperationState state) {
        if (next != null) {
            executeTask(next, state);
        }
    }

    @Subscribe public void onTransactionConfirmed(TransactionConfirmedEvent event) {
        BaseTask task = new DetermineBlockOfProofTask(nodeCoreService, bitcoinService);
        Threading.TASK_POOL.submit(() -> executeTask(task, event.getState()));
    }

    @Subscribe public void onFilteredBlockAvailable(FilteredBlockAvailableEvent event) {
        BaseTask task = new ProveTransactionTask(nodeCoreService, bitcoinService);
        Threading.TASK_POOL.submit(() -> executeTask(task, event.getState()));
    }
}
