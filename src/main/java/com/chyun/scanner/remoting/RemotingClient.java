/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chyun.scanner.remoting;

import com.chyun.scanner.remoting.exception.RemotingConnectException;
import com.chyun.scanner.remoting.exception.RemotingSendRequestException;
import com.chyun.scanner.remoting.exception.RemotingTimeoutException;

public interface RemotingClient extends RemotingService {

    public void invokeAsync(final String addr, final byte[] request, final String protocol, final long timeoutMillis)
            throws RemotingSendRequestException, RemotingConnectException, InterruptedException,RemotingTimeoutException;

//    public void registerProcessor(final int requestCode, final NettyRequestProcessor processor,
//                                  final ExecutorService executor);

    public boolean isChannelWriteable(final String addr);
}
