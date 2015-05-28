package com.kafkaconsumer.groups;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.io.OutputStreamWriter;

public class LogWriter {
  private OutputStreamWriter osw;
  
  
  public LogWriter(String path) {
    File fout = new File(path);
	  FileOutputStream fos = new FileOutputStream(fout);
 	  osw = new OutputStreamWriter(fos);
  
  }

  public OutputStreamWriter getWriter() {
  return osw;
  }



}
