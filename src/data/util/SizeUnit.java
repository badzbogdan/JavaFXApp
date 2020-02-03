package data.util;

import java.text.NumberFormat;
import java.util.Locale;

public enum SizeUnit {
	
	B(0),
	KB(10),
	MB(20),
	GB(30),
	TB(40);
	
	private int exp;
	
	private SizeUnit(int exp) {
		this.exp = exp;
	}
	
	public int getExponent() {
		return exp;
	}
	
	public long getBase() {
		return 1L << exp;
	}
	
	public long convertToLong(long value, SizeUnit unit) {
		long valueInBytes = value << exp;
		return valueInBytes / unit.getBase();
	}
	
	public double convertToDouble(long value, SizeUnit unit) {
		long valueInBytes = value << exp;
		return (double) valueInBytes / unit.getBase();
	}
	
	public String convertToString(long value, SizeUnit unit) {
		double convertedValue = convertToDouble(value, unit);
		return NumberFormat.getNumberInstance(Locale.US).format(convertedValue);
	}
	
}
