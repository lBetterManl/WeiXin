package com.yhc.test;

import com.yhc.po.AccessToken;
import com.yhc.util.MessageUtil;
import com.yhc.util.WeixinUtil;

import net.sf.json.JSONObject;

/**
 * @author YHC
 * @email yhc8023tm@foxmail.com
 * @version 2016年10月29日 下午12:03:23
 */
public class WeixinTest {

	public static void main(String[] args) {

		AccessToken token = WeixinUtil.getAccessToken();
		System.out.println("票据："+token.getToken());
		System.out.println("有效时间"+token.getExpiresIn());
	
		try {
			
			//上传图片
			/*
			String path = "D:/thumb.jpg";
			String mediaId = WeixinUtil.upload(path, token.getToken(), MessageUtil.MESSAGE_IMAGE);
			System.out.println("mediaId:"+mediaId);
			*/
			
			//上传缩略图ThumbMediaId		
			String path = "D:/thumb.jpg";
			String mediaId = WeixinUtil.upload(path, token.getToken(), MessageUtil.MESSAGE_THUMB);
			System.out.println("mediaId:"+mediaId);
			
			
			//创建菜单
			/*
			String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
			int result = WeixinUtil.createMenu(token.getToken(), menu);
			if(result==0){
				System.out.println("创建菜单成功");
			}else{
				System.out.println("错误码："+result);
			}
			*/
			
			//查询菜单
			/*
			JSONObject jsonObject = WeixinUtil.queryMeanu(token.getToken());
			System.out.println(jsonObject);
			*/
			
			//删除菜单
			/*
			int result = WeixinUtil.deleteMeanu(token.getToken());
			if(result==0){
				System.out.println("已删除菜单");
			}else{
				System.out.println("错误码："+result);
			}
			*/
			
			//百度翻译
			/*
			String sourceStr = "我是男子汉";
			String language = "en";
			JSONObject jsonObject = WeixinUtil.bdTranslate(sourceStr, language);
			System.out.println(jsonObject);
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
