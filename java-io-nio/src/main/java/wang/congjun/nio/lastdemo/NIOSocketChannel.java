package wang.congjun.nio.lastdemo;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

@Slf4j
public class NIOSocketChannel {

    private Selector selector;
    private static MessageObject messageObject;

    static {
        messageObject=new MessageObject("client");
    }

    public void startConnection(){
        SelectorProvider provider = SelectorProvider.provider();

        try {
            selector = provider.openSelector();

            SocketChannel sc = provider.openSocketChannel();
            sc.configureBlocking(false);

            //注册事件
            sc.register(selector, SelectionKey.OP_CONNECT);

            sc.connect(new InetSocketAddress(8886));

            MsgHandler.inputMsg(messageObject);

            MsgHandler msgHandler = new MsgHandler(selector, messageObject);

            msgHandler.handler();

        }catch (Exception e){
            log.error("【请求连接】 请求连接失败！！");
        }
    }

    public static void main(String[] args) {
        new NIOSocketChannel().startConnection();
    }

}
