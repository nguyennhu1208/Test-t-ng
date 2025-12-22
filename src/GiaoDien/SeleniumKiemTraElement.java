package GiaoDien;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SeleniumKiemTraElement {

    private WebDriver driver;
    private WebDriverWait wait;

    /* ===============================
       MỞ TRÌNH DUYỆT & TRUY CẬP WEB
       =============================== */
    public void openWebsite(String url) {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    /* ===============================
       KIỂM TRA ELEMENT THEO LABEL
       =============================== */
    public boolean checkElement(String label) {
        try {
            // 1Tìm theo thẻ <label>
            List<WebElement> labelTags = driver.findElements(
                    By.xpath("//label[normalize-space()='" + label + "']")
            );
            if (!labelTags.isEmpty()) {
                return true;
            }

            // Tìm theo input / textarea / select
            String xpath =
                    "//*[self::input or self::textarea or self::select]" +
                    "[normalize-space(@placeholder)='" + label + "'" +
                    " or normalize-space(@aria-label)='" + label + "'" +
                    " or normalize-space(@name)='" + label + "'" +
                    " or normalize-space(@id)='" + label + "']";

            List<WebElement> elements = driver.findElements(By.xpath(xpath));
            return !elements.isEmpty();

        } catch (Exception e) {
            return false;
        }
    }

    /* ===============================
       ĐÓNG TRÌNH DUYỆT
       =============================== */
    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }
}
