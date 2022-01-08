 
public abstract class Product {
	
	// Variables
	private String weight;
	private float price;
	private String name;
	private String brand;
	
	
	// Constructor
	public Product(String weight, float price, String name, String brand) {
		setWeight(weight);
		setPrice(price);
		setName(name);
		setBrand(brand);
	}
	
	
	// Getters and Setters
	
	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	
}

