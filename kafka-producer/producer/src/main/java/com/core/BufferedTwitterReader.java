package com.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class BufferedTwitterReader implements Runnable, StatusListener {
	private final Logger LOGGER = Logger.getLogger(BufferedTwitterReader.class);
	private final Charset ENC = Charset.forName("UTF-8");
	private OutputStream outputStream = null;
	private String confFile = "src/main/resources/twitter4j.properties";
	BufferedWriter wd = null;

	/** The actual Twitter stream. It's set up to collect raw JSON data */
	private TwitterStream twitterStream;

	public BufferedTwitterReader(OutputStream stream) {
		this.outputStream = stream;
		setStream();
	}

	public BufferedTwitterReader(String confFile, OutputStream stream) {
		this.confFile = confFile;
		this.outputStream = stream;
		setStream();
	}

	private void setStream() {

		TwitterParams params = null;
		try {
			params = new TwitterParams(confFile);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}

		/** Twitter properties **/
		String consumerKey = params.getString(TwitterParams.CONSUMER_KEY_KEY);
		String consumerSecret = params
				.getString(TwitterParams.CONSUMER_SECRET_KEY);
		String accessToken = params.getString(TwitterParams.ACCESS_TOKEN_KEY);
		String accessTokenSecret = params
				.getString(TwitterParams.ACCESS_TOKEN_SECRET_KEY);

		ConfigurationBuilder conf = new ConfigurationBuilder();
		conf.setOAuthConsumerKey(consumerKey);
		conf.setOAuthConsumerSecret(consumerSecret);
		conf.setOAuthAccessToken(accessToken);
		conf.setOAuthAccessTokenSecret(accessTokenSecret);
		conf.setJSONStoreEnabled(true);
		conf.setIncludeEntitiesEnabled(true);

		twitterStream = new TwitterStreamFactory(conf.build()).getInstance();
		twitterStream.addListener(this);
	}

	public void run() {
		twitterStream.sample();
	}

	@Override
	public void onException(Exception arg0) {
		LOGGER.info(arg0);
		try {
			if (wd != null)
				wd.close();
		} catch (IOException ex) {
			LOGGER.fatal("IO Error while cleaning up", ex);
			LOGGER.trace(null, ex);
		}
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStallWarning(StallWarning arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatus(Status status) {
//		String text = status.getText();
//		String lang = status.getLang();
		// if (lang.equals("en")) {
		try {

			this.outputStream.write((TwitterObjectFactory.getRawJSON(status)+"\n").getBytes());
			// this.outputStream.write((lang + " :: " + text + "\n")
			// .getBytes());
		} catch (IOException e) {
			LOGGER.info(e);
		}

		// }

	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		// TODO Auto-generated method stub

	}
}
