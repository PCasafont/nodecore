// VeriBlock NodeCore
// Copyright 2017-2019 Xenios SEZC
// All rights reserved.
// https://www.veriblock.org
// Distributed under the MIT software license, see the accompanying
// file LICENSE or http://www.opensource.org/licenses/mit-license.php.

package nodecore.miners.pop.api.models;

import java.util.List;

public class ResultMessageResponse {
    public String code;
    public String message;
    public List<String> details;
    public boolean error;
}
