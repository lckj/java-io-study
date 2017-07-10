package wang.congjun.nio.demo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Created by wangcongjun on 2017/7/9.
 */
public class MsgHandler {

    public void write(final SocketChannel sc){
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        BufferedInputStream in = (BufferedInputStream) System.in;
                        System.out.println("请输入：");

                        Scanner scanner = new Scanner(in);
                        String s = scanner.next();
//                        sc.register(selector, SelectionKey.OP_WRITE);
                        sc.write(ByteBuffer.wrap(s.getBytes()));


                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }).start();
    }

    public void write(final SelectionKey key) {
        write((SocketChannel) key.channel());

    }

    public void read( SelectionKey key) {
        try {
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(48);
            sc.read(buffer);

            buffer.flip();
            System.out.println(new String(buffer.array()));


            buffer.clear();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
