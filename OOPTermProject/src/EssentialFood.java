
public class EssentialFood extends Eatables {
	
	// Constructor
	public EssentialFood(String weight, float price, String name, String brand, int count) {
		super(weight, price, name, brand, count);

	}
	
	// Functions
	
	public boolean checkStock() {
		if(super.getPrice() == 1000000000) {
			return false;
		}
		return true;
	}
	
	
	public float getPrice() {
		return super.getPrice();
	}
}
