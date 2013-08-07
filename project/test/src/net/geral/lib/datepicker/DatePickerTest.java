package net.geral.lib.datepicker;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.joda.time.LocalDate;

public class DatePickerTest extends JFrame {
  private static final long serialVersionUID = 1L;

  public static void main(final String[] args) {
    (new DatePickerTest()).setVisible(true);
  }

  final DatePickerPanel picker;

  static {
    try {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }

  public DatePickerTest() {
    getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    setSize(640, 480);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    picker = new DatePickerPanel();
    getContentPane().add(picker);
    picker.addDatePickerListener(new DatePickerListener() {
      @Override
      public void datePickerDateChanged(final LocalDate newDate) {
        System.out.println(newDate);
      }
    });
  }
}