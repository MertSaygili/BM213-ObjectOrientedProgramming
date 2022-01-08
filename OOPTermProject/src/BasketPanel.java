import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class BasketPanel {
	
	// Defining GUI Objects
	private Gradient panel = new Gradient(); // creates(calls) gradient panel
	private JFrame frame = new JFrame("CheApp");
	private JButton button_Return = new JButton();
	private JButton button_ClearAll = new JButton();
	private JButton button_CouponAdd = new JButton();
	private JLabel label_TotalPrice = new JLabel();
	private JLabel label_Logo = new JLabel(new ImageIcon(".\\resources\\minilogo.png"));
	private JTextField textField_Coupon = new JTextField();
	
	
	// Global variables and objects\
	private ArrayList<String> productInformations = new ArrayList<String>();
	private User current_user;
	private Basket basket = new Basket();
	private ArrayList<Product> products = new ArrayList<Product>();
	private JTable table;
	private DefaultTableModel model;
	
	
	// Constructor
	public BasketPanel(User current_user) {
			
		// Assigning of user class
		this.current_user = current_user;
		
		int product_count = productCount();
		
		// File variables
		int index_f_email = current_user.getEmail().indexOf(".");
		String file_name = ".\\users\\" + current_user.getEmail().substring(0, index_f_email) + ".txt";
		
		//table introductions
		Object data[][] = new Object[product_count][4];
		String column[] = { "Product", "Market", "Price", " " };
		
		
		// Calls table settings
		settableSettings(data,column);
				
		// Calls frame settings
		setFrameSettings();
		
		
		
		
		// When user clicks exit icon, closes program
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        current_user.logOut();
		    }
		});

		
		// if user clicks to the return button, MainPanel opens
		button_Return.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainPanel mp = new MainPanel(current_user);
				frame.dispose();
				
			}
			
		});
		
		// if user clicks to the remove all products on the user basket 
		button_ClearAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Calls clear function on basket class
				basket.clear(current_user);
				
				BasketPanel bp = new BasketPanel(current_user); // opens BasketPanel again
				frame.dispose();
				
			}
			
		});
		
		// Add Coupon
		button_CouponAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				float result = 0;
				String currentTotal_price = "";
				
				try {
					currentTotal_price = label_TotalPrice.getText().replace("Total price: ", "");
					currentTotal_price = currentTotal_price.replace("tl", "");
					System.out.println(currentTotal_price);
					
					result = basket.addCoupon(textField_Coupon.getText(), current_user, Float.valueOf(currentTotal_price.replace(',', '.')));
				}
				catch(NumberFormatException ex){
					System.out.println(ex);
				}
				
				if( result != 0) {
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(2);
					String s = nf.format(result);
					label_TotalPrice.setText("Total price: " + s + "tl");
					label_TotalPrice.setBounds(148,240, 150, 30);
					label_TotalPrice.setForeground(new Color(198,23,157));
					System.out.print("Total price: " + String.valueOf(result) + "tl");
					JOptionPane.showMessageDialog(frame, "The coupon has been applied successfully.");
				}
				else {
					JOptionPane.showMessageDialog(frame, "Unvalid Coupon");

				}	
			}
			
		});
		
		// Delete product
		table.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int row = table.rowAtPoint(evt.getPoint());
		        int col = table.columnAtPoint(evt.getPoint());
		        if (row >= 0 && col == 3) {
		        	
		        	if(data[row][3] == "x") {
			        	model.removeRow(row);
			        	basket.removeProduct((String) data[row][1], current_user);
			        	System.out.print((String) data[row][1]);
			        	
			        	BasketPanel BP = new BasketPanel(current_user);
			        	frame.dispose();
		        	}
		        }
		    }
		});
		frame.setVisible(true);
	} // end of constructor
	
	// Table settings
	private void settableSettings(Object[][] data , String[] column) {
		
		int product_count = productCount(); 
		
		// Keeps barkod no
		String barcodeNo = "";
		
		// File variables
		int index_f_email = this.current_user.getEmail().indexOf(".");
		String file_name = ".\\users\\" + this.current_user.getEmail().substring(0, index_f_email) + ".txt", line = "", information = "";
		ArrayList<String> informations = new ArrayList<String>();
		ArrayList<String> types = new ArrayList<String>();
		ArrayList<String> allBarcodeNo = new ArrayList<String>();
		String new_content = "E-mail: " + current_user.getEmail() + "\nPassword: " + current_user.getPassoword() + "\n";
		try { // Opening and reading file
			File file = new File(file_name);
			BufferedReader read = new BufferedReader(new FileReader(file_name));
			while((line = read.readLine()) != null) {
				
				
				if(line.contains("Product Name: ")) {
					
					information = information + line.replace("Product Name: ", "") + " ";
					productInformations.add(line.replace("Product Name: ", ""));
					//line.replace("\n", "");
				}
				else if(line.contains("Market Name: ")) {
					
					information = line.replace("Market Name: ", "") + " ";
					productInformations.add(line.replace("Market Name:", ""));
				}
				else if(line.contains("Product Price: ")) {
					information = information + line.replace("Product Price: ", "") + "tl";
					productInformations.add(line.replace("Product Price: ", ""));
				}
				else if(line.contains("Product No: ")) {
					barcodeNo = line.replace("Product No: ", "");
					allBarcodeNo.add(line.replace("Product No: ", ""));
				} // just go
				else if(line.contains("Product Type: ")) {
					productInformations.add(line.replace("Product Type: ", ""));
					types.add(line.replace("Product Type: ", ""));
				}
				else{
					if(information != "") {
						
						// Choose type and send it to basket;
						Product product = createProductByType(productInformations, barcodeNo);
						basket.addProduct(product); // Added product into basket
						informations.add(information);
						// System.out.println(productInformations); prints all informations about the product
						productInformations.clear();
					}
					information = ""; // clears information string
				}
			}
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Creates product labels which is taking products from basket.
		ArrayList<Product> products = basket.getProducts(); 
		int j = 0;
		
		for(int i=0; i<product_count; i++) {
			if(products.get(i) instanceof NoTypeProduct && ( (NoTypeProduct) products.get(i)).checkStock() == false) {
				data[i][0] = "Out of Stock!!! ";
			}
			else if(products.get(i) instanceof Breakfast && ( (Breakfast) products.get(i)).checkStock() == false) {
				data[i][0] = "Out of Stock!!! ";
			}
			else if(products.get(i) instanceof ColdDrinks && ( (ColdDrinks) products.get(i)).checkStock() == false) {
				data[i][0] = "Out of Stock!!! ";
			}
			else if(products.get(i) instanceof EssentialFood && ( (EssentialFood) products.get(i)).checkStock() == false) {
				data[i][0] = "Out of Stock!!! ";
			}
			else if(products.get(i) instanceof FaceProducts && ( (FaceProducts) products.get(i)).checkStock() == false) {
				data[i][0] = "Out of Stock!!! ";
			}
			else if(products.get(i) instanceof HairProducts && ( (FaceProducts) products.get(i)).checkStock() == false) {
				data[i][0] = "Out of Stock!!! ";
			}
			else if(products.get(i) instanceof HomeCareProducts && ( (HomeCareProducts) products.get(i)).checkStock() == false) {
				data[i][0] = "Out of Stock!!! ";
			}
			else if(products.get(i) instanceof OralProducts && ( (OralProducts) products.get(i)).checkStock() == false) {
				data[i][0] = "Out of Stock!!! ";
			}
			else if(products.get(i) instanceof WarmDrinks && ( (WarmDrinks) products.get(i)).checkStock() == false) {
				data[i][0] = "Out of Stock!!! ";
			}
			else if(products.get(i) instanceof Snacks && ( (Snacks) products.get(i)).checkStock() ==  false) {
				data[i][0] = "Out of Stock!!! ";
			}
			else {
				data[i][0] = products.get(i).getName();
				data[i][1] = products.get(i).getBrand();
				data[i][2] = products.get(i).getPrice() + "tl";
				data[i][3] = "x";
				
				new_content = new_content + "\nProduct Name: " + products.get(i).getName() + 
						"\nProduct No: " + allBarcodeNo.get(i) + 
						"\nMarket Name: " + products.get(i).getBrand().trim()  + 
						"\nProduct Price: " + products.get(i).getPrice() + 
						"\nProduct Type: " + types.get(i) + "\n\n"; 
				
				j++;
				String tempText = products.get(i).getBrand() + " " + products.get(i).getName() + " " + products.get(i).getPrice() + "tl";
				System.out.println(tempText);

			}
		}
		String file_name_1 = ".\\users\\" + current_user.getEmail().substring(0, index_f_email) + ".txt";
		
		try {
			System.out.println(new_content);
			FileWriter write_t_file = new FileWriter(file_name_1);
			PrintWriter printWriter = new PrintWriter(write_t_file);
			
			printWriter.printf(new_content);

			write_t_file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		//table settings
		model = new DefaultTableModel(data, column);
		
		table = new JTable(model);
		
		JScrollPane scroll = new JScrollPane(table);
		panel.add(scroll);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(190);
		table.getColumnModel().getColumn(1).setPreferredWidth(50);
		table.getColumnModel().getColumn(2).setPreferredWidth(48);
		table.getColumnModel().getColumn(3).setPreferredWidth(19);
		
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(new Color(198,23,157));
		
		scroll.setBounds(0, 100, 325, 140);
		table.setFocusable(false);
		table.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
		table.setBackground(new Color(54, 52, 55));
		table.setForeground(Color.white);
		table.setEnabled(false);
		scroll.setBackground(new Color(54, 52, 55));
		scroll.setVisible(true);
	}

	// Frame settings
	public void setFrameSettings() {
		
		Image icon_IMG = Toolkit.getDefaultToolkit().getImage(".\\resources\\Logo.jpeg"); // saves icon
		
		// Frame settings
		frame.setTitle("cheAPP");
		frame.setIconImage(icon_IMG); // changes icon
		frame.setSize(335, 525);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit when press to close button
		frame.setResizable(false); // closes size icon
		frame.setLocationRelativeTo(null); // opens the panel middle of the screen
		frame.getContentPane().setLayout(new GridLayout(0, 1));
		
		// Adding gradient objects into the frame
		frame.add(panel);
		
		// Adding objects into the gradient panel
		panel.setLayout(null); // !important
		panel.add(button_Return); 
		panel.add(button_ClearAll);
		panel.add(label_Logo);
		
		// Calls other settings
		setLabelSettings();
		setButtonSettings();
		setTextFieldSettings();
				

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
		
		// Clear All button
		button_ClearAll.setText("Clear All");
		button_ClearAll.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
		button_ClearAll.setHorizontalTextPosition(SwingConstants.CENTER);
		button_ClearAll.setBounds(10, 420, 100, 25);
		button_ClearAll.setBackground(new Color(134,151,129));
		
		// Add Coupon button
		button_CouponAdd.setText("Add Coupon");
		button_CouponAdd.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
		button_CouponAdd.setHorizontalTextPosition(SwingConstants.CENTER);
		button_CouponAdd.setBounds(180, 380, 130, 25);
		button_CouponAdd.setBackground(new Color(134,151,129));
		
		panel.add(button_CouponAdd);
	}
	
	// TextField Settings
	public void setTextFieldSettings() {
		
		Border border = BorderFactory.createLineBorder(new Color(198,23,157));
		
		// Coupon
		textField_Coupon.setText("Enter Coupon Code");
		textField_Coupon.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
		textField_Coupon.setBounds(10, 380, 140, 25);
		textField_Coupon.setForeground(Color.white);
		textField_Coupon.setOpaque(false); // change background color to transparent
		textField_Coupon.setBorder(border);


		
		panel.add(textField_Coupon);
	}
	
	// For choosing right class to products by using type of the product type
	private Product createProductByType(ArrayList<String> productInfo, String productNo) {
		
		System.out.println(productNo);

		
		Product product; // object of product
		String divideName[];
		String countAndLW[];
		
		String[] breakfast = {"Süt", "Yoðurt", "Bal & Reçel", "Kahvaltýlýk Gevrek", "Peynir", "Krema & Kaymak", "Zeytin", "Þarküteri", "Tereyað"};
		String[] colddrinks = {"Kola & Gazoz & Enerji Ýçeceði","Boza & Þalgam & Ayran & Kefir", "Meyve Suyu", "Su & Maden Suyu"};
		String[] essentialfood = {"Makarna", "Nohut & Fasulye & Buðday", "Pasta Kremasý & Soslar", "Pirinç & Bulgur & Mercimek"
				, "Þeker & Tuz & Baharat", "Un & Ýrmik", "Unlu Mamüller" ,"Ekmek Çeþitleri"};
		String[] faceproducts = {"Vücut Bakým", "Yüz Maskesi", "Yüz Temizleme"};
		String[] hairproducts = {"Saç Maskesi", "Þampuan", "Saç Boyasý", "Taraklar & Fýrçalar"};
		String[] homecareproducts = {"Ahþap & Cam Temizleyici", "Bulaþýk Tablet & Jel Deterjan" , "Çamaþýr Suyu", "Çamaþýr Deterjaný & Yumuþatýcý" 
				,"Ev Temizlik","Parlatýcý & Tuz & Koku Giderici","Kaðýt Havlu & Peçete" ,"Temizlik Bezi & Sünger & Fýrça" ,"Tuvalet Kaðýdý"
				,"Oda Spreyleri" ,"Yüzey Temizleyici" ,"Temizlik Setleri" ,"Haþere Öldürücü"};
		String[] oralproducts = {"Aðýz Bakým Suyu", "Diþ Fýrçasý", "Diþ Macunu"};
		String[] snacks = {"Bisküvi", "Çikolata", "Kekler", "Sakýzlar", "Þekerleme", "Cips & Çerez"};
		String[] warmdrinks = {"Çay & Kahve & Toz Ýçecek"};

		
		String type = productInfo.get(3);
		
		// For creating new Product object
		String marketName = productInfo.get(1);;
		String productName = productInfo.get(0);;
		String weigth = "";
		float liter = 0;
		float price = Float.parseFloat(productInfo.get(2));
		
		int count = 1;
		
		divideName = productName.split(" ");
		
		for(int i=0; i<divideName.length; i++) { // taking weight of product
			if(divideName[i].equals("g") || divideName[i].equals("Kg") || divideName[i].equals("Gr") || divideName[i].equals("gr") || 
					divideName[i].equals("kg") || divideName[i].equals("G")) {
				
				if(divideName[i-1].contains("x")) {
					countAndLW = divideName[i-1].split("x");
				}
				else if(divideName[i-1].contains("X")) {
					countAndLW = divideName[i-1].split("X");
				}
				else if(divideName[i-1].contains("*")) {
					countAndLW = divideName[i-1].split("*");
				}
				else {
					weigth = divideName[i - 1];
					break;
				}
				
				count = Integer.valueOf(countAndLW[0]); // taking amount of product
				weigth = countAndLW[1];
				
			}
			else if(divideName[i].equals("lt") || divideName[i].equals("ml") || divideName[i].equals("Lt")) { // taking ml of product
				
				if(divideName[i-1].contains("x")) { // for example 4x600 ml
					countAndLW = divideName[i-1].split("x");
				}
				else if(divideName[i-1].contains("X")) { // for example 4X300
					countAndLW = divideName[i-1].split("X");
				}
				else if(divideName[i-1].contains("*")) { // for example 4*200	
					countAndLW = divideName[i-1].split("*");

				}
				else {
					liter = Float.valueOf(divideName[i - 1].replace(",", "."));
					break;
				}
				
				count = Integer.valueOf(countAndLW[0]); // taking amount of product
				liter = Float.valueOf(countAndLW[1].replace(",", "."));
				
			}
		}

		ArrayList<String> new_info = new ArrayList<String>();
			
		new_info = getUpdatedPrice(marketName, productNo);

		String new_price = new_info.get(2);
		System.out.println(new_price);

		// Creates objects for every product in their type
		if (Arrays.asList(breakfast).contains(type)) {
			
			product = new Breakfast(weigth , Float.valueOf(new_price), productName, marketName, count);
			if(((Breakfast) product).checkStock()) {
				return product;

			}
			else {
				basket.removeProduct(productName, current_user);
				return product;
			}
		
		}
		else if (Arrays.asList(colddrinks).contains(type)) {
			
			product = new ColdDrinks(weigth, Float.valueOf(new_price), productName, marketName, liter, count);
			if(((ColdDrinks) product).checkStock()) {
				return product;

			}
			else {
				basket.removeProduct(productName, current_user);
				return product;
			}		
		
		}
		else if (Arrays.asList(essentialfood).contains(type)) {
			
			product = new EssentialFood(weigth, Float.valueOf(new_price), productName, marketName, count);
			if(((EssentialFood) product).checkStock()) {
				return product;

			}
			else {
				basket.removeProduct(productName, current_user);
				return product;
			}
		}
		else if (Arrays.asList(faceproducts).contains(type)) {
			product = new FaceProducts(weigth, Float.valueOf(new_price), productName, marketName, count, liter);
			if(((FaceProducts) product).checkStock()) {
				return product;
			}
			else {
				basket.removeProduct(productName, current_user);
				return product;
			}

		}
		else if (Arrays.asList(hairproducts).contains(type)) {
			product = new HairProducts(weigth, Float.valueOf(new_price), productName, marketName, count, liter);
			if(((HairProducts) product).checkStock()) {
				return product;
			}
			else {
				basket.removeProduct(productName, current_user);
				return product;
			}

		}
		else if (Arrays.asList(homecareproducts).contains(type)) {
			product = new HomeCareProducts(weigth, Float.valueOf(new_price), productName, marketName, count, liter);
			if(((HomeCareProducts) product).checkStock()) {
				return product;
			}
			else {
				basket.removeProduct(productName, current_user);
				return product;
			}

		}
		else if (Arrays.asList(oralproducts).contains(type)) {
			product = new OralProducts(weigth, Float.valueOf(new_price), productName, marketName, count, liter);
			if(((OralProducts) product).checkStock()) {
				return product;
			}
			else {
				basket.removeProduct(productName, current_user);
				return product;
			}

		}
		else if (Arrays.asList(snacks).contains(type)) {
			product = new Snacks(weigth, Float.valueOf(new_price), productName, marketName, count);
			if(((Snacks) product).checkStock()) {
				return product;
			}
			else {
				basket.removeProduct(productName, current_user);
				return product;
			}

		}
		else if (Arrays.asList(warmdrinks).contains(type)) {
			product = new WarmDrinks(weigth, Float.valueOf(new_price), productName, marketName, liter, count);
			if(((WarmDrinks) product).checkStock()) {
				return product;
			}
			else {
				basket.removeProduct(productName, current_user);
				return product;
			}

		}
		else {
			if(weigth != "Weight") { // creates objects for weight
				product = new NoTypeProduct(weigth, Float.valueOf(new_price), productName, marketName);
				if(((NoTypeProduct) product).checkStock()) {
					return product;
				}
				else {
					basket.removeProduct(productName, current_user);
					return product;
				}

			}
			else { // creates objects for liter
				product = new NoTypeProduct(weigth, Float.valueOf(new_price), productName, marketName, liter);
				if(((NoTypeProduct) product).checkStock()) {
					return product;
				}
				else {
					basket.removeProduct(productName, current_user);
					return product;
				}


			}
		}
	}
	
	// Label settings and Product
	public void setLabelSettings() {
		
		label_Logo.setLocation(210, 15);
		label_Logo.setOpaque(false);
		label_Logo.setSize(100,30);
		
		// Total price
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		String s=nf.format(basket.getPrice(current_user));
		label_TotalPrice.setText("Total price: " + String.valueOf(s + "tl"));
		label_TotalPrice.setBounds(172,240, 150, 30);
		label_TotalPrice.setFont(new Font(Font.DIALOG, Font.PLAIN, 13));
		label_TotalPrice.setForeground(Color.WHITE);
				
		panel.add(label_TotalPrice);
		
	}
	
	// Finds how many product that user has on his/her basket
	public int productCount() {
		
		// returns product count
		int productCount = basket.getProductCount(current_user);
		return productCount;
	}
	
	// Check stock
	public ArrayList<String> getUpdatedPrice(String marketName, String productNo) {
		marketName = marketName.trim();
		
		ArrayList<String> info = new ArrayList<String>();
		
		if(marketName.equals("A101")) {
			info = PriceTaker.a101(productNo);
			return info;
		}
		else if(marketName.equals("CARREFOUR")) {
			info = PriceTaker.carrefour(productNo);
			return info;
		}
		else if(marketName.equals("Amazon")) {
			info = PriceTaker.amazon(productNo);
			return info;
		}
		else if(marketName.equals("TRENDYOL")) {
			info = PriceTaker.trendyol(productNo);
			return info;
		}
		else if(marketName.equals("HepsiBurada")) {
			info = PriceTaker.hepsiburada(productNo);
			return info;
		}
		else {
			return info;
		}
	}
} // end of BasketPanel class
