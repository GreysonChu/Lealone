/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.server.protocol;

import java.io.IOException;

import org.lealone.db.RunMode;
import org.lealone.net.NetInputStream;
import org.lealone.net.NetOutputStream;

public class InitPacketAck implements AckPacket {

    public final int clientVersion;
    public final boolean autoCommit;
    public final String targetNodes;
    public final RunMode runMode;
    public final boolean invalid;

    public InitPacketAck(int clientVersion, boolean autoCommit, String targetNodes, RunMode runMode, boolean invalid) {
        this.clientVersion = clientVersion;
        this.autoCommit = autoCommit;
        this.targetNodes = targetNodes;
        this.runMode = runMode;
        this.invalid = invalid;
    }

    @Override
    public PacketType getType() {
        return PacketType.SESSION_INIT_ACK;
    }

    @Override
    public void encode(NetOutputStream out, int version) throws IOException {
        out.writeInt(clientVersion);
        out.writeBoolean(autoCommit);
        out.writeString(targetNodes);
        out.writeString(runMode.toString());
        out.writeBoolean(invalid);
    }

    public static final Decoder decoder = new Decoder();

    private static class Decoder implements PacketDecoder<InitPacketAck> {
        @Override
        public InitPacketAck decode(NetInputStream in, int version) throws IOException {
            int clientVersion = in.readInt();
            boolean autoCommit = in.readBoolean();
            String targetNodes = in.readString();
            RunMode runMode = RunMode.valueOf(in.readString());
            boolean invalid = in.readBoolean();
            return new InitPacketAck(clientVersion, autoCommit, targetNodes, runMode, invalid);
        }
    }
}
