package coursematerial.downloader.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import coursera.coursematerial.downloader.SignIn;

@SuppressWarnings("serial")
public class StartDialog extends JFrame {
	private JTextField emailField;
	private JPasswordField passwordField;
	private JLabel headerLabel, emailLabel, passwordLabel, incorrect;
	private JButton button;
	private JPanel loginPanel;
	
	private String email, password, cookie;
	private SignIn login;
	
	public void createStartDialog() {
		setTitle("Coursera Course Material Downloader");
		setSize(300, 250);
		
		setUpLoginFields();
		setUpPanel();
		
		add(loginPanel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	private void setUpLoginFields() {
		headerLabel = new JLabel("Coursera Login");
		headerLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		headerLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		
		emailField = new JTextField();
		emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		
		emailLabel = new JLabel("Enter email:");
		emailLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		emailLabel.setLabelFor(emailLabel);
		
		passwordField = new JPasswordField(20);
		passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		
		passwordLabel = new JLabel("Enter password:");
		passwordLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		passwordLabel.setLabelFor(passwordField);
		
		button = new JButton("Sign in");
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				email = emailField.getText();
				password = String.copyValueOf(passwordField.getPassword());
				emailField.setText("");
				passwordField.setText("");
				
				SwingWorker<Integer, Void> signIn = new SwingWorker<Integer, Void>() {

					@Override
					protected Integer doInBackground() throws Exception {
						login = new SignIn(email, password);
						return login.validator();
					}
					
					@Override
					protected void done() {
						try {
							cookie = login.getCookie();
							loginResult(get());
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					}
					
				};
				signIn.execute();
			}
		});
	}
	
	private void setUpPanel() {
		loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		loginPanel.add(headerLabel);
		loginPanel.add(emailLabel);
		loginPanel.add(emailField);
		loginPanel.add(passwordLabel);
		loginPanel.add(passwordField);
		loginPanel.add(button);
	}
	
	private void loginResult(int status) {
		clearPreviousAttempts();
		
		if (status == 0) {
			setVisible(false);
			Thread nextWindow = new Thread(new Runnable() {

				@Override
				public void run() {
					new ActiveCourses(cookie).listActiveCourses();
				}
				
			});
			nextWindow.start();
			
		}
		else {
			if (status == 1) {
				incorrect = new JLabel("Incorrect Login");
				incorrect.setFont(new Font("Courier", Font.BOLD, 14));
				incorrect.setForeground(Color.RED);
				incorrect.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
			}
			else {
				incorrect = new JLabel("Network Failure");
				incorrect.setFont(new Font("Courier", Font.BOLD, 14));
				incorrect.setForeground(Color.RED);
				incorrect.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
			}
			
			loginPanel.add(incorrect);
			validate();
			repaint();
		}
	}
	
	private void clearPreviousAttempts() {
		if (incorrect != null) {
			loginPanel.remove(incorrect);
			validate();
			repaint();
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				StartDialog start = new StartDialog();
				start.createStartDialog();
				start.setVisible(true);
			}
			
		});
	}
}
