package coursematerial.downloader.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import coursera.coursematerial.downloader.CourseNames;

public class ActiveCourses implements ItemListener {
	private Map<String, String> courseDetails;
	private String cookie;
	private boolean error = false;
	
	private JFrame frame;
	private JPanel panel;
	private JLabel label;
	private JCheckBox selected[];
	private JButton retry;
	
	public ActiveCourses(String cookie) {
		frame = new JFrame("Currently active courses");
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		this.cookie = cookie;
	}
	
	public void listActiveCourses() {
		label = new JLabel("Fetching courses...");
		label.setForeground(Color.BLUE);
		panel.add(label);
		
		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		frame.setVisible(true);
		
		displayCourses();
	}
	
	private void displayCourses() {
		if (retry != null)
			retry.setVisible(false);
		getCourseDetails();
		
		if (!error) {
			label.setText("Select a course");
			label.setForeground(Color.BLUE);
			addCourses();
		}
		else {
			label.setText("Network Error");
			label.setForeground(Color.RED);
			if (retry == null) {
				retry = new JButton("Retry");
				panel.add(retry);
				frame.revalidate();
				frame.repaint();
				
				retry.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						error = false;
						displayCourses();
					}
				});
			}
			if (!retry.isVisible())
				retry.setVisible(true);
		}
	}
	
	private void getCourseDetails() {
		try {
			courseDetails = new CourseNames().getCourses(cookie);
		} catch(Exception e) {
			e.printStackTrace();
			error = true;
		}
	}
	
	private void addCourses() {
		ArrayList<String> courses = new ArrayList<String>();
		Iterator<String> it = courseDetails.keySet().iterator();
		while (it.hasNext())
			courses.add(it.next());
		int l = courses.size();
		selected = new JCheckBox[l];
		
		for (int i = 0; i < l; i++) {
			selected[i] = new JCheckBox(courses.get(i));
			selected[i].addItemListener(this);
			panel.add(selected[i]);
		}
		
		frame.revalidate();
		frame.repaint();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		for (int i = 0, l = selected.length; i < l; i++) {
			if (selected[i] == e.getItemSelectable() && (e.getStateChange() == ItemEvent.SELECTED)) {
				selected[i].setSelected(false);
				frame.setVisible(false);
				final String URL = courseDetails.get(selected[i].getText()).concat("/lecture");
				
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						new DownloadMaterial(URL, cookie).showDownloaderGUI();
					}
				});
			}
		}
		frame.setVisible(true);
	}
}