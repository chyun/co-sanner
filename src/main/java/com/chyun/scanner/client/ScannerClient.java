package com.chyun.scanner.client;

import com.chyun.scanner.protocol.HttpProxy;
import com.chyun.scanner.protocol.PPTPProxy;
import com.chyun.scanner.remoting.RemotingClient;
import com.chyun.scanner.remoting.netty.NettyClientConfig;
import com.chyun.scanner.remoting.netty.NettyRemotingClient;

/**
 * 类的实现描述:
 *
 * @author liqun.wu
 */
public class ScannerClient {
    private final RemotingClient remotingClient;
    private final static String                           reqText          = "GET http://blog.csdn.net/linuu/article/details/51338538 HTTP/1.1\r\nHost: www.qq.com\r\nAccept: */*\r\nPragma: no-cache\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36\r\n\r\n";
    //private final static String                           reqText          = "GET http://www.qq.com/404/search_children.js HTTP/1.1\r\nHost: www.qq.com\r\n\r\n";
    private final static byte[]                           REQ_HTTP         = reqText.getBytes();
    public ScannerClient(final NettyClientConfig nettyClientConfig) {
        this.remotingClient = new NettyRemotingClient(nettyClientConfig);
        this.remotingClient.start();
    }
    public void scan(String ip, int port) {
        try {
            ip = ip + ":" + port;
            this.remotingClient.invokeAsync(ip, HttpProxy.getRequest(), "http", 30000);
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        NettyClientConfig config = new NettyClientConfig();
        ScannerClient client = new ScannerClient(config);
        client.scan("127.0.0.1", 8888);
    }
}
