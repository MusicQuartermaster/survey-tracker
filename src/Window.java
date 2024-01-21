package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Window implements ActionListener, ListSelectionListener {

	// this is the GUI (graphical user interface) for the program. It creates the
	// window and panel that allows users to interact with the data in an intuitive
	// way

	private Main console;

	// the height and width of the window in pixels
	private final int HEIGHT = 500;
	private final int WIDTH = 800;

	// the buttons that appear at the top of the window that allows for user
	// interaction
	private JButton giveSurvey;
	private JButton remove;
	private JButton getLastSurvey;
	private JButton setLastSurvey;

	// this is the list of names that takes up 90% of the window
	public JList<String> studentList;

	// the start of the window creation
	public void createAndShowGUI(Main console) {
		this.console = console;

		// Create and set up the window.
		JFrame frame = new JFrame("Student Survey Tracker");

		// the program will terminate when you close the window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set the size of the window
		frame.setPreferredSize(new Dimension(WIDTH + 30, HEIGHT));
		frame.setMaximumSize(new Dimension(WIDTH + 30, HEIGHT));

		// creates the menu bar with a gray background
		JMenuBar menuBar = new JMenuBar();
		menuBar.setOpaque(true);
		menuBar.setBackground(new Color(100, 100, 100));
		menuBar.setPreferredSize(new Dimension(WIDTH, 25));

		// add the menu bar to the actual window
		frame.setJMenuBar(menuBar);

		// retrieve the content pane of the window so we can add stuff to it
		Container contentPane = frame.getContentPane();

		// set the window color to dark gray
		contentPane.setBackground(new Color(50, 50, 50));

		/*
		 * set window to middle of screen
		 */

		// get information about the computer monitor the program is running on
		GraphicsConfiguration config = frame.getGraphicsConfiguration();

		// set the bounds of the window so that it is centered on the window regardless
		// of the window size
		Rectangle bounds = config.getBounds();
		int x = (bounds.width / 2 - WIDTH / 2);
		int y = (bounds.height / 2 - HEIGHT / 2);
		frame.setLocation(x, y);

		// retrive the list of student names and check which ones have a survey due
		String[] names = new String[console.students.size()];
		for (int i = 0; i < names.length; i++) {
			Student s = console.students.get(i);
			names[i] = s.getName();
			if (s.update()) {
				names[i] += " - Survey Due";
			}
		}

		// add the list of names to the window pane
		studentList = new JList<String>(names);
		// allows the user to select names in the list
		studentList.addListSelectionListener(this);
		// make the names nice and easy to read
		studentList.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		studentList.setMinimumSize(new Dimension(WIDTH - 100, HEIGHT / 2));
		updateList();
		// allows the user to scroll if there are more names than can be shown on a
		// single screen
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(studentList);

		// set the colors so that they match the theme of the other components
		scrollPane.setBackground(new Color(50, 50, 50));
		scrollPane.setForeground(new Color(150, 150, 150));
		studentList.setLayoutOrientation(JList.VERTICAL);
		studentList.setAlignmentY(JList.LEFT_ALIGNMENT);
		studentList.setBackground(new Color(50, 50, 50));
		studentList.setForeground(new Color(150, 150, 150));
		// add the ability to scroll to the window itself
		contentPane.add(scrollPane);

		/*
		 * create the buttons that will appear at the top of the screen
		 */
		JButton save = new JButton("Save");
		save.addActionListener(this);

		JButton add = new JButton("Add");
		add.addActionListener(this);

		remove = new JButton("Remove");
		remove.addActionListener(this);
		remove.setEnabled(false);

		giveSurvey = new JButton("Give Survey");
		giveSurvey.addActionListener(this);
		giveSurvey.setEnabled(false);

		setLastSurvey = new JButton("Set Last Survey");
		setLastSurvey.addActionListener(this);
		setLastSurvey.setEnabled(false);

		getLastSurvey = new JButton("View Last Survey");
		getLastSurvey.addActionListener(this);
		getLastSurvey.setEnabled(false);

		// add the buttons to the menu at the top
		menuBar.add(save);
		menuBar.add(add);
		menuBar.add(remove);
		menuBar.add(giveSurvey);
		menuBar.add(getLastSurvey);
		menuBar.add(setLastSurvey);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	// performs specific actions when something has been detected
	public void actionPerformed(ActionEvent e) {
		int index = -1;
		// determines which button has been pressed
		switch (e.getActionCommand()) {
		// saves the data to a file
		case "Save":
			console.save();
			break;
		// adds a student to the save data
		case "Add":
			console.add();
			updateList();
			break;
		// sets the most recent survey date to today
		case "Give Survey":
			index = studentList.getSelectedIndex();
			console.students.get(index).giveSurvey();
			updateList();
			studentList.setSelectedIndex(index);
			break;
		// removes a student from the save data
		case "Remove":
			index = studentList.getSelectedIndex();
			console.students.remove(index);
			updateList();
			remove.setEnabled(false);
			giveSurvey.setEnabled(false);
			getLastSurvey.setEnabled(false);
			setLastSurvey.setEnabled(false);
			break;
		// displays a small menu with the last survey date on it
		case "View Last Survey":
			index = studentList.getSelectedIndex();
			Calendar viewLast = console.students.get(index).getLastSurvey();
			JOptionPane.showMessageDialog(null,
					"Last survey was given on " + (viewLast.get(Calendar.MONTH) + 1) + "/"
							+ viewLast.get(Calendar.DAY_OF_MONTH) + "/" + viewLast.get(Calendar.YEAR),
					"View Last Survey Date", JOptionPane.INFORMATION_MESSAGE);
			break;
		// displays a small menu that allows the user to change the last survey date
		case "Set Last Survey":
			index = studentList.getSelectedIndex();
			Student s = console.students.get(index);
			String date = JOptionPane.showInputDialog(null, "Enter the last surveyed date (M/D/Y).",
					"Set Last Survey Date", JOptionPane.QUESTION_MESSAGE);

			if (date != null && !date.isEmpty()) {
				Calendar lastSurvey = Calendar.getInstance();
				int month = Integer.parseInt(date.substring(0, date.indexOf('/'))) - 1;
				date = date.substring(date.indexOf('/') + 1);

				int day = Integer.parseInt(date.substring(0, date.indexOf('/')));
				date = date.substring(date.indexOf('/') + 1);

				int year = Integer.parseInt(date);
				if (year < 2000) {
					year += 2000;
				}
				lastSurvey.set(Calendar.MONTH, month);
				lastSurvey.set(Calendar.DAY_OF_MONTH, day);
				lastSurvey.set(Calendar.YEAR, year);
				s.setLastSurvey(lastSurvey);
			}
			updateList();
			studentList.setSelectedIndex(index);
		}
	}

	// determines which students have a survey due
	public void updateList() {
		String[] names = new String[console.students.size()];
		for (int i = 0; i < names.length; i++) {
			Student s = console.students.get(i);
			names[i] = s.getName();
			if (s.update()) {
				names[i] += " - Survey Due";
			}
		}
		studentList.setListData(names);
	}

	@Override
	// performs an action when a name is selected from the list of names
	public void valueChanged(ListSelectionEvent e) {
		giveSurvey.setEnabled(true);
		remove.setEnabled(true);
		setLastSurvey.setEnabled(true);
		getLastSurvey.setEnabled(true);
	}
}
