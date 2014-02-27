package com.example.dotastats.helperclasses;

import java.util.HashMap;
import java.util.List;

public class DownloadResult {
	private boolean isRedirected;
	private boolean isFailure;
	private HashMap<String, String> nameList;
	private HashMap<String, String> userInfo;
	private HashMap<String, List<String>> matchList;
	private HashMap<String, List<String>> recordList;
	private List<HashMap<String, String>> heroesData;
	public static final int RESULT_TYPE_NAMELIST = 1;
	public static final int RESULT_TYPE_USERINFO = 2;
	public static final int RESULT_TYPE_MATCHLIST = 4;
	public static final int RESULT_TYPE_RECORDS = 8;
	public static final int RESULT_TYPE_HEROES = 16;
	private int resultType;
	private String redirectLink;

	public void setRedirectLink(String link) {
		this.redirectLink =  link;
	}
	public String getRedirectLink() {
		return new String(this.redirectLink);
	}
	public boolean isRedirected() {
		return isRedirected;
	}
	public void setRedirected(boolean isRedirected) {
		this.isRedirected = isRedirected;
	}
	public HashMap<String, String> getNameList() {
		return nameList;
	}
	public void setNameList(HashMap<String, String> nameList) {
		this.nameList = nameList;
	}
	public HashMap<String, String> getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(HashMap<String, String> userInfo) {
		this.userInfo = userInfo;
	}
	public HashMap<String, List<String>> getMatchList() {
		return matchList;
	}
	public void setMatchList(HashMap<String, List<String>> matchList) {
		this.matchList = matchList;
	}
	public int getResultType() {
		return resultType;
	}
	public void setResultType(int resultType) {
		this.resultType = resultType;
	}
	public boolean isFailure() {
		return isFailure;
	}
	public void setFailure(boolean isFailure) {
		this.isFailure = isFailure;
	}
	public HashMap<String, List<String>> getRecordList() {
		return recordList;
	}
	public void setRecordList(HashMap<String, List<String>> recordList) {
		this.recordList = recordList;
	}
	public List<HashMap<String, String>> getHeroesData() {
		return heroesData;
	}
	public void setHeroesData(List<HashMap<String, String>> heroesData) {
		this.heroesData = heroesData;
	}
}
