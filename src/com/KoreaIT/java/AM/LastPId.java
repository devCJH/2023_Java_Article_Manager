package com.KoreaIT.java.AM;

public class LastPId {
	private static int articleLastId;
	private static int memberLastId;
	
	static {
		articleLastId = 0;
		memberLastId = 0;
	}
	
	public static int getArticleLastId() {
		int id = articleLastId + 1;
		articleLastId = id;
		return id;
	}
	
	public static int getMemberLastId() {
		int id = memberLastId + 1;
		memberLastId = id;
		return id;
	}
}
