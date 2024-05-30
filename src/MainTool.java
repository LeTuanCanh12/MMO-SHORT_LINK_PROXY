
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainTool {
	private static WebDriver driver;

	private static int proxyDie = 0;
	private static int proxyId = 0;

	public MainTool() {
		// Khởi tạo WebDriver
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
	}

	public static void start(String link, String pathProxy) throws InterruptedException, IOException, ParseException {
		// TODO Auto-generated method stub
		// Tiến trình 1
		try {
			setProxy(getProxyApi(pathProxy));
			driver.get(link);
			Thread.sleep(2000);
			WebElement clickSubmit1 = driver.findElement(By.cssSelector("button[type='submit']"));
			JavascriptExecutor jsExecutor1 = (JavascriptExecutor) driver;
			jsExecutor1.executeScript("arguments[0].click();", clickSubmit1);
			Thread.sleep(2000);
			WebElement clickSubmit2 = driver.findElement(By.cssSelector("button[type='submit']"));
			JavascriptExecutor jsExecutor2 = (JavascriptExecutor) driver;
			jsExecutor2.executeScript("arguments[0].click();", clickSubmit2);
		} catch (TimeoutException e) {
			// TODO: handle exception
			driver.quit();
		} finally {
			driver.quit();
			start(link, pathProxy);
		}

		// Tiến trình 2
//		Thread thread2 = new Thread(new Runnable() {
//			public void run() {
//				driver = new ChromeDriver();
//				driver.get(link);
//				try {
//					Thread.sleep(2000);
//					WebElement clickSubmit1 = driver.findElement(By.cssSelector("button[type='submit']"));
//					JavascriptExecutor jsExecutor1 = (JavascriptExecutor) driver;
//					jsExecutor1.executeScript("arguments[0].click();", clickSubmit1);
//					Thread.sleep(2000);
//					WebElement clickSubmit2 = driver.findElement(By.cssSelector("button[type='submit']"));
//					JavascriptExecutor jsExecutor2 = (JavascriptExecutor) driver;
//					jsExecutor2.executeScript("arguments[0].click();", clickSubmit2);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		});
//		Thread thread1 = new Thread(new Runnable() {
//			public void run() {
//				int seconds = 0;
//				while (seconds < timeOut) {
//					try {
//						Thread.sleep(1000);
//						seconds++;
//						System.out.println(seconds + "s");
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				driver.quit();
//				thread2.stop();
//
//			}
//		});
//
//		// Khởi chạy hai tiến trình
//		thread1.start();
//		thread2.start();

	}

	public static void setProxy(String proxyInfor) throws InterruptedException {
		String[] rs = proxyInfor.split(":");
		String proxyAddress = "";
		int proxyPort = 6547;
		String proxyUsername = "";
		String proxyPassword = "";
		proxyAddress = rs[0];

		proxyPort = Integer.valueOf(rs[1]);

		proxyUsername = rs[2];

		proxyPassword = rs[3];

		Proxy proxy = new Proxy();
		proxy.setProxyType(Proxy.ProxyType.MANUAL);
		proxy.setHttpProxy(proxyAddress + ":" + proxyPort); // Thay "ip_proxy" và "port_proxy" bằng địa chỉ IP và cổng
															// của proxy //
		// // SOCKS
		proxy.setSslProxy(proxyAddress + ":" + proxyPort);

		// Khởi tạo đối tượng ChromeOptions và cấu hình proxy
		ChromeOptions options = new ChromeOptions();
		options.setProxy(proxy);

		options.addExtensions(new File("Proxy-Auto-Auth.crx"));

		// Khởi tạo trình duyệt WebDriver với cấu hình proxy
		driver = new ChromeDriver(options);
		driver.get("chrome-extension://ggmdpepbjljkkkdaklfihhngmmgmpggp/options.html");
		Thread.sleep(1000);
		// Sử dụng trình duyệt với proxy để thực hiện các hoạt động Selenium
		// Ví dụ:
		driver.findElement(By.id("login")).sendKeys(proxyUsername);
		driver.findElement(By.id("password")).sendKeys(proxyPassword);
		driver.findElement(By.id("retry")).clear();
		driver.findElement(By.id("retry")).sendKeys("2");

		driver.findElement(By.id("save")).click();
		proxyId++;
		System.out.println("Số proxy đã get: " + proxyId);
		// Đóng trình duyệt

	}

	public static String readProxy(String pathFile) throws IOException {
		byte[] fileBytes = Files.readAllBytes(Paths.get(pathFile));

		// Chuyển đổi mảng byte[] thành chuỗi sử dụng class Scanner
		return new String(fileBytes).replaceAll("\\r\\n|\\r|\\n", ":");
	}

	public static String getProxyApi(String linkApi) throws ParseException {
		try {
			// Tạo URL của API
			URL url = new URL(linkApi);

			// Mở kết nối HTTP
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Đặt phương thức yêu cầu là GET
			connection.setRequestMethod("GET");

			// Đọc dữ liệu từ kết nối
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			// Đóng kết nối
			connection.disconnect();
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(response.toString());

			// Lấy phần tử đầu tiên trong mảng

			// Lấy dữ liệu từ trường "data"
			JSONObject dataObject = (JSONObject) jsonObject.get("data");

			// Lấy giá trị "proxy"
			String proxy = (String) dataObject.get("proxy");
			System.out.println(proxy);
			return proxy;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static void main(String[] args) throws InterruptedException, IOException, ParseException {
		MainTool test = new MainTool();
		for (int i = 0; i < 2000; i++) {
			System.out.println(test.getProxyApi(""));
		}

	}
}