// VeriBlock NodeCore
// Copyright 2017-2019 Xenios SEZC
// All rights reserved.
// https://www.veriblock.org
// Distributed under the MIT software license, see the accompanying
// file LICENSE or http://www.opensource.org/licenses/mit-license.php.

package nodecore.cli.commands.rpc;

import com.google.inject.Inject;
import io.grpc.StatusRuntimeException;
import nodecore.api.grpc.LockWalletRequest;
import nodecore.api.grpc.ProtocolReply;
import nodecore.cli.annotations.CommandSpec;
import nodecore.cli.commands.serialization.EmptyPayload;
import nodecore.cli.commands.serialization.FormattableObject;
import nodecore.cli.contracts.Command;
import nodecore.cli.contracts.CommandContext;
import nodecore.cli.contracts.DefaultResult;
import nodecore.cli.contracts.Result;
import nodecore.cli.utilities.CommandUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@CommandSpec(
        name = "Lock Wallet",
        form = "lockwallet",
        description = "Disables the temporary unlock on the NodeCore wallet")
public class LockWalletCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(LockWalletCommand.class);

    @Inject
    public LockWalletCommand() {
    }

    @Override
    public Result execute(CommandContext context) throws Exception {
        Result result = new DefaultResult();

        try {
            ProtocolReply reply = context
                    .adminService()
                    .lockWallet(LockWalletRequest.newBuilder().build());

            for (nodecore.api.grpc.Result r : reply.getResultsList()) {
                result.addMessage(r.getCode(), r.getMessage(), r.getDetails(), r.getError());
            }

            if (!reply.getSuccess()) {
                result.fail();
            } else {
                FormattableObject<EmptyPayload> temp = new FormattableObject<>(reply.getResultsList());
                temp.success = !result.didFail();
                temp.payload = new EmptyPayload();

                context.outputObject(temp);

                context.suggestCommands(Collections.singletonList(
                        UnlockWalletCommand.class));
            }
        } catch (StatusRuntimeException e) {
            CommandUtility.handleRuntimeException(result, e, logger);
        }

        return result;
    }
}
