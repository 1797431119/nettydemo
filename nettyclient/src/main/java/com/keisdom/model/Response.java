package com.keisdom.model;

import java.io.Serializable;

public class Response implements Serializable{
	private static final long serialVersionUID = 5344872860554322064L;
	int id;
	String name;
	String responseMessage;
	byte[] attachment;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public byte[] getAttachment() {
		return attachment;
	}
	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}
	
}
