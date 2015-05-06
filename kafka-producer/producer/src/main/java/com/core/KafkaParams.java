package com.core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class KafkaParams {

	public String BROKER_LIST = "broker_list";
	public String SERIALIZER = "serializer";
	public String REQUIRED_ACKS = "required_acks";
	public String KAFKA_TOPIC = "kafka_topic";
	public String BROKER_LIST_VALUE = "broker_list_value";
	public String SERIALIZER_VALUE = "serializer_value";
	public String REQUIRED_ACKS_VALUE = "required_acks_value";
	public String KAFKA_TOPIC_VALUE = "kafka_topic_value";
	
	public KafkaParams(String confFile) {
		prop = new Properties();
		InputStream is = new FileInputStream(confFile);
		prop.load(is);
		
		
		
	}
	
	
	public String getString(String key) {
		return prop.getProperty(key);
	}
}
	
	
