package main;

import java.awt.Label;
import java.io.BufferedInputStream;
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
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;


public class PrintHandler {

    private void delay(int msec) {
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public PrintHandler (FileInputStream fis, Label statusLabel) {
        this.statusLabel = statusLabel;
        this.fis = fis;
    }
    private FileInputStream fis;
    private Label statusLabel;
    private String state;

    public void startPrintJob () {
        try {
            //Platform.runLater(()->statusLabel.setText("PRINTING"));
            delay(5000);
            InputStream is = new BufferedInputStream(this.fis);
            DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            DocPrintJob printJob = service.createPrintJob();
            JobMonitor monitor = new JobMonitor();
            printJob.addPrintJobListener(monitor);
            Doc doc = new SimpleDoc(is, flavor, null);
            printJob.print(doc, null);
            monitor.waitForJobCompletion();
            is.close();
        } catch (PrintException | IOException e) {
            e.printStackTrace();
        }
    }

    private class JobMonitor extends PrintJobAdapter {
        private boolean notify = false;
        final int DATA_TRANSFERRED      = 10;
        final int JOB_COMPLETE          = 11;
        final int JOB_FAILED            = 12;
        final int JOB_CANCELED          = 13;
        final int JOB_NO_MORE_EVENTS    = 14;
        final int JOB_NEEDS_ATTENTION   = 15;

        private int status;
        @Override
        public void printDataTransferCompleted(PrintJobEvent pje) {
            status = DATA_TRANSFERRED;
            markAction();
        }
        @Override
        public void printJobCompleted(PrintJobEvent pje) {
            status = JOB_COMPLETE;
            markAction();
        }
        @Override
        public void printJobFailed(PrintJobEvent pje) {
            status = JOB_FAILED;
            markAction();
        }
        @Override
        public void printJobCanceled(PrintJobEvent pje) {
            status = JOB_CANCELED;
            markAction();
        }
        @Override
        public void printJobNoMoreEvents(PrintJobEvent pje) {
            status = JOB_NO_MORE_EVENTS;
            markAction();
        }
        @Override
        public void printJobRequiresAttention(PrintJobEvent pje) {
            status = JOB_NEEDS_ATTENTION;
            markAction();
        }
        private void markAction() {
            synchronized (JobMonitor.this) {
                notify = true;
                JobMonitor.this.notify();
            }
        }
        public synchronized void waitForJobCompletion() {
            Runnable runner = ()->{
                boolean keepRunning = true;
                while (keepRunning) {
                    try {
                        while (!notify) {
                            wait();
                        }
                        switch(this.status){
                            case DATA_TRANSFERRED:
                                state = "DATA_TRANSFERRED";
                                break;
                            case JOB_COMPLETE:
                                state = "JOB_FINISHED";
                                keepRunning = false;
                                break;
                            case JOB_FAILED:
                                state = "JOB_FAILED";
                                keepRunning = false;
                                break;
                            case JOB_CANCELED:
                                state = "JOB_CANCELED";
                                keepRunning = false;
                                break;
                            case JOB_NO_MORE_EVENTS:
                                state = "JOB_COMPLETE";
                                keepRunning = false;
                                break;
                            case JOB_NEEDS_ATTENTION:
                                state = "JOB_NEEDS_ATTENTION";
                                break;

                        }
                        //Platform.runLater(()->statusLabel.setText(state));
                        delay(5000);
                        notify = false;
                    }
                    catch (InterruptedException e) {}
                }
                delay(5000);
                //Platform.runLater(()->statusLabel.setText(""));
            };
            Thread monitor = new Thread(runner);
            monitor.start();
        }
    }
}
