package net.geral.lib.actiondelay;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.apache.log4j.Logger;

public class ActionDelay<T> implements AncestorListener, Runnable {
  private static final Logger          logger              = Logger
                                                               .getLogger(ActionDelay.class);
  private static final long            DEFAULT_CHECK_DELAY = 100;
  private static final long            DEFAULT_DELAY       = 500;

  private final long                   checkDelay;
  private final long                   delay;
  private final ActionDelayListener<T> listener;
  private final String                 name;
  private T                            action;
  private ActionDelayThread<T>         thread;

  public ActionDelay(final String name, final JComponent ancestor,
      final ActionDelayListener<T> listener) {
    ancestor.addAncestorListener(this);
    this.name = name;
    this.listener = listener;
    this.checkDelay = DEFAULT_CHECK_DELAY;
    this.delay = DEFAULT_DELAY;
  }

  @Override
  public void ancestorAdded(final AncestorEvent event) {
    start();
  }

  @Override
  public void ancestorMoved(final AncestorEvent event) {
  }

  @Override
  public void ancestorRemoved(final AncestorEvent event) {
    stop();
  }

  synchronized void fireDelayedAction() {
    SwingUtilities.invokeLater(this);
  }

  public T getAction() {
    return action;
  }

  public boolean hasAction() {
    return (action != null);
  }

  public synchronized void prepare(final T newAction) {
    if (thread == null) {
      logger.debug("Action (" + name + ") thread not running. Executing now: "
          + newAction);
      action = newAction;
      fireDelayedAction();
      return;
    }
    if (action != null) {
      logger.debug("Action (" + name + ") cancelled: " + action.toString());
    }
    logger.debug("Action (" + name + ") prepared: " + newAction);
    action = newAction;
    thread.prepare();
  }

  @Override
  public void run() {
    try {
      if (action == null) {
        logger.debug("firing action: " + null);
      } else {
        logger.debug("firing action (" + action.getClass().getSimpleName()
            + "): " + action.toString());
      }

      listener.delayedAction(action);
    } finally {
      action = null;
    }
  }

  public synchronized void start() {
    stop();
    thread = new ActionDelayThread<>(this, name, delay, checkDelay);
  }

  public synchronized void stop() {
    if (thread != null) {
      thread.finish();
    }
    thread = null;
  }
}