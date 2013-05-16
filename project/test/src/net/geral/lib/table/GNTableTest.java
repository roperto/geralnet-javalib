package net.geral.lib.table;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.log4j.BasicConfigurator;

public class GNTableTest extends JFrame implements Runnable {
  private static final long serialVersionUID = 1L;

  public static void main(final String[] args) {
    BasicConfigurator.configure();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new GNTableTest().setVisible(true);
      }
    });
  }

  public GNTableTest() {
    setSize(800, 600);
    setLayout(new BorderLayout());
    add(new GNTableTest_Table().getScroll(), BorderLayout.CENTER);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    new Thread(this).start();
  }

  @Override
  public void run() {
    // check EDT violation?
    // add(new JLabel("EDT violation"), BorderLayout.NORTH);
  }
}
