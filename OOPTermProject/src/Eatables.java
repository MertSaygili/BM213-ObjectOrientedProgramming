
public class Eatables extends Product {
	
	// Variables
	private int count;

	
	// Constructor
	public Eatables(String weight, float price, String name, String brand, int count) {
		super(weight, price, name, brand);
		setCount(count);
	}

	// Getters and Setters
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
