
public class Drinks extends Product {

	// Variables
	private float liter;
	private int count;
	
	// Constructor
	public Drinks(String weight, float price, String name, String brand, float liter, int count) {
		super(weight, price, name, brand);
		setLiter(liter);
		setCount(count);
		
	}
	
	// Getters and Setters

	public float getLiter() {
		return liter;
	}

	public void setLiter(float liter) {
		this.liter = liter;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
