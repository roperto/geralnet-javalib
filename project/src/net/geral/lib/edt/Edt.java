package net.geral.lib.edt;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

public class Edt {
  public static final Logger logger = Logger.getLogger(Edt.class);

  public static void required() {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new EdtViolationException("EDT required. Current: "
          + Thread.currentThread().getName());
    }
  }

  public static boolean run(final boolean sync, final Runnable r) {
    if (SwingUtilities.isEventDispatchThread()) {
      r.run();
      return true;
    }
    if (sync) {
      try {
        SwingUtilities.invokeAndWait(r);
        return true;
      } catch (final Exception e) {
        logger.warn(e, e);
        return false;
      }
    }
    SwingUtilities.invokeLater(r);
    return true;
  }
}
