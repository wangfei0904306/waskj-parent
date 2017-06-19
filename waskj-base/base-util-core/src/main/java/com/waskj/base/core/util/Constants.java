package com.waskj.base.core.util;

public interface Constants {


	/**
	 * jwt
	 */
	String JWT_ID = "jwt";
	String JWT_ISSUER = "waskj";
	String JWT_SECRET = "磁云科技技术有线公司(www.waskj.com)";
	String JWT_TOKEN = "token";
	int JWT_TTL = 60 * 60 * 1000;  //millisecond
	int JWT_REFRESH_INTERVAL = 55 * 60 * 1000;  //millisecond
	int JWT_REFRESH_TTL = 12 * 60 * 60 * 1000;  //millisecond
	String USER_LOGIN_KEY = "userLogin";

	/**
	 * 缓存
	 */
	String TOKEN_EHCACHE_NAME = "tokenCache";
	/**
	 * shiro
	 */
	String PARAM_DIGEST = "digest";
	String PARAM_TOKEN = "token";
	String PARAM_USERNAME = "username";
	
}