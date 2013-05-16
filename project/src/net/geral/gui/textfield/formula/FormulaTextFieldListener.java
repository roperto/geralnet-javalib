package net.geral.gui.textfield.formula;

import java.util.EventListener;

public interface FormulaTextFieldListener<T> extends EventListener {
	public abstract void valueChanged(FormulaTextField<T> src);
}
