package com.chyun.scanner.remoting.netty;

import com.chyun.scanner.protocol.HttpProxy;
import com.chyun.scanner.protocol.PPTPProxy;
import com.chyun.scanner.remoting.ChannelEventListener;
import com.chyun.scanner.remoting.common.ServiceThread;
import com.chyun.scanner.remoting.exception.RemotingSendRequestException;
import com.chyun.scanner.remoting.exception.RemotingTimeoutException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 类的实现描述:
 *
 * @author liqun.wu
 */
public abstract class NettyRemotingAbstract {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRemotingAbstract.class);

    protected final NettyEventExecuter nettyEventExecuter = new NettyEventExecuter();

    public abstract ChannelEventListener getChannelEventListener();
    public abstract String getProtocol(Channel channel);

    public void processMessageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        String html = new String((byte[])msg);
        String protocol = getProtocol(ctx.channel());
        if ("http".equals(protocol)) {
            if (HttpProxy.isProxy(html)) {
                System.out.println("发现http代理:" + ctx.channel().remoteAddress());
            } else {
                System.out.println("未发现http代理:" + ctx.channel().remoteAddress());
            }
        } else if ("pptp".equals(protocol)) {
            if (PPTPProxy.isProxy((byte[])msg)) {
                System.out.println("发现pptp代理:" + ctx.channel().remoteAddress());
            } else {
                System.out.println("未发现pptp代理:" + ctx.channel().remoteAddress());
            }
        }
    }

    public void putNettyEvent(final NettyEvent event) {
        this.nettyEventExecuter.putNettyEvent(event);
    }

    class NettyEventExecuter extends ServiceThread {
        private final LinkedBlockingQueue<NettyEvent> eventQueue = new LinkedBlockingQueue<NettyEvent>();
        private final int maxSize = 10000;

        public void putNettyEvent(final NettyEvent event) {
            if (this.eventQueue.size() <= maxSize) {
                this.eventQueue.add(event);
            } else {
                LOGGER.warn("event queue size[{}] enough, so drop this event {}", this.eventQueue.size(), event.toString());
            }
        }

        @Override
        public void run() {
            LOGGER.info(this.getServiceName() + " service started");

            final ChannelEventListener listener = NettyRemotingAbstract.this.getChannelEventListener();

            while (!this.isStopped()) {
                try {
                    NettyEvent event = this.eventQueue.poll(3000, TimeUnit.MILLISECONDS);
                    if (event != null && listener != null) {
                        switch (event.getType()) {
                            case IDLE:
                                listener.onChannelIdle(event.getRemoteAddr(), event.getChannel());
                                break;
                            case CLOSE:
                                listener.onChannelClose(event.getRemoteAddr(), event.getChannel());
                                break;
                            case CONNECT:
                                listener.onChannelConnect(event.getRemoteAddr(), event.getChannel());
                                break;
                            case EXCEPTION:
                                listener.onChannelException(event.getRemoteAddr(), event.getChannel());
                                break;
                            default:
                                break;

                        }
                    }
                } catch (Exception e) {
                    LOGGER.warn(this.getServiceName() + " service has exception. ", e);
                }
            }

            LOGGER.info(this.getServiceName() + " service end");
        }

        @Override
        public String getServiceName() {
            return NettyEventExecuter.class.getSimpleName();
        }
    }

    public void scanResponseTable() {

    }

    public void invokeAsyncImpl(final Channel channel, final byte[] request, final long timeoutMillis)
            throws InterruptedException, RemotingTimeoutException, RemotingSendRequestException {
        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {

            /**
             * 表示数据已经发送完毕, 可以记录一些其他事情，比如日志
             * @param future
             * @throws Exception
             */
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("finish");
                //future.channel().re
                //future.channel().read();
            }
        });
    }

}
