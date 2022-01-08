
public class NoTypeProduct extends Product implements Paid{

	private float liter;
	
	// Constructor 1
	public NoTypeProduct(String weight, float price, String name, String brand) {
		super(weight, price, name, brand);
	}

	// Constructor 2
	public NoTypeProduct(String weight, float price, String name, String brand, float liter) {
		super(weight, price, name, brand);
		setLiter(liter);
	}

	// Getters and Setters
	public float getLiter() {
		return liter;
	}

	public void setLiter(float liter) {
		this.liter = liter;
	}
	
	public boolean checkStock() {
		if(super.getPrice() == 1000000000) {
			return false;
		}
		return true;
	}
	


	@Override
	public float getPrice(User current_user) {
		return liter;
		
	}
}
