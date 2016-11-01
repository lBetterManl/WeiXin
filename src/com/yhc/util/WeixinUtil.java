package com.yhc.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.yhc.menu.Button;
import com.yhc.menu.ClickButton;
import com.yhc.menu.Menu;
import com.yhc.menu.ViewButton;
import com.yhc.po.AccessToken;

import net.sf.json.JSONObject;

/**
 * @author YHC
 * @email yhc8023tm@foxmail.com
 * @version 2016年10月29日 上午11:08:41
 */
public class WeixinUtil {

	//微信测试账号
	/*
	private static final String APPID = "wx5ef0add8e2d2401b";
	private static final String APPSECRET = "507494eb39cd55cfd8a6c493e677cef1";
	*/
	
	//微信开发者账号 l男神lFM
	
	private static final String APPID = "wxcc99b0cd8ac0bb18";
	private static final String APPSECRET = "ac4e220e08b6aee0eb66bb8091b81c50";
	
	
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	
	//百度翻译
	private static final String TRANS_URL = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=QUERY&from=FROM&to=TO&appid=APPID&salt=SALT&sign=SIGN";
	private static final String BD_APPID = "20161029000031002";
	private static final String BD_SECRET = "_3hpCpXAvLcfsVjBJUSS";
	
	/**
	 * get请求
	 * @param url
	 * @return
	 */
	public static JSONObject doGetStr(String url){
		CloseableHttpClient httpClicent = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			CloseableHttpResponse response = httpClicent.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return jsonObject;
	}
	
	/**
	 * post请求
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static JSONObject doPostStr(String url,String outStr){
		CloseableHttpClient httpClicent = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		
		try {
			httpPost.setEntity(new StringEntity(outStr, "UTF-8"));
			CloseableHttpResponse response = httpClicent.execute(httpPost);
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			jsonObject = JSONObject.fromObject(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 获取access_token
	 * @return
	 */
	public static AccessToken getAccessToken(){
		AccessToken token = new AccessToken();
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject.has("errcode")){
			System.out.println("错误代码："+jsonObject.getString("errcode"));
		}else if(jsonObject!=null){
			token.setToken(jsonObject.getString("access_token"));
			token.setExpiresIn(jsonObject.getInt("expires_in"));
		}
		return token;
	}
	
	
	/**
	 * 文件上传
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static String upload(String filePath, String accessToken,String type) throws IOException{
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			throw new IOException("文件不存在");
		}

		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);
		
		URL urlObj = new URL(url);
		//连接
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

		con.setRequestMethod("POST"); 
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); 

		//设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		//设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");

		//获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);

		//文件正文部分
		//把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		//结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

		out.write(foot);

		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try {
			//定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		JSONObject jsonObj = JSONObject.fromObject(result);
		System.out.println(jsonObj);
		String mediaId = "";
		if(jsonObj.has("errcode")){
			System.out.println("错误代码："+jsonObj.getString("errcode"));
		}else{
			String typeName = "media_id";
			if(!MessageUtil.MESSAGE_IMAGE.equals(type)){
				typeName = type + "_media_id";
			}
			mediaId = jsonObj.getString(typeName);
		}
		
		return mediaId;
	}
	
	
	/**
	 * 组装底部菜单
	 * @return
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		ClickButton button11= new ClickButton();
		button11.setName("点击菜单");
		button11.setType("click");
		button11.setKey("11");
		
		ViewButton button21 = new ViewButton();
		button21.setName("视图菜单");
		button21.setType("view");
		button21.setUrl("http://weibo.com/234930828");
		
		ClickButton button31 = new ClickButton();
		button31.setName("扫码事件");
		button31.setType("scancode_push");
		button31.setKey("31");
		
		ClickButton button32 = new ClickButton();
		button32.setName("地理位置");
		button32.setType("location_select");
		button32.setKey("32");
		
		Button button = new Button();
		button.setName("其他菜单");
		button.setSub_button(new Button[]{button31,button32});
		
		menu.setButton(new Button[]{button11,button21,button});
		return menu;
	}
	
	/**
	 * 创建菜单
	 * @param token
	 * @param menu
	 * @return
	 */
	public static int createMenu(String token,String menu){
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doPostStr(url, menu);
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	
	/**
	 * 查询菜单
	 * @param token
	 * @return
	 */
	public static JSONObject queryMeanu(String token){
		String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doGetStr(url);
		return jsonObject;
	}
	
	public static int deleteMeanu(String token){
		String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doGetStr(url);
		int result = 0;
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	
	/**
	 * 百度翻译
	 * @param sourceStr
	 * @param language
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static JSONObject bdTranslate(String sourceStr,String language) throws UnsupportedEncodingException{
		String source = new String(sourceStr.toString().getBytes("UTF-8"));
		String salt = String.valueOf(System.currentTimeMillis());
		//String salt = "1435660288";
		String signStr = BD_APPID+source+salt+BD_SECRET;
		String sign = MD5.md5(signStr).toLowerCase();
		String url = TRANS_URL.replace("QUERY", URLEncoder.encode(source,"UTF-8")).replace("FROM", "auto").replace("TO", language).replace("APPID", BD_APPID).replace("SALT", salt).replace("SIGN", sign);
		JSONObject jsonObject = doGetStr(url);
		return jsonObject;
	}
}
