import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.File; // for file operations
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

// Constructer
public class LogInSignUp {
	
	// Defining GIU Object 
	private JFrame frame = new JFrame(); // frame
	
	private JLabel label_Email = new JLabel("Email: ", JLabel.LEFT);
	private JLabel label_Password = new JLabel("Password: ", JLabel.LEFT);
	private JLabel label_Information1 = new JLabel("", JLabel.LEFT);
	private JLabel label_Information2 = new JLabel("", JLabel.LEFT);
	private JTextField textField_Email = new JTextField("Enter your e-mail address");
	private JTextField textField_RoboKod = new JTextField("");
	private JPasswordField passwordField_Password = new JPasswordField("****");
	private JCheckBox checkBox_Information = new JCheckBox();
	private JButton button_Password = new JButton();
	private JButton button_Next = new JButton();
	private JButton button_Change = new JButton();
	private JButton button_Log_Sign = new JButton();
	private JButton button_IForgotMyPassword =  new JButton();
	private JButton button_Approve = new JButton("Approve");
	JLabel label_Logo = new JLabel(new ImageIcon(".\\resources\\smallLogo.png"));
	
	// The icons i've used
	Icon icon_OpenEye = new ImageIcon(".\\resources\\OpenEye.jpeg");
	Icon icon_CloseEye = new ImageIcon(".\\\\resources\\\\CloseEye.jpeg");
	
	// Defining variables
	public String hold_RandomValue;
	private int password_Count = 0;
	private int checkBox_Count = 0;
	public boolean is_information_click = false; 
	public boolean has_emailSent = false;
	private User new_user;
	
