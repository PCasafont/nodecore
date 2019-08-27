// VeriBlock NodeCore CLI
// Copyright 2017-2019 Xenios SEZC
// All rights reserved.
// https://www.veriblock.org
// Distributed under the MIT software license, see the accompanying
// file LICENSE or http://www.opensource.org/licenses/mit-license.php.

package nodecore.cli.commands.rpc;

import com.google.inject.Inject;
import io.grpc.StatusRuntimeException;
import nodecore.api.grpc.GetLastBitcoinBlockReply;
import nodecore.api.grpc.GetLastBitcoinBlockRequest;
import nodecore.api.grpc.utilities.ByteStringUtility;
import nodecore.cli.annotations.CommandSpec;
import nodecore.cli.commands.serialization.FormattableObject;
import nodecore.cli.contracts.Command;
import nodecore.cli.contracts.CommandContext;
import nodecore.cli.contracts.DefaultResult;
import nodecore.cli.contracts.Result;
import nodecore.cli.utilities.CommandUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CommandSpec(
        name = "Get Last Bitcoin Block",
        form = "getlastbitcoinblock",
        description = "Returns the last Bitcoin block known by NodeCore")
public class GetLastBitcoinBlockCommand implements Command {
    private static final Logger _logger = LoggerFactory.getLogger(GetLastBitcoinBlockCommand.class);

    @Inject
    public GetLastBitcoinBlockCommand() {
    }

    @Override
    public Result execute(CommandContext context) {
        Result result = new DefaultResult();

        try {
            GetLastBitcoinBlockReply reply = context
                    .adminService()
                    .getLastBitcoinBlock(GetLastBitcoinBlockRequest.newBuilder().build());
            if (!reply.getSuccess()) {
                result.fail();
            } else {
                FormattableObject<String> temp = new FormattableObject<>(reply.getResultsList());
                temp.success = !result.didFail();
                temp.payload = ByteStringUtility.byteStringToHex(reply.getHeader());

                context.outputObject(temp);
            }
            for (nodecore.api.grpc.Result r : reply.getResultsList())
                result.addMessage(r.getCode(), r.getMessage(), r.getDetails(), r.getError());
        } catch (StatusRuntimeException e) {
            CommandUtility.handleRuntimeException(result, e, _logger);
        }

        return result;
    }
}
