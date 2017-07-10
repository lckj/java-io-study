package wang.congjun.nio;

import wang.congjun.nio.demo.MsgHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Created by wangcongjun on 2017/7/8.
 */
public class Nio003_SocketChannel extends Nio002_selector{

    public Nio003_SocketChannel() {
        try {
            //打开socketChannel
            SocketChannel socketChannel = SocketChannel.open();
            //设置为异步
            socketChannel.configureBlocking(false);
            //打开selector
            Selector selector = getSelector();
            /**
             * 这个不能写在key.isConnectable()下面不然会一直等待
             */
            //将这个socketChannel链接到服务端
            socketChannel.connect(new InetSocketAddress(999));
            //把socketChannel注册到selector OP_CONNECT
            socketChannel.register(selector, SelectionKey.OP_CONNECT|SelectionKey.OP_READ);
            System.out.println("客户端启动成功.....");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) throws Exception {
        Nio003_SocketChannel channel = new Nio003_SocketChannel();
        channel.setMsgHandler(new MsgHandler());
        channel.channelHandler();
    }
}
