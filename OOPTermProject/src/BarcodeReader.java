import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.*;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;


public class BarcodeReader {
	
	private ArrayList<ArrayList<String>> infos = new ArrayList<ArrayList<String>>();
	private ArrayList<String> minProduct;
	
	// GUI OBjects
	private JFrame frame = new JFrame("CheApp");
	private JLabel label_BarcodeNumber =  new JLabel();
	private JTextField textField_BarcodeNumber = new JTextField();
	private JButton button_Search = new JButton();
	private JButton button_Return = new JButton();
	
	private JButton button_add = new JButton();
	private JButton button_dontadd = new JButton();
	private JLabel label_product = new JLabel();
	private JLabel label_product1 =  new JLabel();
	private JLabel location_product = new JLabel();
	
	JLabel label_Separator = new JLabel();
	
	// Variables
	private String barcode_number_f_product;
	private String barcode_number_f_product_1;
	private int isFound = 0;
	public String barcode = "";
	

	// Constructor
	public BarcodeReader(User current_user) {
		
		// Webcam settings
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());

		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setFPSDisplayed(false);
		panel.setDisplayDebugInfo(false);
		panel.setImageSizeDisplayed(false);
		panel.setMirrored(false);
		
		// calls frame
		setFrameSettings(panel);
		
		// For webcam	
		Thread t1 = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	String barcode = null;
		    	
				while (isFound == 0) {
					barcode = getBarcode(webcam);
					setBarcode_number_f_product(barcode);
				}
				
				// infos->0 == brand, infos->1 == name, infos->2 == price
				System.out.println(barcode);
				infos.add(PriceTaker.a101(barcode));
				infos.add(PriceTaker.carrefour(barcode));
				infos.add(PriceTaker.hepsiburada(barcode));
				infos.add(PriceTaker.amazon(barcode));
				infos.add(PriceTaker.trendyol(barcode));
				
				getMinumum();
				
