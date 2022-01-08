
public class HomeCareProducts extends CareProducts implements Paid {

	// Constructor
	public HomeCareProducts(String weight, float price, String name, String brand, int count, float liter) {
		super(weight, price, name, brand, count, liter);

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
		// TODO Auto-generated method stub
		return super.getPrice();
	}

}
