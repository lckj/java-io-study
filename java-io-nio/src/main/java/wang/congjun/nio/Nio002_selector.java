package wang.congjun.nio;

import wang.congjun.nio.demo.MsgHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by wangcongjun on 2017/7/8.
 */
public abstract class Nio002_selector {
    private Selector selector;
    private MsgHandler msgHandler;

    public Selector getSelector() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return selector;
    }

    public void setMsgHandler(MsgHandler msgHandler) {
        this.msgHandler = msgHandler;
    }

    public void channelHandler() {
        try {
            while (true) {
                Thread.sleep(1000);
                if (selector.select() == 0) continue;
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    selectionKeyIterator.remove();
                    /**
                     * 接受连接事件：当channel中有连接请求进来时会触发此事件，一般只在服务端才会有
                     */
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);

                        sc.register(selector, SelectionKey.OP_READ);
                        System.out.println("新连接加入.....");
                        msgHandler.write(sc,selector);

                    }
                    /**
                     * 链接事件：当channel中有连接被创建时就会触发此事件，一般只在客户端才会有
                     */
                    else if (selectionKey.isConnectable())
                    {
                        SocketChannel sc = (SocketChannel) selectionKey.channel();
                        if (sc.isConnectionPending()) {
                            sc.finishConnect();
                        }
                        msgHandler.write(selectionKey);
//                        msgHandler.write(sc);
//                        sc.register(selector, SelectionKey.OP_WRITE|SelectionKey.OP_READ);
                    }
                    /**
                     * 读事件在channel中有信息可读时就会触发该事件
                     */
                    else if (selectionKey.isValid()&&selectionKey.isReadable())
                    {
                        msgHandler.read(selectionKey);
                    }
                    /**
                     * 这里需要注意write事件是个特殊的事件；当系统内核中有可用内存满足写的要求时
                     * 这个事件会一直被触发，就会出现while不停的空跑现象
                     * 所以一般不用指定write事件
                     */
//                    else if (selectionKey.isValid()&&selectionKey.isWritable())
//                    {
//                        msgHandler.write(selectionKey);
//                    }

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {


    }
}
