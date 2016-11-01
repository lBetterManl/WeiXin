package com.yhc.po;

/**
 * 文本消息
 * @author YHC
 * @email yhc8023tm@foxmail.com
 * @version 2016年10月28日 下午11:04:31
 */
public class TextMessage extends BaseMessage {

	private String Content;	//文本消息内容
	private String MsgId;	//消息id，64位整型
	
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}

}
