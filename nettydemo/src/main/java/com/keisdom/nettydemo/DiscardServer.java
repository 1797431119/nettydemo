package com.keisdom.nettydemo;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class DiscardServer {
	static final boolean SSL = System.getProperty("ssl") != null;
	int port;

	public DiscardServer(int port) {
		this.port = port;
	}
	
	public void run() throws InterruptedException, SSLException, CertificateException {
		final SslContext sslCtx;
		         if (SSL) {
		             SelfSignedCertificate ssc = new SelfSignedCertificate();
		             sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
		         } else {
		             sslCtx = null;
	          }
		EventLoopGroup bossGroup=new NioEventLoopGroup();
		EventLoopGroup workerGroup=new NioEventLoopGroup();
		try {
			ServerBootstrap b=new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).
			childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					if(sslCtx!=null) {
						pipeline.addLast(sslCtx.newHandler(ch.alloc()));
					}
					pipeline.addLast(new DiscardServerHandle());
				}
			}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = b.bind(port).sync();
			if(f.isSuccess()) {
				System.out.println("连接成功");
			}
		}finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		
	}
	public static void main(String[] args) throws InterruptedException, SSLException, CertificateException {
		int port;
		if(args.length>0) {
			port=Integer.parseInt(args[0]);
		}else {
			port=8088;
		}
		new DiscardServer(port).run();
	}
}
