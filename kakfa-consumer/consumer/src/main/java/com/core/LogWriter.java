package com.kafkaconsumer.groups;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

public class LogWriter {
  private OutputStreamWriter osw;
  
  
  public LogWriter(String path) {
    File fout = new File(path);
    
    try {
	  FileOutputStream fos = new FileOutputStream(fout);
	  osw = new OutputStreamWriter(fos);
    } catch (FileNotFoundException e) {
        
        e.printStackTrace(); }
    
  
  
  }

  public OutputStreamWriter getWriter() {
  return osw;
  }



}
