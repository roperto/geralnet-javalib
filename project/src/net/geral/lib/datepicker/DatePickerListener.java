package net.geral.lib.datepicker;

import java.util.EventListener;

import org.joda.time.LocalDate;

public interface DatePickerListener extends EventListener {
	public void datePickerDateChanged(LocalDate newDate);
}