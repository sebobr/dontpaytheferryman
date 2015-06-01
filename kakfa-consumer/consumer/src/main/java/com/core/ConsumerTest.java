package com.kafkaconsumer.groups;


import org.apache.log4j.Logger;
 
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
 
public class ConsumerTest implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ConsumerTest.class);
    private KafkaStream m_stream;
    private int m_threadNumber;
    private LogWriter logw;
    private FileWriter fw;
 
    public ConsumerTest(KafkaStream a_stream, int a_threadNumber) {
        m_threadNumber = a_threadNumber;
        m_stream = a_stream;
      
    	
    }
 
    public void run() {
       

  try {
    File fout = new File("outputlogs.txt");
    FileWriter fw = new FileWriter(fout.getName(), true);
    BufferedWriter bfw = new BufferedWriter(fw);
    ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
    while (it.hasNext()) 
		bfw.write(new String(it.next().message()) + "\n");
    bfw.close();
    System.out.println("WOOOOOOOOFFFFFFFFFFFFFFFFFOOOOOS");
    
    } catch(IOException e) {  e.printStackTrace(); }

 
		//    System.out.println("Thread " + m_threadNumber + ": " + new String(it.next().message()));

            	

            
       
        System.out.println("Shutting down Thread: " + m_threadNumber);
    }
}
