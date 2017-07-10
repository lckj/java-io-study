package wang.congjun.netty.day001_EchoSerAndCli.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by wangcongjun on 2017/7/10.
 */
public class EchoServer {
    private final int port;
    public EchoServer(int port){
        this.port = port;
    }

    public static void main(String[] args) {
        new EchoServer(8888).start();
    }
    public void start(){
        final EchoServerHandler serverHandler = new EchoServerHandler();

            //创建EventLoopGroup
            EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            //指定EventLoopGroup
            b.group(group)
                    //指定使用的Nio传输Channel
                    .channel(NioServerSocketChannel.class)
                    //指定服务启动的端口
                    .localAddress(new InetSocketAddress(port))
                    //添加一个EchoServerHandler到子Channel的ChannelPipeline中
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            //异步的绑定服务器调用sync方法堵塞等待直到绑定完成
            ChannelFuture f = b.bind().sync();
            //获取Channel的CloseFuture并且堵塞到当前线程直到完成
            f.channel().closeFuture().sync();
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }finally {
            try {
                //关闭EventLoopGroup释放所有资源
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
