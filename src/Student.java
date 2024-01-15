package main;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

//stores the information about each client in a single package
public class Student {
	private String name;
	private long daysSinceSurvey;
	private Calendar lastSurvey;

	/*
	 * constructors for the student class
	 */

	// new student object created when no date is specified (today is used)
	public Student(String name) {
		this.name = name;
		daysSinceSurvey = 0;
		lastSurvey = Calendar.getInstance();
	}

	// new student object created when a date is specified
	public Student(String name, Calendar lastSurvey) {
		this.name = name;
		this.lastSurvey = lastSurvey;
		update();
	}

	/*
	 * getters and setters for the student class
	 */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDays() {
		return daysSinceSurvey;
	}

	public Calendar getLastSurvey() {
		return lastSurvey;
	}

	public void setLastSurvey(Calendar lastSurvey) {
		this.lastSurvey = lastSurvey;
	}

	// calculates the number of days elapsed since the last survey was given
	public boolean update() {
		Calendar today = Calendar.getInstance();
		long millisecondDiff = getDiff(today, lastSurvey);
		long days = TimeUnit.MILLISECONDS.toDays(millisecondDiff);
		daysSinceSurvey = days;
		// we only care if the days elapsed are greater than or equal to 27 because that
		// means a new survey is due
		return daysSinceSurvey >= 27;
	}

	// calculates the difference in milliseconds between today and the day the last
	// survey was given
	public long getDiff(Calendar a, Calendar b) {
		long milliA = a.getTimeInMillis();
		long milliB = b.getTimeInMillis();
		return milliA - milliB;

	}

	// reset the last survey given to today
	public void giveSurvey() {
		lastSurvey = Calendar.getInstance();
		update();
	}

	// prints the student data in the format used to save and load the data
	public String toString() {
		return name + "|" + lastSurvey.get(Calendar.MONTH) + "|" + lastSurvey.get(Calendar.DAY_OF_MONTH) + "|"
				+ lastSurvey.get(Calendar.YEAR);
	}
}
