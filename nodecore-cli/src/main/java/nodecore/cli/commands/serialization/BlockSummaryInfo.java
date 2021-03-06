// VeriBlock NodeCore CLI
// Copyright 2017-2019 Xenios SEZC
// All rights reserved.
// https://www.veriblock.org
// Distributed under the MIT software license, see the accompanying
// file LICENSE or http://www.opensource.org/licenses/mit-license.php.

package nodecore.cli.commands.serialization;

import com.google.gson.annotations.SerializedName;
import nodecore.api.grpc.VeriBlockMessages;
import nodecore.api.grpc.utilities.ByteStringUtility;
import org.veriblock.core.utilities.Utility;

public class BlockSummaryInfo {
    public BlockSummaryInfo(final VeriBlockMessages.BlockSummary block) {
        hash = ByteStringUtility.byteStringToHex(block.getHash());
        number = block.getNumber();
        version = (short)block.getVersion();
        previousHash = ByteStringUtility.byteStringToHex(block.getPreviousHash());
        if (number % 20 == 0) {
            previousKeystoneHash = ByteStringUtility.byteStringToHex(block.getSecondPreviousHash());
            secondPreviousKeystoneHash = ByteStringUtility.byteStringToHex(block.getThirdPreviousHash());
        } else {
            secondPreviousHash = ByteStringUtility.byteStringToHex(block.getSecondPreviousHash());
            thirdPreviousHash = ByteStringUtility.byteStringToHex(block.getThirdPreviousHash());
        }
        merkleRoot = ByteStringUtility.byteStringToHex(block.getMerkleRoot());
        timestamp = block.getTimestamp();
        decoded_difficulty = block.getDecodedDifficulty();
        winningNonce = block.getWinningNonce();

        size = block.getSize();
        ledgerHash = ByteStringUtility.byteStringToHex(block.getLedgerHash());
        powCoinbaseReward = Utility.formatAtomicLongWithDecimal(block.getPowCoinbaseReward());
        popCoinbaseReward = Utility.formatAtomicLongWithDecimal(block.getPopCoinbaseReward());
        transactionFees = Utility.formatAtomicLongWithDecimal(block.getTotalFees());
    }

    @SerializedName("hash")
    public String hash;

    @SerializedName("number")
    public int number;

    @SerializedName("version")
    public short version;

    @SerializedName("previous_hash")
    public String previousHash;

    @SerializedName("second_previous_hash")
    public String secondPreviousHash;

    @SerializedName("previous_keystone_hash")
    public String previousKeystoneHash;

    @SerializedName("third_previous_hash")
    public String thirdPreviousHash;

    @SerializedName("second_previous_keystone_hash")
    public String secondPreviousKeystoneHash;

    @SerializedName("merkle_root")
    public String merkleRoot;

    @SerializedName("timestamp")
    public int timestamp;

    @SerializedName("decoded_difficulty")
    public long decoded_difficulty;

    @SerializedName("winning_nonce")
    public int winningNonce;

    @SerializedName("ledger_hash")
    public String ledgerHash;

    @SerializedName("size")
    public int size;

    @SerializedName("transaction_fees")
    public String transactionFees;

    @SerializedName("pow_coinbase_reward")
    public String powCoinbaseReward;

    @SerializedName("pop_coinbase_reward")
    public String popCoinbaseReward;
}
