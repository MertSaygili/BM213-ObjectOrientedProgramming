import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class PriceTaker {
	
	// Functions
	public final static String maxValue = "1000000000"; // For not finding product
	
	private static WebDriver createDriver() {
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("--headless");
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		WebDriver driver = new ChromeDriver(chromeOptions);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		return driver;
	}
	
	public static ArrayList<String> a101(String barcode) {
		ArrayList<String> info = new ArrayList<String>();
		WebDriver driver = createDriver();
		try {
			
			driver.navigate().to("https://www.a101.com.tr/list/?search_text="+ barcode);
			
			
			String name = driver.findElement(By.className("name")).getText();
			String price = driver.findElement(By.className("current")).getText();
			
			price = price.replace(',', '.');
			price = price.split(" ")[0];
			
			driver.findElement(By.className("name-price")).click();
			
			List<WebElement> list = driver.findElements(By.className("breadcrumb-item"));
			String type = list.get(list.size() - 2).getText();
			
			
			info.add("A101"); info.add(name); info.add(price); info.add(type); 
			
			System.out.print(info);
			driver.quit();
			return info;
		}
		catch (Exception e) {
			driver.quit();
			info.add("A101"); info.add(""); info.add(maxValue); info.add("");
			System.out.println("There was an error while finding product");
			return info;
		}
	}
	
	public static ArrayList<String> carrefour(String barcode) {
		ArrayList<String> info = new ArrayList<String>();
		WebDriver driver = createDriver();
		try {

			
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.navigate().to("https://www.carrefoursa.com/search/?text="+barcode);
			
			String price = driver.findElement(By.className("price-cont")).getText();
			String name = driver.findElement(By.className("item-name")).getText();
					
			price = price.replace(',', '.');
			price = price.split(" ")[0];
			
			if (price == "") {
				price = maxValue; // Carrefourda bunu yapmak zorundayým çünkü ürünü bulamasa bile bir hata vermiyor.
			}
			
			info.add("CARREFOUR"); info.add(name); info.add(price); 
			
			System.out.println(info);
			
			driver.quit();
			
			return info;
		}
		catch (Exception e) {
			driver.quit();
			info.add("CARREFOUR"); info.add(""); info.add(maxValue);
			System.out.println("There was an error while findind product");
			return info;
		}
	}
	
	public static ArrayList<String> amazon(String barcode) {
		ArrayList<String> info = new ArrayList<String>();
		WebDriver driver = createDriver();
		try {

			driver.navigate().to("https://www.amazon.com.tr/s?k=" + barcode);
			
			driver.findElement(By.id("sp-cc-accept")).click();
			WebElement body = driver.findElement(By.tagName("body")); // önce bodye geçip sonra bakýyorum
			String name = body.findElement(By.className("a-size-base-plus")).getText();
			String price = body.findElement(By.className("a-price-whole")).getText();
			
			
			price = price + "." + body.findElement(By.className("a-price-fraction")).getText();
			price = price.replace(',', ' ');
			price = price.split(" ")[0];
			

			info.add("Amazon"); info.add(name); info.add(price); 
			
			System.out.print(info);
			
			driver.quit();
			
			return info;
			
		}
		catch (Exception e) {
			driver.quit();
			info.add("Amazon"); info.add(""); info.add(maxValue);
			System.out.println("There was an error while findind product");
			return info;
		}
	}
	
	public static ArrayList<String> trendyol(String barcode) {
		
		ArrayList<String> info = new ArrayList<String>();
		WebDriver driver = createDriver();
		try {

			
			driver.navigate().to("https://www.trendyol.com/sr?q="+barcode);
			
			
			
			WebElement body = driver.findElement(By.tagName("body")); // Bodye geçip onda arýyorum.
			String name = body.findElement(By.className("prdct-desc-cntnr-name")).getText();
			String price = body.findElement(By.className("prc-box-sllng")).getText();
			
			price = price.replace(',', '.');
			price = price.split(" ")[0];		
			
			info.add("TRENDYOL"); info.add(name); info.add(price); 
			
			System.out.print(info);
			
			driver.quit();
			
			return info;
		}
		catch (Exception e) {
			System.out.println(e);
			driver.quit();
			info.add("TRENDYOL"); info.add(""); info.add(maxValue); 
			System.out.println("There was an error while finding product");
			return info;
		}
	}
	
	public static ArrayList<String> hepsiburada(String barcode) {
		ArrayList<String> info = new ArrayList<String>();
		WebDriver driver = createDriver();
		try {

			driver.navigate().to("https://www.hepsiburada.com/ara?q=" + barcode);
			
			String name = driver.findElement(By.xpath("/html/body/div[3]/main/div[2]/div/div[6]/div[2]/div/div[3]/div/div/div/div/div/div/div/ul/li/div/a/div[3]/h3")).getText();
			String price = driver.findElement(By.xpath("/html/body/div[3]/main/div[2]/div/div[6]/div[2]/div/div[3]/div/div/div/div/div/div/div/ul/li/div/a/div[3]/div[1]/div[2]")).getText();
			
			price = price.replace(',', '.');
			price = price.split(" ")[0];			
			
			info.add("HepsiBurada"); info.add(name); info.add(price);
			
			System.out.println(info);
			
			driver.quit();
			
			return info;
		}
		catch (Exception e) {
			driver.quit();
			info.add("HepsiBurada"); info.add(""); info.add(maxValue);
			System.out.println("There was an error while finding product");
			return info;
		}
	}
	
}