				System.out.println(minProduct);
				location_product.setText(minProduct.get(0));
				label_product.setText(minProduct.get(1) + " -> " + minProduct.get(2));
				label_product1.setText("Would you add it to basket?");
				button_add.setVisible(true); button_dontadd.setVisible(true); label_product.setVisible(true); label_product1.setVisible(true); location_product.setVisible(true);
				webcam.close();
				
		    }
		});  
		t1.start();
		
		// When user clicks exit icon, closes program
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        current_user.logOut();
		    }
		});
		
		// Go backs to the MainPanel
		button_Return.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				t1.interrupt();
				webcam.close();
				MainPanel MP = new MainPanel(current_user);
				frame.dispose();
			}
		});
		
		// Adds new product to user's file
		button_add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Taking user' information from user_class
				int index_f_email = current_user.getEmail().indexOf('.');	
				String file_name = ".\\users\\" + current_user.getEmail().substring(0, index_f_email) +".txt"; String new_content = ""; String line;
				
				try { // Opening File and file operations
					File file = new File(file_name);
					BufferedReader read = new BufferedReader(new FileReader(file));
					
					while((line = read.readLine()) != null) { // travels every line
						line = line + "\n";
						new_content = new_content + line;
					}
					// Adding new product to the user's file
					if(getBarcode_number_f_product() != null) {
						new_content = new_content + "\nProduct Name: " + minProduct.get(1) + "\nProduct No: " + getBarcode_number_f_product() + 
								"\nMarket Name: " + minProduct.get(0) + "\nProduct Price: " + minProduct.get(2) + "\nProduct Type: " + infos.get(0).get(3) + "\n\n";

					}
					else {
						new_content = new_content + "\nProduct Name: " + minProduct.get(1) + "\nProduct No: " + getBarcode_number_f_product_1() + 
								"\nMarket Name: " + minProduct.get(0) + "\nProduct Price: " + minProduct.get(2) + "\nProduct Type: " + infos.get(0).get(3) + "\n\n";

					}
					read.close(); // closing file
					
					FileWriter write_t_file = new FileWriter(file_name);
					PrintWriter printWriter = new PrintWriter(write_t_file);
					
					printWriter.printf(new_content); // adding new content to the file
					// I'LL call product class and add to user's basket
					write_t_file.close(); // closing file					
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				button_add.setVisible(false); button_dontadd.setVisible(false); label_product.setVisible(false); label_product1.setVisible(false);
				frame.dispose();
				BarcodeReader BR = new BarcodeReader(current_user);
			}
		});
		
		// Do not add the product to the file and opens BarcodeReader again
		button_dontadd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button_add.setVisible(false); button_dontadd.setVisible(false); label_product.setVisible(false); label_product1.setVisible(false);
				frame.dispose();
				BarcodeReader BR = new BarcodeReader(current_user);
			}
		});
		
		// Search the barcode
		button_Search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(textField_BarcodeNumber.getText() != null) {
					webcam.close();
					String current_barcode = textField_BarcodeNumber.getText();
					
					System.out.println(current_barcode);
					infos.add(PriceTaker.a101(current_barcode));
					infos.add(PriceTaker.carrefour(current_barcode));
					infos.add(PriceTaker.hepsiburada(current_barcode));
					infos.add(PriceTaker.amazon(current_barcode));
					infos.add(PriceTaker.trendyol(current_barcode));
					
					System.out.println("Current: " + current_barcode);
					setBarcode_number_f_product_1(current_barcode);
					
					getMinumum();
					
					System.out.println(minProduct);
					location_product.setText(minProduct.get(0));
					label_product.setText(minProduct.get(1) + " -> " + minProduct.get(2));
					label_product1.setText("Would you add it to basket?");
					button_add.setVisible(true); button_dontadd.setVisible(true); label_product.setVisible(true); label_product1.setVisible(true); location_product.setVisible(true);
				}
			}
		});
		
	} // end of constructor
	
	// getBarcode method
	public String getBarcode(Webcam webcam) {
		try {
			if (webcam.isOpen()) {
				BufferedImage bf = null;
				
				if ((bf = webcam.getImage()) == null) {
					// resim alýnmadýysa.
				}
				else {
					BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bf)));
					Result result = new MultiFormatReader().decode(bitmap);
					barcode = result.getText();
					// Bu barkodu kullanarak fiyatlar bulunup en ucuz fiyat ve market texte yazýlacak.
					isFound = 1;
					return barcode;
				}
			}

			
		} 
		catch(Exception e) {
			System.out.println("Error while reading barcode " + e.getMessage());
		}
		return null;
	}
	
	// For choosing to min price of products
	public void getMinumum() {
		
		float minPrice = 2000000000;
		for (ArrayList<String> price : infos) {
			if (Float.parseFloat(price.get(2)) < minPrice) {
				minPrice = Float.parseFloat(price.get(2));
				minProduct = price;
			}
 			
		}
		if (minProduct.get(2) == "1000000000") { // Hiçbir maðazada bulunamazsa ürün
			ArrayList<String> temp = new ArrayList<String>();
			temp.add("Product cannot be found");
			temp.add("");
			temp.add("0");
			minProduct = temp; // minProduct geçersiz hale geliyor.
		}
	}
	
	// Frame settings
	public void setFrameSettings(WebcamPanel panel) {
	
		Image icon_IMG = Toolkit.getDefaultToolkit().getImage(".\\resources\\Logo.jpeg"); // saves icon
		
		
		// Calls gradient
		Gradient gradient_panel = new Gradient(); // creates(calls) gradient panel
	
		// Frame settings
		frame.setTitle("cheAPP");
		frame.setIconImage(icon_IMG); // changes icon
		frame.setSize(335, 525);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // exit when press to close button
		frame.setResizable(false); // closes size icon
		frame.setLocationRelativeTo(null); // opens the panel middle of the screen
		frame.getContentPane().setLayout(new GridLayout(0, 1));
		
		// Calls other GUI settings
		setLabelSettings();
		setButtonSettings();
		setTextFieldSettings();
		
		frame.add(gradient_panel); // adding gradient to the frame
		frame.add(panel);  // adding webcam to the frame
		
		// Adding GUI items to the gradient_panel
		gradient_panel.setLayout(null); // important!
		
		gradient_panel.add(label_BarcodeNumber);
		gradient_panel.add(textField_BarcodeNumber);
		gradient_panel.add(button_Return); gradient_panel.add(button_Search);
		gradient_panel.add(label_Separator);
		gradient_panel.add(button_add); gradient_panel.add(button_dontadd); gradient_panel.add(label_product); gradient_panel.add(label_product1);
		gradient_panel.add(location_product);
	
		
		button_add.setVisible(false); button_dontadd.setVisible(false); label_product.setVisible(false); label_product.setVisible(false); ; location_product.setVisible(false);
		
		
		frame.setVisible(true);

	}
	
	// Button settings
	public void setButtonSettings() {
		Icon icon_Return = new ImageIcon(".\\resources\\returnIcon.png"); // return icon
		
		// Return button
		button_Return.setIcon(icon_Return);
		button_Return.setFont(new Font(Font.DIALOG, Font.PLAIN, 9));
		button_Return.setHorizontalTextPosition(SwingConstants.CENTER);
		button_Return.setBounds(8, 8, 30, 30);
		button_Return.setBackground(new Color(134,151,129));
		button_Return.setOpaque(false);
		button_Return.setContentAreaFilled(false);
		button_Return.setBorderPainted(false);
		
		// Search button
		button_Search.setText("Search");
		button_Search.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
		button_Search.setHorizontalAlignment(SwingConstants.CENTER);
		button_Search.setBounds(240, 78, 70, 25);
		button_Search.setBackground(new Color(134, 151, 129));
		
		//add button
		button_add.setText("Add to Basket");
		button_add.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
		button_add.setHorizontalAlignment(SwingConstants.CENTER);
		button_add.setBounds(4, 200, 150, 25);
		button_add.setBackground(new Color(134, 151, 129));
		
		//don't add button
		button_dontadd.setText("Scan Again");
		button_dontadd.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
		button_dontadd.setHorizontalAlignment(SwingConstants.CENTER);
		button_dontadd.setBounds(165, 200, 150, 25);
		button_dontadd.setBackground(new Color(134, 151, 129));
	}
	
	// TextField settings
	public void setTextFieldSettings() {
		
		// Barcode number
		textField_BarcodeNumber.setText("Enter barcode number...");
		textField_BarcodeNumber.setFont(new Font(Font.DIALOG, Font.ITALIC, 11));
		textField_BarcodeNumber.setBounds(110, 48, 200, 25);
	}
	
	// Label settings
	public void setLabelSettings() {
		
		// Barcode number
		label_BarcodeNumber.setText("Enter barcode:");
		label_BarcodeNumber.setFont(new Font(Font.DIALOG, Font.PLAIN, 13));
		label_BarcodeNumber.setBounds(10, 50, 100, 20);
		label_BarcodeNumber.setForeground(Color.WHITE);
		
		//product name/market
		label_product.setFont(new Font(Font.DIALOG, Font.PLAIN, 13));
		label_product.setBounds(4, 150, 300, 40);
		label_product.setForeground(Color.WHITE);
		
		label_product1.setFont(new Font(Font.DIALOG, Font.PLAIN, 13));
		label_product1.setBounds(4, 165, 300, 40);
		label_product1.setForeground(Color.WHITE);
		
		location_product.setFont(new Font(Font.DIALOG, Font.PLAIN, 13));
		location_product.setBounds(4, 135, 300, 40);
		location_product.setForeground(Color.WHITE);
		
		
		//Separator
		label_Separator.setText(" - - - - - - - - - - - or you can scan - - - - - - - - - - ");
		label_Separator.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
		label_Separator.setBounds(7, 120, 300, 20);
		label_Separator.setForeground(new Color(134, 151, 129));
	}
	
	// Getter and Setter of barcode number
	public String getBarcode_number_f_product() {
		return this.barcode_number_f_product;
	}
	public void setBarcode_number_f_product(String barcode) {
		this.barcode_number_f_product = barcode;
	}

	public String getBarcode_number_f_product_1() {
		return barcode_number_f_product_1;
	}

	public void setBarcode_number_f_product_1(String barcode_number_f_product_1) {
		this.barcode_number_f_product_1 = barcode_number_f_product_1;
		System.out.println(this.barcode_number_f_product_1);
	}
	
} // end of BarcodeReader class

