package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Main {

	ArrayList<Student> students = new ArrayList<Student>();

	File backup;

	String path = "save.data";
	File inputFile = new File(path);

	public Main() {
		load();
	}

	public void load() {
		try {
			students = new ArrayList<Student>();
			backup = new File("backup.data");
			backup.createNewFile();

			// if the file exists, load data
			if (!inputFile.createNewFile()) {
				Scanner input = new Scanner(inputFile);

				// start reading file input
				while (input.hasNextLine()) {

					// format: Name|Month|Day|Year

					String student = input.nextLine();
					// get name
					String name = student.substring(0, student.indexOf('|'));
					student = student.substring(student.indexOf('|') + 1);

					// create new calendar
					Calendar lastSurvey = Calendar.getInstance();

					// get month
					int month = Integer.parseInt(student.substring(0, student.indexOf('|')));
					student = student.substring(student.indexOf('|') + 1);

					// get day
					int day = Integer.parseInt(student.substring(0, student.indexOf('|')));
					student = student.substring(student.indexOf('|') + 1);

					// get year
					int year = Integer.parseInt(student);

					// set date fields
					lastSurvey.set(Calendar.MONTH, month);
					lastSurvey.set(Calendar.DAY_OF_MONTH, day);
					lastSurvey.set(Calendar.YEAR, year);

					// add student to list
					Student s = new Student(name, lastSurvey);
					students.add(s);
				}
				// stop reading file input
				input.close();
			}
		} catch (IOException e) {
			println("File not found");
		}
	}

	// save all data to multiple local files
	public void save() {
		boolean saveBackUp = true;
		try {
			// open output to file
			inputFile.createNewFile();
			PrintWriter pw = new PrintWriter(inputFile);

			// write each student's info to the file
			for (Student s : students) {
				pw.println(s);
			}

			// stop output
			pw.close();
		} catch (IOException e) {
			// tell user an error has occurred
			println("Error while saving, check \'error.log\' for details");
			try {
				// create file for error logging
				File error = new File("error.log");
				PrintWriter pw = new PrintWriter(error);

				// output the error message
				pw.println("Copy + Paste or Screenshot the following and email it to nhout2@uis.edu");
				pw.println(e.getStackTrace());

				// stop outputting
				pw.close();
			} catch (IOException f) {
				// that's just unfortunate but not unexpected
				println("Error while writing to \'error.log\'");
			}
			// if an error occurs while saving, a backup is not saved to reduce risk of file
			// corruption and save data loss
			saveBackUp = false;
		}
		// only save to backup if no error occurred
		if (saveBackUp) {
			try {
				// create the backup file
				backup = new File("backup.data");
				backup.createNewFile();

				// start output
				PrintWriter pw = new PrintWriter(backup);

				// print each student's data to the backup
				for (Student s : students) {
					pw.println(s);
				}

				// stop output
				pw.close();
			} catch (IOException e) {
				// still bad but at least the original save data exists. This will likely never
				// happen
				println("Error while saving backup");
			}
		}
	}

	// add a student to the list
	public void add() {
		// prompt user for info
		// ask for name
		String name = JOptionPane.showInputDialog(null, "Enter the student's name", "Add a Student",
				JOptionPane.QUESTION_MESSAGE);

		// if the user presses the Cancel button, go back
		if (name == null || name.isEmpty()) {
			return;
		}

		// ask for date but not in a romantic way
		String date = JOptionPane.showInputDialog(null, "Enter the last surveyed date (M/D/Y). Leave blank for today.",
				"Add a Student", JOptionPane.QUESTION_MESSAGE);

		// interpret data entered to actual date
		// start with the current time / date
		Calendar lastSurvey = Calendar.getInstance();

		// if date is entered, parse it and modify the lastSurvey variable
		if (date != null && !date.isEmpty()) {
			// retrieve the month entered (0-based indexing)
			int month = Integer.parseInt(date.substring(0, date.indexOf('/'))) - 1;
			date = date.substring(date.indexOf('/') + 1);

			// retrieve the day entered
			int day = Integer.parseInt(date.substring(0, date.indexOf('/')));
			date = date.substring(date.indexOf('/') + 1);

			// retrieve the year entered
			int year = Integer.parseInt(date);
			// if the user enters just the last two numbers for the year, add the first two
			// back on
			if (year < 2000) {
				year += 2000;
			}
			// store the data collected
			lastSurvey.set(Calendar.MONTH, month);
			lastSurvey.set(Calendar.DAY_OF_MONTH, day);
			lastSurvey.set(Calendar.YEAR, year);
		}

		// create the student and store the survey data
		Student s = new Student(name, lastSurvey);

		// add the student to the arraylist
		students.add(s);
	}

	// this is just faster if I need to print something out
	public static void println(Object obj) {
		System.out.println(obj);
	}

}
