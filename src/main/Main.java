package main;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.PrinterStateReason;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
		AttributeSet attributes = printService.getAttributes();
		String printerState = attributes.get(PrinterState.class).toString();
		String printerStateReason = attributes.get(PrinterStateReason.class).toString();
		
		System.out.println("printerState = " + printerState); // May be IDLE, PROCESSING, STOPPED or UNKNOWN
		System.out.println("printerStateReason = " + printerStateReason); // If your printer state returns STOPPED, for example, you can identify the reason 

		if( printerState.equals( PrinterState.STOPPED.toString() ) ) {
			if( printerStateReason.equals(PrinterStateReason.TONER_LOW.toString()) ) {
				System.out.println("Toner level is low!");
			}
		}
		

	}

}
