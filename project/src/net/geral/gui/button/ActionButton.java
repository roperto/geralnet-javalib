package net.geral.gui.button;

import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.security.InvalidParameterException;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.KeyStroke;

public class ActionButton extends JButton implements KeyEventDispatcher {
  private static class TheAction extends AbstractAction {
    private static final long    serialVersionUID = 1L;
    private final AbstractButton button;

    public TheAction(final AbstractButton _btn) {
      button = _btn;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
      if (button.isEnabled()) {
        button.doClick();
      }
    }
  }

  private static final long serialVersionUID = 1L;

  private static String createHint(final KeyStroke ks) {
    final int mods = ks.getModifiers();
    String hint = "";
    if ((mods & InputEvent.CTRL_MASK) > 0) {
      hint += "Ctrl + ";
    }
    if ((mods & InputEvent.ALT_MASK) > 0) {
      hint += "Alt + ";
    }
    if ((mods & InputEvent.SHIFT_MASK) > 0) {
      hint += "Shift + ";
    }
    hint += KeyEvent.getKeyText(ks.getKeyCode());
    return hint;
  }

  private static int getVK(final char c) {
    if ((c < 'A') || (c > 'Z')) {
      throw new InvalidParameterException("char must be between A-Z");
    }
    return c;
  }

  private final String label;
  private final String hint;
  private int          minimumWidth = 0;

  public ActionButton(final String label, final char ctrlChar,
      final String actionCommand) {
    this(label, KeyStroke.getKeyStroke(getVK(ctrlChar), InputEvent.CTRL_MASK),
        actionCommand, null);
  }

  public ActionButton(final String label, final char ctrlChar,
      final String actionCommand, final ActionListener listener) {
    this(label, KeyStroke.getKeyStroke(getVK(ctrlChar), InputEvent.CTRL_MASK),
        actionCommand, listener);
  }

  public ActionButton(final String label, final int keyEventVK,
      final int inputEventMASK, final String actionCommand) {
    this(label, KeyStroke.getKeyStroke(keyEventVK, inputEventMASK),
        actionCommand);
  }

  public ActionButton(final String label, final int keyEventVK,
      final int inputEventMASK, final String actionCommand,
      final ActionListener listener) {
    this(label, KeyStroke.getKeyStroke(keyEventVK, inputEventMASK),
        actionCommand, listener);
  }

  public ActionButton(final String label, final KeyStroke ks,
      final String actionCommand) {
    this(label, ks, actionCommand, null);
  }

  public ActionButton(final String label, final KeyStroke ks,
      final String actionCommand, final ActionListener listener) {
    super(label);
    this.label = label;
    hint = createHint(ks);
    final TheAction action = new TheAction(this);
    setActionCommand(actionCommand);
    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(ks, actionCommand);
    getActionMap().put(actionCommand, action);
    KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .addKeyEventDispatcher(this);
    if (listener != null) {
      addActionListener(listener);
    }
  }

  @Override
  public boolean dispatchKeyEvent(final KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
      if (e.getID() == KeyEvent.KEY_PRESSED) {
        setText(hint);
      } else {
        setText(label);
      }
    }
    // always return false (never consume event)
    return false;
  }

  @Override
  public Dimension getPreferredSize() {
    final Dimension preferred = super.getPreferredSize();
    if (minimumWidth == 0) {
      minimumWidth = preferred.width;
    } else {
      minimumWidth = Math.max(minimumWidth, preferred.width);
    }
    preferred.width = minimumWidth;
    return preferred;
  }
}
