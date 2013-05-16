package net.geral.lib.datepicker;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

public class DatePickerDayLabel extends JLabel implements MouseListener {
    private static final long serialVersionUID = 1L;
    private static final Color EXTRA_FG = Color.LIGHT_GRAY;
    private static final Color SELECIONADO_BG = Color.BLACK;
    private static final Color SELECIONADO_FG = Color.WHITE;
    private static final Color FDS_BG = new Color(255, 204, 204);
    private static final Color FDS_FG = Color.BLACK;
    private static final Color SEMANA_BG = new Color(255, 255, 238);
    private static final Color SEMANA_FG = Color.BLACK;
    private static final Color MOUSEOVER_BG = new Color(0, 0, 96);
    private static final Color MOUSEOVER_FG = Color.WHITE;

    private final DatePickerPanel feedback;
    private LocalDate data = LocalDate.now();
    private boolean extra = false;
    private boolean mouseOver = false;

    public DatePickerDayLabel(final DatePickerPanel calendario) {
	feedback = calendario;
	setOpaque(true);
	setHorizontalAlignment(SwingConstants.CENTER);
	addMouseListener(this);
    }

    public LocalDate getDate() {
	return data;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
	feedback.setDate(data);
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
	mouseOver = true;
	updateColor();
    }

    @Override
    public void mouseExited(final MouseEvent e) {
	mouseOver = false;
	updateColor();
    }

    @Override
    public void mousePressed(final MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(final MouseEvent arg0) {
    }

    public void setDate(final LocalDate d, final boolean diaExtra) {
	data = d;
	extra = diaExtra;
	setText(String.valueOf(d.getDayOfMonth()));
	setHorizontalAlignment(SwingConstants.CENTER);
	updateColor();
    }

    public void updateColor() {
	if (mouseOver) {
	    setBackground(MOUSEOVER_BG);
	    setForeground(MOUSEOVER_FG);
	} else {
	    if (feedback.isSelected(this)) {
		setBackground(SELECIONADO_BG);
		setForeground(SELECIONADO_FG);
	    } else {
		final int dow = data.getDayOfWeek();
		if ((dow == DateTimeConstants.SATURDAY)
			|| (dow == DateTimeConstants.SUNDAY)) {
		    setBackground(FDS_BG);
		    setForeground(extra ? EXTRA_FG : FDS_FG);
		} else {
		    setBackground(SEMANA_BG);
		    setForeground(extra ? EXTRA_FG : SEMANA_FG);
		}
	    }
	}
    }
}
