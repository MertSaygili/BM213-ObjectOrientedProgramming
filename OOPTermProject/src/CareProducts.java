
public class CareProducts extends Product{
	// Variables
	private int count;
	private float liter;
	
	
	// Constructor
	public CareProducts(String weight, float price, String name, String brand, int count, float liter) {
		super(weight, price, name, brand);
		setCount(count);
		setLiter(liter);
		
	}

	// Getters and Setters
	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}

	public float getLiter() {
		return liter;
	}

	public void setLiter(float liter) {
		this.liter = liter;
	}

}
