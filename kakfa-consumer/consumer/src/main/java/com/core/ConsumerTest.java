package com.kafkaconsumer.groups;
 
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
 
public class ConsumerTest implements Runnable {
    private KafkaStream m_stream;
    private int m_threadNumber;
    private LogWriter logw;
    private OutputStreamWriter osw;
 
    public ConsumerTest(KafkaStream a_stream, int a_threadNumber, LogWriter logw) {
        m_threadNumber = a_threadNumber;
        m_stream = a_stream;
        this.logw = logw;
        osw = logw.getWriter();
    }
 
    public void run() {
        ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
        while (it.hasNext())
           osw.write(new String(it.next().message()));
           // System.out.println("Thread " + m_threadNumber + ": " + new String(it.next().message()));
        System.out.println("Shutting down Thread: " + m_threadNumber);
    }
}
