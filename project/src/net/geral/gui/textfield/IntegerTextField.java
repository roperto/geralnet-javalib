package net.geral.gui.textfield;

import net.geral.gui.textfield.formula.FormulaTextField;
import net.geral.math.FormatadorDecimal;

public class IntegerTextField extends FormulaTextField<Integer> {
	private static final long				serialVersionUID	= 1L;
	private static final FormatadorDecimal	formatador			= new FormatadorDecimal(0);

	private final boolean					allowNegative;

	public IntegerTextField(final boolean negativeAllowed) {
		super("[^0-9\\-\\+]");
		allowNegative = negativeAllowed;
	}

	@Override
	protected boolean evaluate() {
		try {
			//evaluate
			final String parts[] = getParts();
			if (parts.length == 0) {
				value = null;
				return true;
			}
			int iValue = 0;
			for (final String p : parts) {
				iValue += Integer.parseInt(p);
			}
			//check number
			if ((!allowNegative) && (iValue < 0)) return false;
			//ok
			value = new Integer(iValue);
			return true;
		}
		catch (final NumberFormatException e) {
			return false;
		}
	}

	public int getInteger() {
		return getValue(false).intValue();
	}

	@Override
	public Integer getValueForNull() {
		return new Integer(0);
	}

	@Override
	protected Integer stringToValue(final String s, final boolean nullAllowed) {
		if ((s == null) || (s.length() == 0)) return (nullAllowed ? null : getValueForNull());
		return Integer.parseInt(s);
	}

	@Override
	protected String valueToString(final Integer i) {
		return (i == null) ? "" : formatador.formatar(i);
	}
}
