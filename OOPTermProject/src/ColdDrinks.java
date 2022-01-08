
public class ColdDrinks extends Drinks implements Paid{

	
	// Constructor
	public ColdDrinks(String weight, float price, String name, String brand, float liter, int count) {
		super(weight, price, name, brand, liter, count);
		
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
