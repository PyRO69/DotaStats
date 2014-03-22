package com.example.dotastats.helperclasses;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/*
 * Common Holder class to hold all the parsed information.
 * Made immutable and uses a builder to simplify object creation.
 * 
 * @author swaroop
 */
public class DownloadResult {

	public static enum RESULT_TYPE {
		RESULT_TYPE_NAMELIST,
		RESULT_TYPE_USERINFO,
		RESULT_TYPE_MATCHLIST,
		RESULT_TYPE_RECORDS,
		RESULT_TYPE_HEROES
	}

	private final boolean isRedirected;
	private final boolean isFailure;
	private final HashMap<String, String> nameList;
	private final HashMap<String, String> userInfo;
	private final HashMap<String, List<String>> matchList;
	private final HashMap<String, List<String>> recordList;
	private final List<HashMap<String, String>> heroesData;
	private final RESULT_TYPE resultType;
	private final String redirectLink;

	private DownloadResult(DownloadResultBuilder builder) {
		this.isRedirected = builder.isRedirected;
		this.isFailure =  builder.isFailure;
		this.nameList = builder.nameList;
		this.userInfo = builder.userInfo;
		this.matchList = builder.matchList;
		this.recordList = builder.recordList;
		this.heroesData = builder.heroesData;
		this.resultType = builder.resultType;
		this.redirectLink = builder.redirectLink;
	}

	public String getRedirectLink() {
		return new String(this.redirectLink);
	}

	public boolean isRedirected() {
		return isRedirected;
	}

	public HashMap<String, String> getNameList() {
		return (HashMap<String, String>) Collections.unmodifiableMap(nameList);
	}

	public HashMap<String, String> getUserInfo() {
		return (HashMap<String,String>) Collections.unmodifiableMap(userInfo);
	}

	public HashMap<String, List<String>> getMatchList() {
		return (HashMap<String, List<String>>) Collections.unmodifiableMap(matchList);
	}

	public RESULT_TYPE getResultType() {
		return resultType;
	}

	public boolean isFailure() {
		return isFailure;
	}

	public HashMap<String, List<String>> getRecordList() {
		return (HashMap<String, List<String>>) Collections.unmodifiableMap(recordList);
	}

	public List<HashMap<String, String>> getHeroesData() {
		return Collections.unmodifiableList(heroesData);
	}

	public static class DownloadResultBuilder {
		private boolean isRedirected = false;
		private boolean isFailure = false;
		private HashMap<String, String> nameList = null;
		private HashMap<String, String> userInfo = null;
		private HashMap<String, List<String>> matchList = null;
		private HashMap<String, List<String>> recordList = null;
		private List<HashMap<String, String>> heroesData = null;
		private RESULT_TYPE resultType = null;
		private String redirectLink = null;
		
		public DownloadResultBuilder redirected(boolean isRedirected) {
			this.isRedirected = isRedirected;
			return this;
		}
		public DownloadResultBuilder failure(boolean isFailure) {
			this.isFailure = isFailure;
			return this;
		}
		public DownloadResultBuilder setNameList(HashMap<String, String> nameList) {
			this.nameList = nameList;
			return this;
		}
		public DownloadResultBuilder setUserInfo(HashMap<String, String> userInfo) {
			this.userInfo = userInfo;
			return this;
		}
		public DownloadResultBuilder setMatchList(HashMap<String, List<String>> matchList) {
			this.matchList = matchList;
			return this;
		}
		public DownloadResultBuilder setRecordList(HashMap<String, List<String>> recordList) {
			this.recordList =  recordList;
			return this;
		}
		public DownloadResultBuilder setHeroesData(List<HashMap<String, String>> heroesData) {
			this.heroesData = heroesData;
			return this;
		}
		public DownloadResultBuilder setResultType(RESULT_TYPE resultType) {
			this.resultType = resultType;
			return this;
		}
		public DownloadResultBuilder setRedirectLink(String redirect) {
			this.redirectLink = redirect;
			return this;
		}
		public DownloadResult build() {
			return new DownloadResult(this);
		}
	}
}
