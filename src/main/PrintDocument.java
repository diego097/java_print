package main;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSize;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

public class PrintDocument {

	
	  public void printText(String text) throws PrintException, IOException {

	    PrintService service = PrintServiceLookup.lookupDefaultPrintService();
	    System.out.println( "Impresora" + service.getName() );
	    
	   // InputStream is = new ByteArrayInputStream(text.getBytes("UTF8"));
	    //prints the famous hello world! plus a form feed
	   // InputStream is = new ByteArrayInputStream("".getBytes("UTF8"));
	    
	    FileInputStream text1 = new FileInputStream("/file.txt");
	    System.out.println(text1.read());
	    PrintRequestAttributeSet  pras = new HashPrintRequestAttributeSet();
	    pras.add(new Copies(1));
	    //pras.add(MediaSize.ISO.A4);
	    
	    DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
	    //DocFlavor flavor = DocFlavor.INPUT_STREAM.TEXT_PLAIN_US_ASCII;
	    Doc doc = new SimpleDoc(text1, flavor, null);
	   
	    DocPrintJob job = service.createPrintJob();

	    PrintJobWatcher pjw = new PrintJobWatcher(job);
	    job.print(doc, pras);
	    pjw.waitForDone();
	    //is.close();
	    System.out.println("IMPRESON TERMINADA");
	  }
	}

	class PrintJobWatcher {
	  boolean done = false;

	  PrintJobWatcher(DocPrintJob job) {
	    job.addPrintJobListener(new PrintJobAdapter() {
	      public void printJobCanceled(PrintJobEvent pje) {
	        allDone();
	      }
	      public void printJobCompleted(PrintJobEvent pje) {
	        allDone();
	      }
	      public void printJobFailed(PrintJobEvent pje) {
	        allDone();
	      }
	      public void printJobNoMoreEvents(PrintJobEvent pje) {
	        allDone();
	      }
	      void allDone() {
	        synchronized (PrintJobWatcher.this) {
	          done = true;
	          System.out.println("Printing document is done ...");
	          PrintJobWatcher.this.notify();
	        }
	      }
	    });
	  }
	  public synchronized void waitForDone() {
	    try {
	      while (!done) {
	        wait();
	      }
	    } catch (InterruptedException e) {
	    }
	  }
	}
