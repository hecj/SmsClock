package com.hecj.smsclock.util;
/**
 * 系统常量类
 * @author HECJ
 *
 */
public final class ConstantUtil {
	
	/**
	 * 日志target
	 */
	public final static String LOGNAME = "hecjlog";
	/**
	 * 是否打印日志
	 */
	public final static boolean ISPRINTLOG = true;
	
	/**
	 * 数据库表SMS
	 */
	public final static String CONTENT_SMS = "content://sms";
	/**
	 * 短信收件箱
	 */
	public final static String CONTENT_SMS_INBOX = "content://sms/inbox";
	/**
	 * 已发送
	 */
	public final static String CONTENT_SMS_SENT = "content://sms/sent";
	/**
	 * 草稿
	 */
	public final static String CONTENT_SMS_DRAFT = "content://sms/draft";
	/**
	 * 发件箱
	 */
	public final static String CONTENT_SMS_OUTBOX = "content://sms/outbox";
	/**
	 * 发送失败
	 */
	public final static String CONTENT_SMS_FAILED = "content://sms/failed";
	/**
	 * 待发送
	 */
	public final static String CONTENT_SMS_QUEQUD = "content://sms/queue";
	
	
}
