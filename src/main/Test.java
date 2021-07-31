package main;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.standard.PrinterState;

class Test {

	  public static void main (String [] args)
	  {
	    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
	    System.out.println("Number of print services: " + printServices.length);
	    
	    
	    for (PrintService printer : printServices) {
	    	
	    	//AttributeSet attributes = printer.getAttributes();
	    	//String printerState = attributes.get(PrinterState.class)==null?"NUL":attributes.get(PrinterState.class).toString();
	    	System.out.println("Printer: " + printer.getName());
	    	System.out.println( printer.isAttributeCategorySupported(PrinterState.class) );
	    	
	    	Set<Attribute> set = getAttributes(printer);
	    	Iterator<Attribute> it = set.iterator();
	        while(it.hasNext()){
	        	Attribute attr = it.next();
	           System.out.println( attr.getName() + " = " + attr.getCategory() +  " ------ "+ attr.toString() );
	        }
	    	//	    	System.out.println(printerState);
	    }
	  }
	  
	  public static Set<Attribute> getAttributes(PrintService printer) {
		    Set<Attribute> set = new LinkedHashSet<Attribute>();

		    //get the supported docflavors, categories and attributes
		    Class<? extends Attribute>[] categories = (Class<? extends Attribute>[]) printer.getSupportedAttributeCategories();
		    DocFlavor[] flavors = printer.getSupportedDocFlavors();
		    AttributeSet attributes = printer.getAttributes();

		    //get all the avaliable attributes
		    for (Class<? extends Attribute> category : categories) {
		        for (DocFlavor flavor : flavors) {
		            //get the value
		            Object value = printer.getSupportedAttributeValues(category, flavor, attributes);

		            //check if it's something
		            if (value != null) {
		                //if it's a SINGLE attribute...
		                if (value instanceof Attribute)
		                    set.add((Attribute) value); //...then add it

		                //if it's a SET of attributes...
		                else if (value instanceof Attribute[])
		                    set.addAll(Arrays.asList((Attribute[]) value)); //...then add its childs
		            }
		        }
		    }

		    return set;
		}
	}
