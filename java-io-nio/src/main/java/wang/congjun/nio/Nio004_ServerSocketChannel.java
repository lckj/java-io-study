package wang.congjun.nio;

import wang.congjun.nio.demo.MsgHandler;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by wangcongjun on 2017/7/8.
 */
public class Nio004_ServerSocketChannel extends Nio002_selector {
    public Nio004_ServerSocketChannel(){
        try {
            //开启一个ServerSocketChannel通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //设置为非阻塞的
            serverSocketChannel.configureBlocking(false);
            //为这个通道的ServerSocket绑定端口和ip
            serverSocketChannel.socket().bind(new InetSocketAddress(999));
            //获取Selector选择器
            Selector selector = getSelector();
            //将这个通道注册到选择器上
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务端启动成功.......");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        Nio004_ServerSocketChannel ssc = new Nio004_ServerSocketChannel();
        ssc.setMsgHandler(new MsgHandler());
        ssc.channelHandler();

    }
}