	public LogInSignUp() { 
		
		// Main GIU settings
		setFrameSettings();
		
		// makes password visible and unvisible
		button_Password.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(password_Count % 2 == 0) {
					button_Password.setIcon(icon_OpenEye); // changes icon
					passwordField_Password.setEchoChar((char)0); // shows input
				}
				else {
					button_Password.setIcon(icon_CloseEye); // changes icon
					passwordField_Password.setEchoChar('*'); // hides input
				}
				password_Count++;
			}
		});

		// Listener of Information, checks the information has been clicked or not
		checkBox_Information.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkBox_Count % 2 == 0) {
					is_information_click = true;
				}
				else {
					is_information_click = false;
				}
				checkBox_Count++;
			}
		});
		
		// When haes been clicked on the Next button
		button_Next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// First i'll check that are there any problem about password and e-mail
				if(String.valueOf(passwordField_Password.getPassword()).length() != 0 && is_information_click == true 
						&& isEmailOnCorrectForm() == true ) {
					
					String line; String split_line[] = new String[2];
					int index_f_email = textField_Email.getText().indexOf('.');
					String file_name = ".\\users\\" + textField_Email.getText().substring(0, index_f_email) +".txt";
					
					if(isUserExists()) { // there is a user with that name
						
						try { // takes user's password from the file
							
							File file = new File(file_name);
							BufferedReader read = new BufferedReader(new FileReader(file));
							
							while((line = read.readLine()) != null) { // reads file line by line
								if(line.contains("Password: ")) { // if line has include Password command split the password and break it
									split_line = line.split(" ");
									break; // we do not need to scan file anymore
								}			
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						
						if(String.valueOf(passwordField_Password.getPassword()).equals(split_line[1])) { // if password are equalt to each other return true
							
							// Go to the MainPanel
							frame.dispose();
							AddUserToUserClass();
							MainPanel mp = new MainPanel(new_user);
						}
						else {
							JOptionPane.showMessageDialog(frame, "Wrong password!"); // shows Wrong password! text and clear textFields
							
							clearTextFields();
						}
						
					}
					else { // when user does not exist, else condition will work. In that condition, we will create new user, user file, sends email to user's email
					
						// Lets close next button, lets open objects which are user needs
						button_Next.setVisible(false);
						button_Log_Sign.setVisible(true);
						textField_RoboKod.setVisible(true);
						
						// create new roboCode
						hold_RandomValue = createRoboCode();
						
						// Sends email
						sendEmail();
						JOptionPane.showMessageDialog(frame, "We have send RoboCode to your e-mail address!");
					}
				}
				else { // when password and email do not in good condition
					JOptionPane.showMessageDialog(frame, "Wrong e-mail or password!");
					clearTextFields();
				}
			}
		});
		
		// Log in Sign Up button
		button_Log_Sign.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index_f_email = textField_Email.getText().indexOf('.');
				String file_name = ".\\users\\" + textField_Email.getText().substring(0, index_f_email) +".txt";
				
				// TODO Auto-generated method stub
				if(hold_RandomValue.equals(textField_RoboKod.getText())) {
					// Save informations about user to the textFile
					createAccount(file_name);
					
					// go to the other panel
					JOptionPane.showMessageDialog(frame, "You've signed up succesfully!");
					AddUserToUserClass();
					AddUserToUserClass();
					MainPanel mp = new MainPanel(new_user);
					frame.dispose();
				}
				else {
					JOptionPane.showMessageDialog(frame, "Wrong robo code!"); 
				}
			}
			
		});
		
		// Hover of IForgotMyPassword
		button_IForgotMyPassword.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	button_IForgotMyPassword.setForeground(new Color(209, 51, 215));
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	button_IForgotMyPassword.setForeground(new Color(142, 7, 146));
		    }
		});
		
		// Listener of IForgotMyPassword, activates when clicked to forgot password?
		button_IForgotMyPassword.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Updates passwordField_Password settings
				passwordField_Password.setText("Enter your new password...");
				passwordField_Password.setEchoChar((char)0); // shows input
				password_Count++;
				
				// Closes extra things
				button_IForgotMyPassword.setVisible(false);
				button_Next.setVisible(false);
				
				// Opens
				button_Change.setVisible(true);
			}	
		});
		
		// Change button
		button_Change.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// if user exists, changes password
				
				if(String.valueOf(passwordField_Password.getPassword()).length() != 0) {
					if(isUserExists()) {
						
						// Opens robocode
						hold_RandomValue = createRoboCode();
						textField_RoboKod.setVisible(true);
						button_Change.setVisible(false);
						button_Approve.setVisible(true);
						sendEmail();
						JOptionPane.showMessageDialog(frame, "We have send RoboCode to your e-mail address!");
						
					}
					else { // there is no e-mail like that
						JOptionPane.showMessageDialog(frame, "This e-mail has not been found!"); 
					}
				}
				else { // wrong condition of password
					JOptionPane.showMessageDialog(frame, "Wrong password!"); 
				}	
			}
		});
			
		button_Approve.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
				// TODO Auto-generated method stub
				if(hold_RandomValue.equals(textField_RoboKod.getText())) {
					String new_content = "";
					String line;
					int index_f_email = textField_Email.getText().indexOf('.');	
					String file_name = ".\\users\\" + textField_Email.getText().substring(0, index_f_email) +".txt";
					
					// Save information about user to textFile
					try {
						File file = new File(file_name);
						BufferedReader read = new BufferedReader(new FileReader(file));
						
						while((line = read.readLine()) != null) { // reads file line by line
							if(line.contains("Password: ")) { // if line has include Password command split the password and break it
								line = "Password: " + String.valueOf(passwordField_Password.getPassword());
							}	
							line = line + "\n";
							new_content = new_content + line;
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					// update the user's file
					try {
						System.out.println(new_content);
						FileWriter write_t_file = new FileWriter(file_name);
						PrintWriter printWriter = new PrintWriter(write_t_file);
						
						printWriter.printf(new_content);
						
						write_t_file.close();
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	
					// go to the other panel
					AddUserToUserClass();
					JOptionPane.showMessageDialog(frame, "Password has been changed succesfully!"); 
					MainPanel mp = new MainPanel(new_user);
					frame.dispose();
				}
				else {
					JOptionPane.showMessageDialog(frame, "Wrong Robo Code"); 
				}	
			}		
		});
		
	} // end of constructor
		
	// Main GIU settings
	private void setFrameSettings() {
		
		Image icon_IMG = Toolkit.getDefaultToolkit().getImage(".\\\\resources\\\\Logo.jpeg"); // saves icon
		Gradient panel = new Gradient(); // creates(calls) gradient panel
		
		// Frame settings
		frame.setTitle("cheAPP");
		frame.setIconImage(icon_IMG); // changes icon
		frame.setSize(335, 525);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit when press to close button
		frame.setResizable(false); // closes size icon
		frame.setLocationRelativeTo(null); // opens the panel middle of the screen
		frame.getContentPane().setLayout(new GridLayout(0, 1));
		
		
		// Calls other settings
		setLabelSettings();
		setTextFieldSettings();
		setButtonSettings();
		setCheckBoxSettings();
		
		frame.add(panel); // adds gradient panel to frame
		
		
		panel.setLayout(null); // important!
		panel.add(label_Logo);panel.add(label_Email); panel.add(label_Password); panel.add(label_Information1); panel.add(label_Information2);
		panel.add(textField_Email); panel.add(passwordField_Password); panel.add(textField_RoboKod);
		panel.add(button_Approve); panel.add(checkBox_Information);
		panel.add(button_IForgotMyPassword); panel.add(button_Password); panel.add(button_Next); panel.add(button_Log_Sign); panel.add(button_Change);
		
		
		frame.setVisible(true); // makes frame can be seen
	}
	
	// All label settings
	private void setLabelSettings() {
		
		//brand 
		label_Logo.setLocation(10, 40);
		label_Logo.setSize(300,85);
		
		// ID
		label_Email.setText("Email: ");
		label_Email.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
		label_Email.setBounds(25, 135, 100, 50); // x coordinate, y coordinate, width, height
		label_Email.setForeground(Color.WHITE);
		
		// Password
		label_Password.setText("Password: ");
		label_Password.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
		label_Password.setBounds(25, 190, 100, 45); // x coordinate, y coordinate, width, height
		label_Password.setForeground(Color.WHITE);
		
		// Information1 and Information2
		label_Information1.setText("I accept the proccessing of my personel data");
		label_Information1.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
		label_Information1.setBounds(25, 275, 300, 25); // x coordinate, y coordinate, width, height
		label_Information1.setForeground(Color.WHITE);
		
		label_Information2.setText("for commercial purposes. ");
		label_Information2.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
		label_Information2.setBounds(25, 295, 300, 20); // x coordinate, y coordinate, width, height
		label_Information2.setForeground(Color.WHITE);
	}
	
	// All text field settings
	private void setTextFieldSettings() {
		Border border = BorderFactory.createLineBorder(new Color(198,23,157));
		// ID
		textField_Email.setFont(new Font(Font.DIALOG, Font.ITALIC, 13));
		textField_Email.setBounds(25, 175, 270, 25);
		textField_Email.setBorder(border);
		textField_Email.setOpaque(false); // change background color to transparent
		textField_Email.setForeground(Color.WHITE); // changes color to black
		
		// Password
		passwordField_Password.setFont(new Font(Font.DIALOG, Font.ITALIC, 13));
		passwordField_Password.setBounds(25, 230, 240, 25);
		passwordField_Password.setBorder(border);
		passwordField_Password.setOpaque(false); // change background color to transparent
		passwordField_Password.setForeground(Color.WHITE);
		passwordField_Password.setToolTipText("Enter more than five character");
		
		// RoboKod
		textField_RoboKod.setFont(new Font(Font.DIALOG, Font.PLAIN, 13));
		textField_RoboKod.setBorder(border);
		textField_RoboKod.setBounds(180, 320, 115, 25);
		textField_RoboKod.setOpaque(false);
		textField_RoboKod.setVisible(false);

	}
	
	// All button Settings
	private void setButtonSettings() {
		
		// Password
		button_Password.setIcon(icon_CloseEye);
		button_Password.setBounds(270, 230, 25, 25);
		
		// Next
		button_Next.setText("Next");
		button_Next.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
		button_Next.setHorizontalTextPosition(SwingConstants.CENTER);
		button_Next.setBounds(225, 320, 70, 30);
		button_Next.setBackground(new Color(134,151,129));
		
		// Change
		button_Change.setText("Change");
		button_Change.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
		button_Change.setHorizontalTextPosition(SwingConstants.CENTER);
		button_Change.setBounds(110, 385, 100, 35);
		button_Change.setBackground(new Color(134,151,129));
		button_Change.setVisible(false);
		
		// Log in Sign up 
		button_Log_Sign.setText("Sign up");
		button_Log_Sign.setHorizontalAlignment(SwingConstants.CENTER);
		button_Log_Sign.setFont(new Font(Font.DIALOG , Font.PLAIN, 11));
		button_Log_Sign.setBounds(110, 385, 190, 35); // i'll change width 
		button_Log_Sign.setVisible(false);
		button_Log_Sign.setBackground(new Color(134,151,129));
		
		// I forgot my password
		button_IForgotMyPassword.setText("Forgot password?");
		button_IForgotMyPassword.setFont(new Font(Font.DIALOG, Font.ITALIC, 11));
		button_IForgotMyPassword.setBounds(175, 255, 150, 25);
		button_IForgotMyPassword.setForeground(new Color(142, 7, 146));
		
		// makes button transparent
		button_IForgotMyPassword.setOpaque(false);
		button_IForgotMyPassword.setContentAreaFilled(false);
		button_IForgotMyPassword.setBorderPainted(false);
		
		// Approve
		button_Approve.setText("Approve password");
		button_Approve.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
		button_Approve.setHorizontalTextPosition(SwingConstants.CENTER);
		button_Approve.setBounds(80, 385, 140, 35);
		button_Approve.setBackground(new Color(134,151,129));
		button_Approve.setVisible(false);
	}
	
	// All checkbox settings
	private void setCheckBoxSettings() {
		
		// Information
		checkBox_Information.setBounds(275, 277, 25, 25);
		checkBox_Information.setOpaque(false);
	}
	
	// 	Creates roboCode for mail
	public String createRoboCode() {
		Random random = new Random();
		String roboCode = "";
		int random_number;
		for(int i=0; i<6; i++) {
			random_number = random.nextInt(2);
			
			if(random_number == 0) {
				roboCode = roboCode + String.valueOf(random.nextInt(10));
			}
			else {
				roboCode = roboCode + (char)(random.nextInt(26) + 'a');
			}
		}
		return roboCode;	
	}
	
	// Checks the email, is email on correct form
	public boolean isEmailOnCorrectForm() {
		if(textField_Email.getText().contains("@") && (textField_Email.getText().contains("hotmail") || textField_Email.getText().contains("gmail") || 
				textField_Email.getText().contains("outlook")) && textField_Email.getText().contains(".com")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	// Sends email
	public void sendEmail() {
		
		String to = textField_Email.getText();
		String from = "ooprojectproject@gmail.com";
		String host = "smtp.gmail.com";
			
		// HOST SETTINGS
		Properties properties = System.getProperties(); 
		properties.put("mail.smtp.host", host);
	    properties.put("mail.smtp.port", "465");
	    properties.put("mail.smtp.ssl.enable", "true");
	    properties.put("mail.smtp.auth", "true");
	        
		// Opens main mail account
	    Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

	    	protected PasswordAuthentication getPasswordAuthentication() {

	    		return new PasswordAuthentication("ooprojectproject@gmail.com", "ooproject");

	        }

	     });

	        // Sending e-mail
	        try {
	        	String text = "Vertification code : " + this.hold_RandomValue;
	            MimeMessage message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(from));

	            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	            message.setSubject("CheApp");
	            message.setText(text);
	            
	            Transport.send(message);
	        } catch (MessagingException mex) {
	            mex.printStackTrace();
	            System.out.println("Message could not received to User");
	        }

	}

	// Checks is there a  user with that name
	private boolean isUserExists() {
		int index_f_email = 0;
		String file_name = "";
		
		// creating file name
		index_f_email = textField_Email.getText().indexOf('.');
		file_name = ".\\users\\" + textField_Email.getText().substring(0, index_f_email) +".txt";
		
		File new_file = new File(file_name);
		if(new_file.exists()) { // there is file, user exits
			return true; 
		}
		else {
			return false; // there is no file with this name, user does not exist
		}			
	}
	
	// Check is there user with that e-mail, create new file
	private void createAccount(String file_name) {

		// File operations
		File new_file = new File(file_name);
		if(new_file.exists() == false) { // there is no file with that name so create new textFile for user
			writeToFile(file_name);
		}	
	}
	
	// Writes to the file
	private void writeToFile(String file_name) {
		
		// Creating content
		String content = "E-mail: " + textField_Email.getText() + "\nPassword: " + String.valueOf(passwordField_Password.getPassword()) + "\n";
		try {
			FileWriter write_t_file = new FileWriter(file_name);
			PrintWriter printWriter = new PrintWriter(write_t_file);
			
			printWriter.printf(content);
			
			write_t_file.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Cleans textFields
	private void clearTextFields() {
		textField_Email.setText("");
		passwordField_Password.setText("");
	}
	
	private void AddUserToUserClass() {
		new_user = new User();
		new_user.setEmail(textField_Email.getText());
		new_user.setPassword(String.valueOf(passwordField_Password.getPassword()));
	}

}