package com.yhc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.yhc.po.Image;
import com.yhc.po.ImageMessage;
import com.yhc.po.Music;
import com.yhc.po.MusicMessage;
import com.yhc.po.News;
import com.yhc.po.NewsMessage;
import com.yhc.po.TextMessage;

/**
 * @author YHC
 * @email yhc8023tm@foxmail.com
 * @version 2016年10月28日 下午10:41:08
 */
public class MessageUtil {

	// 消息类型
	public static final String MESSAGE_TEXT = "text";

	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_SHORTVIDEO = "shortvideo";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_LINK = "link";

	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	public static final String MESSAGE_SCANCODE = "scancode_push";

	public static final String MESSAGE_THUMB = "thumb";
	public static final String MESSAGE_MUSIC = "music";
	public static final String MESSAGE_NEWS = "news";

	/**
	 * 将xml转换为Map集合 微信开发文档有误，可通过map查看参数
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> xml2Map(HttpServletRequest request)
			throws IOException, DocumentException {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();

		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		Element root = doc.getRootElement();
		List<Element> list = root.elements();
		for (Element e : list) {
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}

	/**
	 * 文本消息转换为xml
	 * 
	 * @param textMessage
	 * @return
	 */
	public static String textMessage2Xml(TextMessage textMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 组装文本消息
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initText(String toUserName, String fromUserName,
			String content) {
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MessageUtil.MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		return textMessage2Xml(text);
	}

	/**
	 * 图文消息转为xml
	 * 
	 * @param newsMessage
	 * @return
	 */
	public static String newsMessage2Xml(NewsMessage newsMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
	 * 组装图文消息
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initNewsMessage(String toUserName,
			String fromUserName) {
		String message = null;
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();

		News news = new News();
		news.setTitle("男神介绍");
		news.setDescription("关于男神的描述");
		news.setPicUrl("http://cloud.hbut.edu.cn/WeiXin/image/nanshen.jpg");
		news.setUrl("http://weibo.com/234930828");

		newsList.add(news);

		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());

		message = newsMessage2Xml(newsMessage);
		return message;
	}

	/**
	 * 图片消息转为xml
	 * 
	 * @param imageMessage
	 * @return
	 */
	public static String imageMessage2Xml(ImageMessage imageMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}

	/**
	 * 组装图片消息
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initImageMessage(String toUserName,
			String fromUserName) {
		String message = null;
		Image image = new Image();
		image.setMediaId(
				"ozg2_PJhqxtQzShXkLNACEf2Xig1vkHH1dpb3jAnrGwifCg0kd_j1qROpwlXjesL");
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setImage(image);
		message = imageMessage2Xml(imageMessage);
		return message;
	}

	/**
	 * 音乐消息转为xml
	 * 
	 * @param musicMessage
	 * @return
	 */
	public static String musicMessage2Xml(MusicMessage musicMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}

	/**
	 * 组装音乐消息
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initMusicMessage(String toUserName,
			String fromUserName) {
		String message = null;
		Music music = new Music();
		music.setThumbMediaId(
				"UA5ZNgieKCY-CaBlAe3eCpkbeCJ7uIGDvP8FNUQmkCx2vcb-nJegg4d3SsJ49jho");
		music.setTitle("see you again");
		music.setDescription("速7片尾曲");
		music.setMusicUrl("http://cloud.hbut.edu.cn/WeiXin/music/sua.mp3");
		music.setHQMusicUrl("http://cloud.hbut.edu.cn/WeiXin/music/sua.mp3");

		MusicMessage musicMessage = new MusicMessage();
		musicMessage.setFromUserName(toUserName);
		musicMessage.setToUserName(fromUserName);
		musicMessage.setMsgType(MESSAGE_MUSIC);
		musicMessage.setCreateTime(new Date().getTime());
		musicMessage.setMusic(music);

		message = musicMessage2Xml(musicMessage);
		return message;
	}

	/**
	 * 命令菜单
	 * 
	 * @return
	 */
	public static String menuText() {
		StringBuffer menu = new StringBuffer();
		menu.append("欢迎您的关注，请按照菜单提示操作：\n\n");
		menu.append("1.文本消息\n");
		menu.append("2.文本消息2\n");
		menu.append("3.图文消息\n");
		menu.append("4.图片消息\n");
		menu.append("5.音乐消息\n");
		menu.append("6.百度翻译\n");
		menu.append("\n\n回复？调出此菜单。");
		return menu.toString();
	}

	public static String firstMenu() {
		StringBuffer menu = new StringBuffer();
		menu.append("这是文本消息1");
		return menu.toString();
	}

	public static String secondMenu() {
		StringBuffer menu = new StringBuffer();
		menu.append("这是文本消息2");
		return menu.toString();
	}

	public static String bdfyNemu(){
		StringBuffer menu = new StringBuffer();
		menu.append("翻译指南：\n\n");
		menu.append("使用格式：翻译+语言代码+原文\n");
		menu.append("语言代码：\n");
		menu.append("zh	中文\t"
				+ "en 英语\n"
				+ "yue	粤语	\t"
				+ "wyw	文言文\n"
				+ "jp	日语\t"
				+ "kor	韩语\n"
				+ "fra	法语\t"
				+ "spa	西班牙语\n"
				+ "th	泰语\t"
				+ "ara	阿拉伯语\n"
				+ "ru	俄语\t"
				+ "pt	葡萄牙语\n"
				+ "de	德语\t"
				+ "it	意大利语\n"
				+ "el	希腊语\t"
				+ "nl	荷兰语\n"
				+ "pl	波兰语\t"
				+ "bul	保加利亚语\n"
				+ "est	爱沙尼亚语\t"
				+ "dan	丹麦语\n"
				+ "fin	芬兰语\t"
				+ "cs	捷克语\n"
				+ "rom	罗马尼亚语\t"
				+ "slo	斯洛文尼亚语\n"
				+ "swe	瑞典语\t"
				+ "hu	匈牙利语\n"
				+ "cht	繁体中文\n\n");
		menu.append("使用示例：\n");
		menu.append("翻译+en+好人\n");
		menu.append("翻译结果：good man\n");
		menu.append("\n\n回复？显示主菜单。");
		return menu.toString();
	}
}
