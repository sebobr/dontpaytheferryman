package com.core;

public class KafkaParams {

	public static final String BROKER_LIST = "metadata.broker.list";
	public static final String SERIALIZER = "serializer.class";
	public static final String REQUIRED_ACKS = "request.required.acks";
	public static final String KAFKA_TOPIC = "kafka.topic";

	public static final String BROKER_LIST_VALUE = "172.17.42.1:49154";
	public static final String SERIALIZER_VALUE = "kafka.serializer.StringEncoder";
	public static final String REQUIRED_ACKS_VALUE = "request.required.acks";
	public static final String KAFKA_TOPIC_VALUE = "tweets";
}
