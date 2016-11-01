package com.yhc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.yhc.po.TextMessage;
import com.yhc.util.CheckUtil;
import com.yhc.util.MessageUtil;
import com.yhc.util.WeixinUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WeixinServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");

		PrintWriter out = resp.getWriter();
		if (CheckUtil.checkSignatura(signature, timestamp, nonce)) {
			out.println(echostr);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		try {
			Map<String, String> map = MessageUtil.xml2Map(req);
			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String msgType = map.get("MsgType");

			String message = null;
			// 为文本消息就回复
			if (MessageUtil.MESSAGE_TEXT.equals(msgType)) {
				String content = map.get("Content");
				if ("1".equals(content)) {
					message = MessageUtil.initText(toUserName, fromUserName,
							MessageUtil.firstMenu());
				} else if ("2".equals(content)) {
					message = MessageUtil.initText(toUserName, fromUserName,
							MessageUtil.secondMenu());
				} else if ("3".equals(content)) {
					message = MessageUtil.initNewsMessage(toUserName,
							fromUserName);
				} else if ("4".equals(content)) {
					message = MessageUtil.initImageMessage(toUserName,
							fromUserName);
				} else if ("5".equals(content)) {
					message = MessageUtil.initMusicMessage(toUserName,
							fromUserName);
				} else if ("6".equals(content)) {
					message = MessageUtil.initText(toUserName, fromUserName,
							MessageUtil.bdfyNemu());
				} else if ("?".equals(content) || "？".equals(content)) {
					message = MessageUtil.initText(toUserName, fromUserName,
							MessageUtil.menuText());
				} else if (content.startsWith("翻译+")) {
					String[] sourceStrArr = content.split("\\+", 3);
					String language = sourceStrArr[1].trim();
					System.out.println("language:"+language);
					String[] lgList = { "zh", "en", "yue", "wyw", "jp", "kor",
							"fra", "spa", "th", "ara", "ru", "pt", "de", "it",
							"el", "nl", "pl", "bul", "est", "dan", "fin", "cs",
							"rom", "slo", "swe", "hu", "cht" };
					System.out.println(Arrays.asList(lgList).contains(language));
					if(!Arrays.asList(lgList).contains(language)){
						language = "en";
					}
					String source = sourceStrArr[2].trim();
					JSONObject jsonObject = WeixinUtil.bdTranslate(source,
							language);
					System.out.println(jsonObject);
					JSONArray resultArr = jsonObject.getJSONArray("trans_result");
					JSONObject result = resultArr.getJSONObject(0);
					String dst = result.getString("dst");
					message = MessageUtil.initText(toUserName, fromUserName,
							dst);
				} else {
					TextMessage text = new TextMessage();
					text.setFromUserName(toUserName);
					text.setToUserName(fromUserName);
					text.setMsgType(MessageUtil.MESSAGE_TEXT);
					text.setCreateTime(new Date().getTime());
					text.setContent("您发送的消息是：" + content);
					message = MessageUtil.textMessage2Xml(text);
				}

			} else if (MessageUtil.MESSAGE_IMAGE.equals(msgType)) {
				String picUrl = map.get("PicUrl");
				TextMessage text = new TextMessage();
				text.setFromUserName(toUserName);
				text.setToUserName(fromUserName);
				text.setMsgType(MessageUtil.MESSAGE_TEXT);
				text.setCreateTime(new Date().getTime());
				text.setContent("图片链接为：" + picUrl);
				message = MessageUtil.textMessage2Xml(text);

				System.out.println("图片链接为：" + picUrl);

			} else if (MessageUtil.MESSAGE_EVENT.equals(msgType)) {
				System.out.println("当前为事件\n");
				String eventType = map.get("Event");
				if (MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)) {
					message = MessageUtil.initText(toUserName, fromUserName,
							MessageUtil.menuText());
					System.out.println("用户：" + toUserName + "关注！");
				} else if (MessageUtil.MESSAGE_CLICK.equals(eventType)) {
					message = MessageUtil.initText(toUserName, fromUserName,
							MessageUtil.menuText());
				} else if (MessageUtil.MESSAGE_VIEW.equals(eventType)) {
					String url = map.get("EventKey");
					message = MessageUtil.initText(toUserName, fromUserName,
							url);
				} else if (MessageUtil.MESSAGE_SCANCODE.equals(eventType)) {
					String key = map.get("EventKey");
					message = MessageUtil.initText(toUserName, fromUserName,
							key);
				}
			} else if (MessageUtil.MESSAGE_LOCATION.equals(msgType)) {
				String label = map.get("Label");
				message = MessageUtil.initText(toUserName, fromUserName, label);
			}
			if (message != null) {
				System.out.println(message);
				out.print(message);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.close();
		}

	}
}
