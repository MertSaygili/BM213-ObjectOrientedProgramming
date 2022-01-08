import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Basket implements Paid {
	
	//  Variables
	private float totalPrice;
	private int productCount;
	private ArrayList<Product> products = new ArrayList<>();
	private int discount_rate;
	private Map<String, Float> coupons = new HashMap<>();
	// private coupons hash seklinde gelecek
	
	{ // Global coupons values
		coupons.put("17129207", (float) 12);
		coupons.put("199188177", (float) 25);
		coupons.put("111999222888", (float) 40);
	}
	
	
	// Getters and Setters
	public void setProducts(Product[] product) {
		
	}
	
	public ArrayList<Product>  getProducts() {
		return this.products; // just for now
	}
	
	public  int getProductCount(User current_user) {
		// File variables
		int index_f_email = current_user.getEmail().indexOf("."), product_count = 0;;
		String file_name = ".\\users\\" + current_user.getEmail().substring(0, index_f_email) + ".txt", line = "";
		try { // Opening and reading file
			File file = new File(file_name);
			BufferedReader read = new BufferedReader(new FileReader(file_name));
			while((line = read.readLine()) != null) {
				if(line.contains("Market Name: ")) {
					product_count = product_count + 1; // with this loop we will find how many product we have
				}
			}
			read.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		return product_count;
	}
	
	// Functions
	public void clear(User current_user) { // clears all products from basket
		
		int index_f_email = current_user.getEmail().indexOf(".");
		String file_name = ".\\users\\" + current_user.getEmail().substring(0, index_f_email) + ".txt", line = "", information = "";
		String new_content = "";
		try {
			new_content = "E-mail: " + current_user.getEmail() + "\nPassowrd: " + current_user.getPassoword() + "\n";
			
			System.out.println(new_content);
			FileWriter write_t_file = new FileWriter(file_name);
			PrintWriter printWriter = new PrintWriter(write_t_file);
			
			printWriter.printf(new_content);
			
			write_t_file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addProduct(Product product) { // adds product to basket
		products.add(product);
	}
	
	public void removeProduct(String product_name, User current_user) { // removes product from basket
		
		int index_f_email = current_user.getEmail().indexOf(".");
		String file_name = ".\\users\\" + current_user.getEmail().substring(0, index_f_email) + ".txt", line = "";
		String new_content = "";
		if (this.getProductCount(current_user) == 1) {
			this.clear(current_user);
			return;
		}
		try {
			File file = new File(file_name);
			BufferedReader read = new BufferedReader(new FileReader(file_name));
			
			boolean isFound = false;
			while((line = read.readLine()) != null) 
			{
				if(line.contains(product_name) && !isFound)
				{
					isFound = true;
					for (int x = 0; x<7;x++) {
						line = read.readLine();
					}
				}
				new_content = new_content + line + "\n";
			}
			read.close();
			FileWriter write_t_file = new FileWriter(file_name);
			PrintWriter printWriter = new PrintWriter(write_t_file);
			
			printWriter.printf(new_content);
			
			write_t_file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public float addCoupon(String coupon_code, User current_user, float price) {
		float total_price = price;
					
		if(coupons.containsKey(coupon_code)) {
			total_price = total_price - ( ( total_price * coupons.get(coupon_code)) / 100);
			coupons.remove(coupon_code); // removes coupon after coupon has used
			return total_price;
		}	
		else {
			return 0;
		}
	}

	
	@Override
	public float getPrice(User current_user) { // returns total price of basket
		float total_price = 0;
		int index_f_email = current_user.getEmail().indexOf(".");
		String file_name = ".\\users\\" + current_user.getEmail().substring(0, index_f_email) + ".txt", line = "";
		try {
			File file = new File(file_name); // opens file 
			BufferedReader read = new BufferedReader(new FileReader(file_name));
			
			while((line = read.readLine()) != null ){ // takes every price and add to the total_price variable
				if(line.contains("Product Price: ")) {
					total_price = total_price + Float.valueOf(line.replaceAll("Product Price: ", ""));
				}
			}
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return total_price;
	}

}
