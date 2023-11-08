package it.betacom.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FormattatoreDouble {
	
	public static double formatta(double daFormattare) {
		DecimalFormat df = new DecimalFormat("0.00");
		DecimalFormatSymbols decimalDecimalFormatSymbols = new DecimalFormatSymbols();
		decimalDecimalFormatSymbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(decimalDecimalFormatSymbols);
		return Double.parseDouble(df.format(daFormattare));
	}
	
}
