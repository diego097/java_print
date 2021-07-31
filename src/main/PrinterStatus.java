package main;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;

public class PrinterStatus {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		PrintService printer = PrintServiceLookup.lookupDefaultPrintService();
		AttributeSet att = printer.getAttributes();
		
		for (Attribute a : att.toArray()) {
		
			String attributeName;String attributeValue;
			attributeName = a.getName();
			attributeValue = att.get(a.getClass()).toString();
			
			System.out.println(attributeName + " : " + attributeValue);
			
			
			
			
			
			
		}
	}

}
