package wang.congjun.netty.day001_EchoSerAndCli.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by wangcongjun on 2017/7/10.
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start(){

        final EchoClientHandler clientHandler = new EchoClientHandler();
        //创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建Bootstrap
            Bootstrap b = new Bootstrap();
            //指定group
            b.group(group)
                    //指定使用的NioChannel
                    .channel(NioSocketChannel.class)
                    //指定远程连接的地址和端口号
                    .remoteAddress(new InetSocketAddress(host,port))
                    //在创建Channel时向ChannelPipeline中添加一个EchoClientHandler
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(clientHandler);
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new EchoClient("127.0.0.1",8888).start();
    }
}
