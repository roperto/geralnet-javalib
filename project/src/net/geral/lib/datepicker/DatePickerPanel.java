package net.geral.lib.datepicker;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

public class DatePickerPanel extends JPanel implements ActionListener {
  private static DatePickerConfiguration defaultConfiguration = new DatePickerConfiguration();

  private static final long              serialVersionUID     = 1L;
  private static final int               ROWS                 = 6;
  private static final int               COLUMNS              = 7;
  private static final DateFormatSymbols symbols              = new DateFormatSymbols();

  public static DatePickerConfiguration getDefaultConfiguration() {
    return defaultConfiguration;
  }

  public static void setDefaultConfiguration(final DatePickerConfiguration cfg) {
    defaultConfiguration = cfg;
  }

  private final JLabel[]                weekTitles   = new JLabel[COLUMNS];
  private final DatePickerDayLabel[]    daysLabels   = new DatePickerDayLabel[ROWS
                                                         * COLUMNS];
  private final JComboBox<String>       cbMonth      = new JComboBox<String>();
  private final JComboBox<Integer>      cbYears      = new JComboBox<Integer>();
  private final EventListenerList       listeners    = new EventListenerList();

  private final DatePickerConfiguration config;
  private DatePickerDayLabel            selected     = null;

  private boolean                       doNotSetDate = false;

  public DatePickerPanel() {
    this(defaultConfiguration);
  }

  public DatePickerPanel(final DatePickerConfiguration cfg) {
    config = cfg;
    setLayout(new BorderLayout(0, 0));
    add(createYearMonthSelector(), BorderLayout.NORTH);
    add(createDaysTable(), BorderLayout.CENTER);
    setBorder(BorderFactory.createLineBorder(config.borderColor));
    setDate(LocalDate.now());
    cbYears.setEditable(true);
    ((JTextField) cbYears.getEditor().getEditorComponent()).setColumns(3);
    // add event listeners only after all set up
    cbMonth.addActionListener(this);
    cbYears.addActionListener(this);
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    if ("today".equals(e.getActionCommand())) {
      setDate(LocalDate.now());
      return;
    }

    if ("comboBoxChanged".equals(e.getActionCommand())) {
      updateMesAno();
      return;
    }
  }

  public void addDatePickerListener(final DatePickerListener l) {
    listeners.add(DatePickerListener.class, l);
  }

  private JPanel createDaysTable() {
    final JPanel table = new JPanel(new GridLayout(0, COLUMNS, 0, 0));
    createDaysTableTitles(table);
    createDaysTableDays(table);
    return table;
  }

  private void createDaysTableDays(final JPanel table) {
    final int rc = ROWS * COLUMNS;
    for (int i = 0; i < rc; i++) {
      daysLabels[i] = new DatePickerDayLabel(this);
      table.add(daysLabels[i]);
    }
    selected = daysLabels[0];
  }

  private void createDaysTableTitles(final JPanel table) {
    final Font f = getFont().deriveFont(Font.BOLD);
    for (int i = 0; i < COLUMNS; i++) {
      final JLabel lbl = new JLabel();
      weekTitles[i] = lbl;
      String weekday = symbols.getWeekdays()[i + 1];
      if (weekday.length() > config.weekAbbreviatedSize) {
        weekday = weekday.substring(0, config.weekAbbreviatedSize);
      }
      lbl.setText(" " + weekday + " ");
      lbl.setHorizontalAlignment(SwingConstants.CENTER);
      final boolean weekend = ((i == 0) || (i == 6));
      lbl.setBackground(weekend ? config.titleBackgroundWeekend
          : config.titleBackgroundWorkday);
      lbl.setForeground(config.titleForeground);
      lbl.setFont(f);
      lbl.setOpaque(true);
      table.add(weekTitles[i]);
    }
  }

  private JPanel createYearMonthSelector() {
    for (final String s : symbols.getMonths()) {
      cbMonth.addItem(s);
    }

    final int currentYear = LocalDate.now().getYear();
    final int fromYear = currentYear + config.yearFrom;
    final int toYear = currentYear + config.yearTo;
    for (int y = fromYear; y <= toYear; y++) {
      cbYears.addItem(new Integer(y));
    }

    final JButton btnToday = new JButton(config.todayLabel);
    btnToday.setActionCommand("today");
    btnToday.addActionListener(this);

    final JPanel todayMonthYear = new JPanel(new BorderLayout());
    todayMonthYear.add(btnToday, BorderLayout.WEST);
    todayMonthYear.add(cbMonth, BorderLayout.CENTER);
    todayMonthYear.add(cbYears, BorderLayout.EAST);

    if (config.monthYearBackground != null) {
      todayMonthYear.setOpaque(true);
      todayMonthYear.setBackground(config.monthYearBackground);
    }

    return todayMonthYear;
  }

  private void fireCalendarioChangedDate(final LocalDate newDate) {
    for (final DatePickerListener dpl : listeners
        .getListeners(DatePickerListener.class)) {
      dpl.datePickerDateChanged(newDate);
    }
  }

  public LocalDate getDate() {
    return selected.getDate();
  }

  boolean isSelected(final DatePickerDayLabel cdl) {
    return selected == cdl;
  }

  public void removeDatePickerListener(final DatePickerListener l) {
    listeners.remove(DatePickerListener.class, l);
  }

  private void seletorSetDate(final LocalDate newData) {
    final int toMonth = newData.getMonthOfYear() - 1;
    final int toYear = newData.getYear();
    if (cbMonth.getSelectedIndex() != toMonth) {
      cbMonth.setSelectedIndex(toMonth);
    }
    final Object o = cbYears.getSelectedItem();
    if (!(o instanceof Integer) || (((Integer) o).intValue() != toYear)) {
      cbYears.setSelectedItem(new Integer(toYear));
    }
  }

  public void setDate(final LocalDate newDate) {
    doNotSetDate = true;
    seletorSetDate(newDate);
    doNotSetDate = false;
    LocalDate tmp = newDate;
    tmp = tmp.withDayOfMonth(1);// first day of month
    tmp = tmp.plusDays(-7);// one week back
    tmp = tmp.withDayOfWeek(DateTimeConstants.SUNDAY);// next sunday

    for (final DatePickerDayLabel cdl : daysLabels) {
      if (tmp.equals(newDate)) {
        selected = cdl;
      }
      cdl.setDate(tmp, newDate.getMonthOfYear() != tmp.getMonthOfYear());
      tmp = tmp.plusDays(1);
    }

    for (final DatePickerDayLabel cdl : daysLabels) {
      cdl.updateColor();
    }

    fireCalendarioChangedDate(newDate);
  }

  private void updateMesAno() {
    if (selected == null) {
      return; // component not yet created
    }
    LocalDate d = selected.getDate();
    int year;
    try {
      final Object o = cbYears.getSelectedItem();
      if (o instanceof Integer) {
        year = ((Integer) o).intValue();
      } else {
        year = Integer.parseInt(o.toString());
      }
    } catch (final NumberFormatException e) {
      year = d.getYear();
    }
    d = d.withYear(year).withMonthOfYear(cbMonth.getSelectedIndex() + 1);
    if (!doNotSetDate) {
      setDate(d);
    }
  }
}
