package com.core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class KafkaParams {

	public static final String BROKER_LIST = "broker_list";
	public static final String SERIALIZER = "serializer";
	public static final String REQUIRED_ACKS = "required_acks";
	public static final String KAFKA_TOPIC = "kafka_topic";
	public static final String BROKER_LIST_VALUE = "broker_list_value";
	public static final String SERIALIZER_VALUE = "serializer_value";
	public static final String REQUIRED_ACKS_VALUE = "required_acks_value";
	public static final String KAFKA_TOPIC_VALUE = "kafka_topic_value";
	public Properties prop;
	
	public KafkaParams(String confFile) throws Exception {
		prop = new Properties();
		InputStream is = new FileInputStream(confFile);
		prop.load(is);
		
		
		
	}
	
	
	public String getString(String key) {
		return prop.getProperty(key);
	}
}
	
	
