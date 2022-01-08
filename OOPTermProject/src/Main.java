 
public class Main {
	// Main Class of Project
	
	public static void main(String[] args) {
		 LogInSignUp login = new LogInSignUp();
		
		User user1 = new User();
		user1.setEmail("mert71719601@gmail.com");
		user1.setPassword("MerhabaMert");
		MainPanel mp = new MainPanel(user1);
	}
}