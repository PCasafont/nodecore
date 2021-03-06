// VeriBlock NodeCore CLI
// Copyright 2017-2019 Xenios SEZC
// All rights reserved.
// https://www.veriblock.org
// Distributed under the MIT software license, see the accompanying
// file LICENSE or http://www.opensource.org/licenses/mit-license.php.

package nodecore.cli.commands.rpc;

import com.google.inject.Inject;
import io.grpc.StatusRuntimeException;
import nodecore.api.grpc.VeriBlockMessages;
import nodecore.cli.annotations.CommandParameterType;
import nodecore.cli.annotations.CommandSpec;
import nodecore.cli.annotations.CommandSpecParameter;
import nodecore.cli.commands.serialization.EmptyPayload;
import nodecore.cli.commands.serialization.FormattableObject;
import nodecore.cli.contracts.*;
import nodecore.cli.utilities.CommandUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@CommandSpec(
        name = "Remove Node",
        form = "removenode",
        description = "Removes the peer address from the configuration")
@CommandSpecParameter(name = "peer", required = true, type = CommandParameterType.PEER)
public class RemoveNodeCommand implements Command {
    private static final Logger _logger = LoggerFactory.getLogger(RemoveNodeCommand.class);

    @Inject
    public RemoveNodeCommand() {}

    @Override
    public Result execute(CommandContext context) {
        Result result = new DefaultResult();

        PeerEndpoint peer = context.getParameter("peer");
        try {
            VeriBlockMessages.Endpoint endpoint = VeriBlockMessages.Endpoint
                    .newBuilder()
                    .setAddress(peer.address())
                    .setPort(peer.port())
                    .build();
            VeriBlockMessages.ProtocolReply reply = context
                    .adminService()
                    .removeNode(VeriBlockMessages.NodeRequest
                            .newBuilder()
                            .addEndpoint(endpoint)
                            .build());
            if (!reply.getSuccess()) {
                result.fail();
            } else {
                FormattableObject<EmptyPayload> temp = new FormattableObject<>(reply.getResultsList());
                temp.success = !result.didFail();
                temp.payload = new EmptyPayload();

                context.outputObject(temp);

                context.suggestCommands(Collections.singletonList(AddNodeCommand.class));
            }
            for (VeriBlockMessages.Result r : reply.getResultsList())
                result.addMessage(r.getCode(), r.getMessage(), r.getDetails(), r.getError());
        } catch (StatusRuntimeException e) {
            CommandUtility.handleRuntimeException(result, e, _logger);
        }

        return result;
    }
}
