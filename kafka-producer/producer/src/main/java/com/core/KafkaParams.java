package com.core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class KafkaParams {

	public String BROKER_LIST = "metadata.broker.list";
	public String SERIALIZER = "serializer.class";
	public String REQUIRED_ACKS = "request.required.acks";
	public String KAFKA_TOPIC = "kafka.topic";
	public String BROKER_LIST_VALUE = "172.17.42.1:49154";
	public String SERIALIZER_VALUE = "kafka.serializer.StringEncoder";
	public String REQUIRED_ACKS_VALUE = "request.required.acks";
	public String KAFKA_TOPIC_VALUE = "tweets";
	
	public KafkaParams(String confFile) {
		prop = new Properties();
		InputStream is = new FileInputStream(confFile);
		prop.load(is);
		
		
		
	}
	
	
	public String getString(String key) {
		return prop.getProperty(key);
	}
}
	
	
}
