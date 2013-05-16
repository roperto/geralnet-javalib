package net.geral.gui.textfield;

import net.geral.gui.textfield.formula.FormulaTextField;
import net.geral.math.FormatadorDecimal;

public class DoubleTextField extends FormulaTextField<Double> {
	private static final long		serialVersionUID	= 1L;

	private final FormatadorDecimal	formatador;
	private final boolean			allowNegative;

	public DoubleTextField(final boolean negativeAllowed, final int decimals) {
		super("[^0-9\\-\\+\\,]");
		allowNegative = negativeAllowed;
		formatador = new FormatadorDecimal(decimals);
	}

	@Override
	protected boolean evaluate() {
		try {
			final String parts[] = getParts();
			if (parts.length == 0) {
				value = null;
				return true;
			}
			double d = 0.0;
			for (final String p : parts) {
				final Double pd = stringToValue(p, true);
				if (pd == null) return false;
				d += pd;
			}
			if ((!allowNegative) && (d < 0.0)) return false;
			value = new Double(d);
			return true;
		}
		catch (final NumberFormatException e) {
			return false;
		}
	}

	@Override
	protected Double getValueForNull() {
		return new Double(0.0);
	}

	@Override
	protected Double stringToValue(final String s, final boolean nullAllowed) {
		if ((s == null) || (s.length() == 0)) return (nullAllowed ? null : getValueForNull());
		return Double.parseDouble(s.replace(".", "").replace(',', '.'));
	}

	@Override
	protected String valueToString(final Double v) {
		return (value == null) ? "" : formatador.formatar(value);
	}
}
