package com.pomodoit.db;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Session {

	private int id;
	private String name;
	private float mark;
	private String date;

	// Constructor
	public Session(String n, float m) {
		this.name = n;
		this.mark = m;
		this.date = getDate();
	}
	
	public Session() {
		// Nothing.
	}

	// Getter & Setter
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getMark() {
		return mark;
	}
	public void setMark(float mark) {
		this.mark = mark;
	}

	public String getDate() {
		return this.date;
	}
	
	public String createDate() {
		Calendar c = new GregorianCalendar();
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DATE);
		String mm = Integer.toString(m);
		String dd = Integer.toString(d);
		return "" + c.get(Calendar.YEAR) + (m < 10 ? "0" + mm : mm) +
				(d < 10 ? "0" + dd : dd);	
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
