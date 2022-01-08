
public class Snacks extends Eatables implements Paid {

	// Constructor
	public Snacks(String weight, float price, String name, String brand, int count) {
		super(weight, price, name, brand, count);

	}
	
	// Functions
	
	public boolean checkStock() {
		if(super.getPrice() == 1000000000) {
			return false;
		}
		return true;
	}
	

	@Override
	public float getPrice(User current_user) {
		
		return super.getPrice();
	}
	

}
