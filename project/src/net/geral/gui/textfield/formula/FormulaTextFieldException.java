package net.geral.gui.textfield.formula;

public class FormulaTextFieldException extends Exception {
	private static final long	serialVersionUID	= 1L;

	public FormulaTextFieldException(final NumberFormatException e) {
		super(e);
	}

	public FormulaTextFieldException(final String string) {
		super(string);
	}
}
