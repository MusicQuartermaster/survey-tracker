package main;

public class Driver {

	public static void main(String[] args) {
		// starts the program
		Main console = new Main();

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Window().createAndShowGUI(console);
			}
		});
	}
}
