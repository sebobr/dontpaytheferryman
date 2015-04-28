package com.core;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class KafkaThreadProducer {
	private static final Logger LOGGER = Logger
			.getLogger(KafkaThreadProducer.class);
	private static final int BUFFER_LEN = 4096;

	public static void main(String[] args) {
		if (args.length < 1)
			throw new RuntimeException("Not enough arguments were passed");
		BasicConfigurator.configure();
		if (args[0].equals("0")) {
			for (int i = 1; i < args.length; i++) {
				produceForFile(args[i]);
			}
		} else {
			produceFromTwitter();
		}
	}

	private static void produceFromTwitter() {
		try {
			LOGGER.debug("Setting up streams");
			PipedInputStream send = new PipedInputStream();
			PipedOutputStream input = new PipedOutputStream(send);

			LOGGER.debug("Setting up connections");
			LOGGER.debug("Setting up file reader");
			BufferedTwitterReader reader = new BufferedTwitterReader( input);
			LOGGER.debug("Setting up kafka producer");
			KafkaProducer kafkaProducer = new KafkaProducer(
					KafkaParams.KAFKA_TOPIC_VALUE, send);

			LOGGER.debug("Spinning up threads");
			Thread source = new Thread(reader);
			Thread kafka = new Thread(kafkaProducer);

			source.start();
			kafka.start();
			kafka.join();

			LOGGER.debug("Joining");
		} catch (IOException ex) {
			LOGGER.fatal("IO Error while piping", ex);
			LOGGER.trace(null, ex);
		} catch (InterruptedException ex) {
			LOGGER.warn("interruped", ex);
			LOGGER.trace(null, ex);
		}
	}

	private static void produceForFile(String filename) {
		try {
			LOGGER.debug("Setting up streams");
			PipedInputStream send = new PipedInputStream(BUFFER_LEN);
			PipedOutputStream input = new PipedOutputStream(send);

			LOGGER.debug("Setting up connections");
			LOGGER.debug("Setting up file reader");
			BufferedFileReader reader = new BufferedFileReader(filename, input);
			LOGGER.debug("Setting up kafka producer");
			KafkaProducer kafkaProducer = new KafkaProducer(
					KafkaParams.KAFKA_TOPIC_VALUE, send);

			LOGGER.debug("Spinning up threads");
			Thread source = new Thread(reader);
			Thread kafka = new Thread(kafkaProducer);

			source.start();
			kafka.start();
			kafka.join();

			LOGGER.debug("Joining");
		} catch (IOException ex) {
			LOGGER.fatal("IO Error while piping", ex);
			LOGGER.trace(null, ex);
		} catch (InterruptedException ex) {
			LOGGER.warn("interruped", ex);
			LOGGER.trace(null, ex);
		}
	}
}
