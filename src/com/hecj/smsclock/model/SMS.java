package com.hecj.smsclock.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 短信实体
 * @author HECJ
 *
 */
public class SMS implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String threadId;
	private String address;
	private String person;
	private String protocol;
	private String read;
	private String status;
	private String type;
	private String serviceCenter;
	private String body;
	private Date date;
	/**
	 * 同一人的短信总数
	 */
	private int count;
	
	
	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public SMS(String id, String threadId, String address, String person,
			String protocol, String read, String status, String type,
			String serviceCenter, String body, Date date) {
		super();
		this.id = id;
		this.threadId = threadId;
		this.address = address;
		this.person = person;
		this.protocol = protocol;
		this.read = read;
		this.status = status;
		this.type = type;
		this.serviceCenter = serviceCenter;
		this.body = body;
		this.date = date;
	}


	public SMS() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getThreadId() {
		return threadId;
	}


	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getPerson() {
		return person;
	}


	public void setPerson(String person) {
		this.person = person;
	}


	public String getProtocol() {
		return protocol;
	}


	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}


	public String getRead() {
		return read;
	}


	public void setRead(String read) {
		this.read = read;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getServiceCenter() {
		return serviceCenter;
	}


	public void setServiceCenter(String serviceCenter) {
		this.serviceCenter = serviceCenter;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
