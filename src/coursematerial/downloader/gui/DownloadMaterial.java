package coursematerial.downloader.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;

import coursera.coursematerial.downloader.DownloadExecutor;
import coursera.coursematerial.downloader.DownloadLinks;

public class DownloadMaterial {
	private String URL;
	private final String cookie;
	private ArrayList<String> titles, downloads, toDownload, downloadTitle;
	private final boolean DEBUG = false;

	private JLabel status;
	private JButton lectures;
	private JPanel downloadPanel;
	private JFrame frame;
	private JScrollPane tableScroller;
	private DownloadTable table;
	
	public DownloadMaterial(String URL, String cookie) {
		this.URL = URL;
		this.cookie = cookie;
	}
	
	public void showDownloaderGUI() {
		setUpButtons();
		setUpPanel();

		frame = new JFrame("Lecture List");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 500);
		frame.add(downloadPanel);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		addTableDisplay();
	}

	private void setUpButtons() {
		lectures = new JButton("Download Lectures");
		lectures.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	}

	private void setUpPanel() {
		downloadPanel = new JPanel();
		downloadPanel.setLayout(new BoxLayout(downloadPanel, BoxLayout.PAGE_AXIS));
		downloadPanel.add(lectures);
	}

	private void addTableDisplay() {
		table = new DownloadTable("Lectures");
		DownloadLinks links = new DownloadLinks(URL, cookie);
		titles = links.getLectureTitles();
		downloads = links.getLectures();
		
		Iterator<String> it = titles.iterator();
		while (it.hasNext())
			table.addRows(it.next());
		table.fireTableRowsInserted(table.getRowCount() - 1, table.getColumnCount() - 1);

		if (tableScroller != null) {
			downloadPanel.remove(tableScroller);
			frame.validate();
			frame.repaint();
		}

		tableScroller = new JScrollPane(new JTable(table));
		downloadPanel.add(tableScroller);

		frame.validate();
		frame.repaint();

		lectures.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				getSelectedDownloads();

				if (toDownload.isEmpty())
					addStatus("Select atleast one lecture", -1);
				else {
					final File file = getDownloadPath();
					if (file != null) {
						SwingWorker<Integer, Void> download = new SwingWorker<Integer, Void>() {

							@Override
							protected Integer doInBackground() {
								return new DownloadExecutor().executeDownload(toDownload, downloadTitle, convertFilePath(file.toString()), ".mp4", cookie);
							}

							@Override
							protected void done() {
								try {
									lectures.setEnabled(true);
									if (get() == 0)
										addStatus("Download Complete", 0);
									else
										addStatus("Network Error", -1);
								} catch (InterruptedException | ExecutionException e) {
									e.printStackTrace();
								}
							}
						};
						download.execute();
						lectures.setEnabled(false);
						addStatus("Download in progress", 1);
					}
				}
			}

		});
	}

	private void getSelectedDownloads() {
		toDownload = new ArrayList<String>(); downloadTitle = new ArrayList<String>();
		int p = 0;
		for (int i = 0, l = table.getRowCount(); i < l; i++) {
			if ((boolean) table.isSelected(i)) {
				toDownload.add(downloads.get(i));
				downloadTitle.add(titles.get(i));

				if (DEBUG)
					System.out.println(toDownload.get(p) + " " + downloadTitle.get(p));

				p++;
			}
		}
	}

	@SuppressWarnings("finally")
	private File getDownloadPath() {
		/*
		 * Code for using JFileChooser for selecting save download path taken from
		 * http://stackoverflow.com/questions/10524376/how-to-make-jfilechooser-default-to-computer-view-instead-of-my-documents
		 * from Hovercraft Full Of Eels's answer
		 */
		File file = null;
		try {
			FileSystemView view = FileSystemView.getFileSystemView();
			file = view.getDefaultDirectory();
			while (file != null) {
				file = file.getParentFile();
				if (file != null && "Computer".equalsIgnoreCase(file.toString())) {
					JFileChooser fileChooser = new JFileChooser(file);
			        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			        int result = fileChooser.showOpenDialog(null);
			        if (result == JFileChooser.APPROVE_OPTION) {
			        	file = fileChooser.getSelectedFile();
			        	break;
			        }
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			return file;
		}
	}

	private String convertFilePath(String file) {
		file = file.replaceAll("[\\\\]", "\\\\\\\\");
		if (file.charAt(file.length() - 1) != '\\')
			file.concat("\\\\\\\\");
		return file;
	}

	private void addStatus(String text, int type) {
		if (status != null) {
			downloadPanel.remove(status);
			frame.revalidate();
			frame.repaint();
		}

		status = new JLabel(text);
		status.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		if (type == 0)
			status.setForeground(Color.GREEN);
		else if (type == 1)
			status.setForeground(Color.BLUE);
		else
			status.setForeground(Color.RED);

		downloadPanel.add(status);
		frame.revalidate();
		frame.repaint();
	}
}