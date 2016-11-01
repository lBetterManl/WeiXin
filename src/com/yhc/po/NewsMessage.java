package com.yhc.po;

import java.util.List;

/**
 * 图文消息
 * @author YHC
 * @email yhc8023tm@foxmail.com
 * @version 2016年10月29日 上午10:04:27
 */
public class NewsMessage extends BaseMessage {
	private int ArticleCount;
	private List<News> Articles;
	
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<News> getArticles() {
		return Articles;
	}
	public void setArticles(List<News> articles) {
		Articles = articles;
	}
	
}
