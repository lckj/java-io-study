package wang.congjun.nio.demo1;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by wangcongjun on 2017/7/8.
 */
public class Nio001_Channel {
    public static void main(String[] args) throws Exception {
        RandomAccessFile aFile = new RandomAccessFile("D:/temp/data/nio-data.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);//read into buffer

        buf.put("sss".getBytes());
        System.out.println(buf.position());
        while (bytesRead != -1) {
            System.out.println("Read " + bytesRead);
            buf.flip();  //make buffer ready for read
            while(buf.hasRemaining()){
                System.out.print((char) buf.get());// read 1 byte at a time
            }
            buf.clear(); //make buffer ready for writing
            bytesRead = inChannel.read(buf);
        }
        aFile.close();

    }
}
