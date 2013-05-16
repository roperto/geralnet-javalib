package net.geral.math;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FormatadorDecimal {
	private static final String					BASE	= "#,###,###,###,###,###,##0";
	private static final DecimalFormatSymbols	SIMBOLOS;

	static {
		SIMBOLOS = new DecimalFormatSymbols();
		SIMBOLOS.setDecimalSeparator(',');
		SIMBOLOS.setGroupingSeparator('.');
	}

	public static String formatar(final double d, final int decimais) {
		return (new FormatadorDecimal(decimais)).formatar(d);
	}

	private final DecimalFormat	formatter;

	public FormatadorDecimal(final int decimais) {
		final StringBuilder sb = new StringBuilder(BASE);

		if (decimais > 0) {
			sb.append('.');
			for (int i = 0; i < decimais; i++) {
				sb.append("0");
			}
		}

		formatter = new DecimalFormat(sb.toString(), SIMBOLOS);
	}

	public String formatar(final double d) {
		return formatter.format(d);
	}

	public String formatar(final int i) {
		return formatter.format((double)i);
	}
}
