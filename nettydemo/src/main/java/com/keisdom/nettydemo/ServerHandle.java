package com.keisdom.nettydemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.imageio.stream.FileImageOutputStream;

import com.keisdom.model.Request;
import com.keisdom.model.Response;
import com.keisdom.util.GzipUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandle extends ChannelHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Object obj = (Object) msg;
		Request request = (Request) obj;
		System.out.println("client:" + request.getId() + "," + request.getName() + "," + request.getRequestMessage());
		Response response = new Response();
		response.setId(request.getId());
		response.setName("response" + request.getId());
		response.setResponseMessage("响应内容" + request.getRequestMessage());
		// byte[] unGzipData=GzipUtils.unGzip(request.getAttachment());
		// char separator=File.separatorChar;
		// String
		// path=System.getProperty("user.dir")+separator+"recieve"+separator+"1.png";
		setImg(request.getAttachment());
		// FileOutputStream outputStream=new
		// FileOutputStream(System.getProperty("user.dir")+separator+"recieve"+separator+"1.png");
		// outputStream.write(unGzipData);
		// outputStream.flush();
		// outputStream.close();
		// ctx.writeAndFlush(response);
		// ctx.writeAndFlush(Unpooled.copiedBuffer("888".getBytes()));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	static void setImg(byte[] data) {
		if (data.length < 3) {
			return;
		}
		try {
			char separator = File.separatorChar;
			String path = System.getProperty("user.dir") + separator + "recieve" + separator
					+ System.currentTimeMillis() + ".png";
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileImageOutputStream output = new FileImageOutputStream(file);
			output.write(data, 0, data.length);
			output.flush();
			output.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
