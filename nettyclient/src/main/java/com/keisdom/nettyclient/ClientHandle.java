package com.keisdom.nettyclient;

import java.io.File;
import java.io.FileInputStream;

import com.keisdom.model.Request;
import com.keisdom.model.Response;
import com.keisdom.util.GzipUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class ClientHandle extends ChannelHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Response response=(Response) msg;
		System.out.println("Server:"+ response.getId() + "," + response.getName() + "," +response.getResponseMessage());
		ReferenceCountUtil.release(msg);
		/*try {
			ByteBuf in=(ByteBuf) msg;
			byte[] data=new byte[in.readableBytes()];
			in.readBytes(data);
			System.out.println("server:"+new String(data).trim());
		}finally {
			ReferenceCountUtil.release(msg);
		}*/
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	/*@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Request request = new Request();
		request.setId(1);
		request.setName("pro" + 1);
		request.setRequestMessage("数据信息" + 1);
		char separator = File.separatorChar;
		File file = new File(System.getProperty("user.dir") + separator + "source" + separator + "2.png");
		FileInputStream inputStream = new FileInputStream(file);
		byte[] data = new byte[inputStream.available()];
		inputStream.read(data);
		inputStream.close();
		byte[] GzipData = GzipUtils.gzip(data);
		request.setAttachment(GzipData);
		ctx.writeAndFlush(request);
	}*/
	
}
