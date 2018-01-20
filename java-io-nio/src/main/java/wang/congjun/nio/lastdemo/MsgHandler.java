package wang.congjun.nio.lastdemo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.spec.ECField;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Data
public class MsgHandler {

    private Selector selector;
    private MessageObject mo;

    public MsgHandler(Selector selector, MessageObject mo) {
        this.selector = selector;
        this.mo = mo;
    }

    public MsgHandler() {
    }

    public void handler() {

        while (true) {
            try {
                selector.select();
            } catch (Exception e) {
                log.error("【接受请求】接受请求出错");
                e.printStackTrace();
            }
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        doAccept(key);
                    }

                    if (key.isConnectable()) {
                        doConnect(key);
                    }

                    if (key.isReadable()) {
                        doRead(key);
                    }

                    if (key.isWritable() && mo.isReady()) {
                        doWrite(key);
                        mo.setReady(false);
                    }
                }
            }
        }
    }

    private void doConnect(SelectionKey key) {
        SocketChannel sc = (SocketChannel) key.channel();
        if (sc.isConnectionPending()) {
            try {
                if (sc.finishConnect()) {
                    log.info("【回话连接】连接成功name：{}", mo.getName());
                    sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }
            } catch (Exception e) {
                log.info("【回话连接】注册读写事件失败！！name：{}", mo.getName());
            }
        }
    }

    private void doAccept(SelectionKey key) {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        try {
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            log.info("【接受连接】接受连接成功！！");
        } catch (Exception e) {
            log.info("【接受连接】接受连接失败name：{}", mo.getName());
            e.printStackTrace();
        }

    }

    private void doRead(SelectionKey key) {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            sc.read(byteBuffer);
            byte[] array = byteBuffer.array();
            log.info("【读取数据】读取数据内容data:{},name：{}", new String(array), mo.getName());
        } catch (Exception e) {
            log.info("【读取数据】读取数据失败name：{}", mo.getName());
            System.exit(1);
        }finally {
            try {
                sc.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void doWrite(SelectionKey key) {
        SocketChannel sc = (SocketChannel) key.channel();
        String msg = mo.getData();
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
        try {
            sc.write(byteBuffer);
            log.info("【写入数据】写入数据内容data:{},name：{}", msg, mo.getName());
        } catch (Exception e) {
            log.info("【写入数据】写入数据失败name：{}", mo.getName());
        }

    }

    public static void inputMsg(MessageObject mo) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String next = scanner.next();
                mo.setReady(true);
                mo.setData(next);
            }
        });
    }

}
