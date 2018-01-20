package wang.congjun.nio.lastdemo;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

@Slf4j
public class NIOServerSocketChannel {
    private Selector selector;
    private static MessageObject messageObject;
    static{
        messageObject=new MessageObject("server");
    }

    public void startServer() {

        SelectorProvider provider = SelectorProvider.provider();
        try {
            //召唤一个server
            ServerSocketChannel ssc = provider.openServerSocketChannel();
            selector = provider.openSelector();

            ssc.configureBlocking(false);
            //注册服务
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            //启动服务
            ssc.bind(new InetSocketAddress(8886));

            log.info("【启动服务】服务启动成功！！");

            MsgHandler.inputMsg(messageObject);


            MsgHandler msgHandler = new MsgHandler(selector, messageObject);
            msgHandler.handler();

        } catch (Exception e) {
            log.error("【启动服务】启动失败！！");
        }

    }

    public static void main(String[] args) {
        new NIOServerSocketChannel().startServer();
    }

}
