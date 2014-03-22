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

	public static enum RESULT_TYPE {
		RESULT_TYPE_NAMELIST,
		RESULT_TYPE_USERINFO,
		RESULT_TYPE_MATCHLIST,
		RESULT_TYPE_RECORDS,
		RESULT_TYPE_HEROES
	}

	private RESULT_TYPE resultType;
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
	public RESULT_TYPE getResultType() {
		return resultType;
	}
	public void setResultType(RESULT_TYPE resultType) {
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
