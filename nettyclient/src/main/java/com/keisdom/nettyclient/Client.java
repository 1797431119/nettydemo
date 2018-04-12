package com.keisdom.nettyclient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

import javax.imageio.stream.FileImageInputStream;

import com.keisdom.model.Request;
import com.keisdom.util.GzipUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class Client {
	//static Logger logger=(Logger) Log4JLoggerFactory.getInstance(Client.class);
	public static void main(String[] args) {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ChannelFuture future;
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					/*ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());*/
					ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS));
					ch.pipeline().addLast(new ClientHandle());
				}
			});
			future = b.connect("127.0.0.1", 8088).sync();
			Request request = new Request();
			request.setId(1);
			request.setName("pro" + 1);
			request.setRequestMessage("数据信息" + 1);
			char separator = File.separatorChar;
			File file = new File(System.getProperty("user.dir") + separator + "source" + separator + "2.png");
			/*FileInputStream inputStream = new FileInputStream(file);
			byte[] data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
			byte[] GzipData = GzipUtils.gzip(data);*/
			request.setAttachment(getImg(file));
			ChannelFuture f = future.channel().writeAndFlush((Object) request);
			/*future.channel().writeAndFlush(Unpooled.copiedBuffer("777".getBytes()));*/
			future.channel().closeFuture().sync();
			/*new Thread(()->{
				if(!f.channel().isActive()) {
					b.connect("127.0.0.1", 8088);
				}
			}).start();;*/
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			workerGroup.shutdownGracefully();
		}
	}
	static byte[] getImg(File file) {
		byte[] data = null;
	    FileImageInputStream input = null;
	    try {
	    	input=new FileImageInputStream(file);
	    	ByteArrayOutputStream output=new ByteArrayOutputStream();
	    	byte[] buf = new byte[1024];
	    	int numBytesRead = 0;
	    	while((numBytesRead=input.read(buf))!=-1) {
	    	    output.write(buf,0,numBytesRead);
	    	}
	    	data = output.toByteArray();
	    	output.close();
    	    input.close();
	    }catch (Exception e) {
			// TODO: handle exception
		}
		return data;
	}
}
