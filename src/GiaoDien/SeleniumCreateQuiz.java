package GiaoDien;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class SeleniumCreateQuiz {
	private static WebElement openQuestionForm(WebDriver driver, int index) {

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    By questionBlock = By.xpath(
	        "(//div[contains(@class,'border-gray-200') and contains(@class,'mb-6')])["
	        + (index + 1) + "]"
	    );

	    WebElement block = wait.until(
	        ExpectedConditions.presenceOfElementLocated(questionBlock)
	    );

	    WebElement arrow = block.findElement(
	        By.xpath(".//button[contains(@class,'ml-auto')]")
	    );

	    ((JavascriptExecutor) driver).executeScript(
	        "arguments[0].scrollIntoView({block:'center'});", arrow
	    );

	    ((JavascriptExecutor) driver).executeScript(
	        "arguments[0].click();", arrow
	    );

	    return block;
	}

	public static void run(
			Map<String, String> info,
			List<Question> questions
	) {
		try {
			WebDriver driver = new ChromeDriver();
			driver.manage().window().maximize();

			driver.get("https://test.psi.plt.pro.vn/login");
			Thread.sleep(3000);
			  driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[1]/input"))
              .sendKeys("admin@test.psi.plt.com");
		      driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[2]/div/input"))
		              .sendKeys("123456");
		      driver.findElement(By.xpath("/html/body/div/div[1]/div/form/button"))
		              .click();
		  	Thread.sleep(3000);
		      driver.get("https://test.psi.plt.pro.vn/admin/quiz-management");
		    	Thread.sleep(3000);
		      //nhấn nút thêm bộ câu hỏi
		      driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div[2]/div/div[3]/div[2]/button"))
		      .click();
		    	Thread.sleep(3000);
			/* ===== NHẬP THÔNG TIN BỘ ===== */
			driver.findElement(By.xpath("//input[contains(@placeholder,'Nhập tên bộ câu hỏi...')]"))
					.sendKeys(info.get("TEN_BO"));

			driver.findElement(By.xpath("//textarea[contains(@placeholder,'Mô tả chi tiết về bộ câu hỏi...')]"))
					.sendKeys(info.get("MO_TA"));
			
			driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div[2]/div/div[2]/div[3]/div[1]/input"))
			.clear();
			
			driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div[2]/div/div[2]/div[3]/div[1]/input"))
					.sendKeys(info.get("THOI_GIAN"));
			
			driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div[2]/div/div[2]/div[3]/div[2]/input"))
			.clear();
			
			driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div[2]/div/div[2]/div[3]/div[2]/input"))
					.sendKeys(info.get("SO_CAU"));

			Thread.sleep(1000);


			/* ===== THÊM CÂU HỎI ===== */
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			for (int i = 0; i < questions.size(); i++) {

			    Question q = questions.get(i);
			    WebElement questionBlock;

			    // Câu đầu tiên mở sẵn
			    if (i == 0) {
			        questionBlock = wait.until(
			            ExpectedConditions.presenceOfElementLocated(
			                By.xpath("(//div[contains(@class,'border-gray-200') and contains(@class,'mb-6')])[1]")
			            )
			        );
			    } else {
			        questionBlock = openQuestionForm(driver, i);
			        Thread.sleep(500);
			    }

			    // ===== NỘI DUNG CÂU HỎI =====
			    WebElement content = questionBlock.findElement(
			        By.xpath(".//textarea[@placeholder='Nội dung câu hỏi']")
			    );
			    content.clear();
			    content.sendKeys(q.content);

			    // ===== LẤY TOÀN BỘ INPUT TEXT TRONG BLOCK =====
			    List<WebElement> allTextInputs = questionBlock.findElements(
			        By.xpath(".//input[@type='text']")
			    );

			    // LẤY 4 INPUT CUỐI = 4 ĐÁP ÁN
			    int size = allTextInputs.size();
			    WebElement ansA = allTextInputs.get(size - 4);
			    WebElement ansB = allTextInputs.get(size - 3);
			    WebElement ansC = allTextInputs.get(size - 2);
			    WebElement ansD = allTextInputs.get(size - 1);

			    ansA.clear(); ansA.sendKeys(q.a);
			    ansB.clear(); ansB.sendKeys(q.b);
			    ansC.clear(); ansC.sendKeys(q.c);
			    ansD.clear(); ansD.sendKeys(q.d);

			    // ===== RADIO =====
			    List<WebElement> radios = questionBlock.findElements(
			        By.xpath(".//input[@type='radio']")
			    );

			    int correctIndex = "ABCD".indexOf(q.correct);
			    radios.get(correctIndex).click();

			    // ===== THÊM FORM MỚI =====
			    if (i < questions.size() - 1) {

			        WebElement btnAdd = wait.until(
			            ExpectedConditions.elementToBeClickable(
			                By.xpath("//button[contains(text(),'+ Thêm câu hỏi')]")
			            )
			        );

			        ((JavascriptExecutor) driver).executeScript(
			            "arguments[0].scrollIntoView({block:'center'});", btnAdd
			        );
			        ((JavascriptExecutor) driver).executeScript(
			            "arguments[0].click();", btnAdd
			        );

			        Thread.sleep(800);
			    }
			}
			WebDriverWait waitSave = new WebDriverWait(driver, Duration.ofSeconds(10));

			By btnSaveLocator = By.xpath("//button[.//span[contains(text(),'Lưu câu hỏi')] or contains(text(),'Lưu câu hỏi')]");

			WebElement btnSave = waitSave.until(
			    ExpectedConditions.elementToBeClickable(btnSaveLocator)
			);

			// scroll tới nút
			((JavascriptExecutor) driver).executeScript(
			    "arguments[0].scrollIntoView({block:'center'});", btnSave
			);

			// đợi UI ổn định
			Thread.sleep(500);

			// click bằng JS (an toàn nhất với React)
			((JavascriptExecutor) driver).executeScript(
			    "arguments[0].click();", btnSave
			);



		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
