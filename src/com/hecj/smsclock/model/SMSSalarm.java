package com.hecj.smsclock.model;

import java.io.Serializable;
import java.util.Date;

public class SMSSalarm implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Integer id  ;
    public String telphone  ;
    public String telname  ;
    public String content   ;
    public String status   ;
    public String getTelname() {
		return telname;
	}
	public void setTelname(String telname) {
		this.telname = telname;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date sendTime  ;
    public Date createtime ;
    public String showTime;
	public String getShowTime() {
		return showTime;
	}
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	public SMSSalarm(Integer id, String telphone, String content,
			Date sendTime, Date createtime) {
		super();
		this.id = id;
		this.telphone = telphone;
		this.content = content;
		this.sendTime = sendTime;
		this.createtime = createtime;
	}
	public SMSSalarm() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
    
}
