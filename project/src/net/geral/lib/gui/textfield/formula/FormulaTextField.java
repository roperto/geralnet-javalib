package net.geral.lib.gui.textfield.formula;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;

import net.geral.lib.textfieds.ErrorFieldSetter;
import net.geral.lib.util.StringUtils;

public abstract class FormulaTextField<T> extends JTextField implements
    DocumentListener, FocusListener, Runnable {
  private static final long       serialVersionUID = 1L;

  private String                  filter           = null;
  private boolean                 hasError         = false;

  protected T                     value            = null;
  private final EventListenerList listeners        = new EventListenerList();
  private ErrorFieldSetter        efs              = null;

  public FormulaTextField(final String filterRegex) {
    filter = filterRegex;
    getDocument().addDocumentListener(this);
    addFocusListener(this);
    setHorizontalAlignment(TRAILING);
    SwingUtilities.invokeLater(this);
  }

  public void addChangeListener(final FormulaTextFieldListener<T> l) {
    listeners.add(FormulaTextFieldListener.class, l);
  }

  private void applyFilter() {
    if (filter == null) {
      return;
    }
    int pos = getCaretPosition();
    String text = getText();
    final int len = text.length();
    text = text.replaceAll(filter, "");
    if (len == text.length()) {
      return; // no changes
    }
    super.setText(text); // use super, here we override to setValue
    // if just typed, try to guess new caret position
    pos--;
    if (pos < 0) {
      pos = 0;
    }
    if (pos > text.length()) {
      pos = text.length();
    }
    setCaretPosition(pos);
  }

  @Override
  public void changedUpdate(final DocumentEvent e) {
    SwingUtilities.invokeLater(this);
  }

  protected void checkColor() {
    hasError = !evaluate();
    System.err.println(hasError);
    efs().set(this, !hasError);
  }

  public ErrorFieldSetter efs() {
    if (efs == null) {
      efs = new ErrorFieldSetter();
    }
    return efs;
  }

  protected abstract boolean evaluate();

  @SuppressWarnings("unchecked")
  protected void fireChanged() {
    final Object[] ls = listeners.getListenerList();
    for (int i = 0; i < ls.length; i += 2) {
      if (ls[i] == FormulaTextFieldListener.class) {
        ((FormulaTextFieldListener<T>) ls[i + 1]).valueChanged(this);
      }
    }
  }

  @Override
  public void focusGained(final FocusEvent e) {
    applyFilter();
    valueChanged();
  }

  @Override
  public void focusLost(final FocusEvent e) {
    if (!hasError) {
      format(true);
    }
  }

  private void format(final boolean evaluateCurrentText) {
    if (evaluateCurrentText) {
      setValue(stringToValue(getText(), true));
    } else {
      super.setText(valueToString(value));
    }
  }

  public String[] getParts() {
    final String t = StringUtils.trim(getText());
    if (t.length() == 0) {
      return new String[0];
    }
    return t.split("(?=[\\+\\-])");
  }

  public T getValue(final boolean nullAllowed) {
    if (value == null) {
      return nullAllowed ? null : getValueForNull();
    }
    return value;
  }

  protected abstract T getValueForNull();

  public boolean hasError() {
    return hasError;
  }

  @Override
  public void insertUpdate(final DocumentEvent e) {
    SwingUtilities.invokeLater(this);
  }

  public void removeChangeListener(final FormulaTextFieldListener<T> l) {
    listeners.remove(FormulaTextFieldListener.class, l);
  }

  @Override
  public void removeUpdate(final DocumentEvent e) {
    SwingUtilities.invokeLater(this);
  }

  @Override
  public void run() {
    // invokelater from document change
    valueChanged();
  }

  public void setErrorFieldSetter(final ErrorFieldSetter efs) {
    this.efs = efs;
  }

  @Deprecated
  @Override
  public final void setText(final String t) {
    super.setText(t);
    format(true);
  }

  public void setValue(final T v) {
    value = v;
    format(false);
  }

  protected abstract T stringToValue(String s, boolean nullAllowed);

  private void valueChanged() {
    applyFilter();
    checkColor();
    fireChanged();
  }

  protected abstract String valueToString(T v);
}
