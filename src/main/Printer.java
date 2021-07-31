package main;

import java.util.Locale;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.print.attribute.standard.PrinterState;

public class Printer {
	public static void main(String[] args) {

    	AttributeSet attributes = new HashPrintServiceAttributeSet(new PrinterName(null, Locale.getDefault()));
    		PrintService[] services = PrintServiceLookup.lookupPrintServices(
    		    DocFlavor.SERVICE_FORMATTED.PRINTABLE,
    		    attributes);
    		PrintService printService = services[0];
    		PrintServiceAttributeSet printServiceAttributes = printService.getAttributes();
    		PrinterState printerState = (PrinterState) printServiceAttributes.get(PrinterState.class);
    }
}
