package com.core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class TwitterParams {

	private Properties prop;

	public static final String CONSUMER_KEY_KEY = "consumerKey";
	public static final String CONSUMER_SECRET_KEY = "consumerSecret";
	public static final String ACCESS_TOKEN_KEY = "accessToken";
	public static final String ACCESS_TOKEN_SECRET_KEY = "accessTokenSecret";

	public static final String BATCH_SIZE_KEY = "batchSize";
	public static final long DEFAULT_BATCH_SIZE = 1000L;
	public static final String KEYWORDS_KEY = "keywords";

	TwitterParams(String confFile) throws Exception {
		prop = new Properties();
		InputStream is = new FileInputStream(confFile);
		prop.load(is);
	}

	public String getString(String key) {
		return prop.getProperty(key);
	}
}
