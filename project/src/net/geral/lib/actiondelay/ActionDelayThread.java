package net.geral.lib.actiondelay;

import org.apache.log4j.Logger;

public class ActionDelayThread<T> extends Thread {
  private static final Logger  logger  = Logger
                                           .getLogger(ActionDelayThread.class);
  private boolean              running = false;
  long                         execute = 0;
  private final long           checkDelay;
  private final long           delay;
  private final ActionDelay<T> actionDelay;

  public ActionDelayThread(final ActionDelay<T> actionDelay, final String name,
      final long delay, final long checkDelay) {
    this.actionDelay = actionDelay;
    this.checkDelay = checkDelay;
    this.delay = delay;
    setName(name);
    start();
  }

  public void finish() {
    running = false;
  }

  public synchronized void prepare() {
    execute = System.currentTimeMillis() + delay;
  }

  @Override
  public void run() {
    logger.debug("ActionDelay #" + getId() + " (" + getName() + ") started.");
    while (running) {
      synchronized (this) {
        if (actionDelay.hasAction()) {
          if (System.currentTimeMillis() >= execute) {
            actionDelay.fireDelayedAction();
          }
        }
      }
      try {
        Thread.sleep(checkDelay);
      } catch (final InterruptedException e) {
        logger.warn(e, e);
      }
    }
    logger.debug("ActionDelay #" + getId() + " (" + getName() + ") stopped.");
  }

  @Override
  public synchronized void start() {
    if (running) {
      return;
    }
    running = true;
    super.start();
  }
}