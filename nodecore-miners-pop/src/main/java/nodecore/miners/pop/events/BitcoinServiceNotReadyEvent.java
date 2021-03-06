// VeriBlock PoP Miner
// Copyright 2017-2019 Xenios SEZC
// All rights reserved.
// https://www.veriblock.org
// Distributed under the MIT software license, see the accompanying
// file LICENSE or http://www.opensource.org/licenses/mit-license.php.

package nodecore.miners.pop.events;

public class BitcoinServiceNotReadyEvent extends WarningMessageEvent {
    public BitcoinServiceNotReadyEvent() {
        super("Bitcoin service is not ready");
    }
}
