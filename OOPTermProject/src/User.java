public class User {
	
	// Variables
	private String e_mail;
	private String password;
	private Basket basket;
	
	
	// Getters and Setters
	
	public String getEmail() {
		return e_mail;
	}
	
	public void setEmail(String e_mail) {
		this.e_mail = e_mail;
	}
	
	public String getPassoword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Basket getBasket() {
		return this.basket;
	}
	
	// Functions
	public void logOut() {
		System.exit(0); // closes the application
	}
}
