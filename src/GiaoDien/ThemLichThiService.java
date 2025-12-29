package GiaoDien;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.Map;

public class ThemLichThiService {

    public static void run(Map<String, String> info) {

        WebDriver driver = null;

        try {
            driver = new ChromeDriver();
            driver.manage().window().maximize();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            /* ================= LOGIN ================= */
            driver.get("https://test.psi.plt.pro.vn/login");

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@type='email']")
            )).sendKeys("admin@test.psi.plt.com");

            driver.findElement(By.xpath("//input[@type='password']"))
                    .sendKeys("123456");

            driver.findElement(By.xpath("//button[contains(text(),'Đăng nhập')]"))
                    .click();

            /* ================= VÀO TRANG LỊCH THI ================= */
            wait.until(ExpectedConditions.urlContains("/admin"));
            driver.get("https://test.psi.plt.pro.vn/admin/quiz-management/quiz-schedule");

            /* ================= NHẤN TẠO MỚI ================= */
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("/html/body/div/div[1]/div[2]/div[2]/div/div[2]/div[2]/div[2]/button")
            )).click();

            /* ================= FORM ================= */

            // ===== TÊN ĐỀ THI =====
            WebElement ddlDe = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.name("quiz"))
            );
            selectByContainsText(ddlDe, info.get("TEN_DE_THI"));

            // ===== MÃ ĐỀ THI =====
            WebElement txtMaDe = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//input[contains(@placeholder,'PHP')]")
                    )
            );
            txtMaDe.clear();
            txtMaDe.sendKeys(info.get("MA_DE"));

            // ===== LỚP THI =====
            WebElement ddlLop = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.name("class_id"))
            );
            selectByContainsText(ddlLop, info.get("LOP_THI"));

            // ===== THỜI GIAN BẮT ĐẦU =====
            WebElement batDau = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//label[normalize-space()='Thời gian bắt đầu *']/following::input[1]")
                    )
            );
            batDau.clear();
            batDau.sendKeys(info.get("BAT_DAU"));

            // ===== THỜI GIAN KẾT THÚC =====
            WebElement ketThuc = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//label[normalize-space()='Thời gian kết thúc *']/following::input[1]")
                    )
            );
            ketThuc.clear();
            ketThuc.sendKeys(info.get("KET_THUC"));

            // ===== SỐ LẦN LÀM BÀI =====
            int soLan = Integer.parseInt(info.get("SO_LAN"));
            WebElement btnPlus = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//span[normalize-space()='Số lần làm bài tối đa']/following::button[.='+'][1]")
                    )
            );
            for (int i = 1; i < soLan; i++) {
                btnPlus.click();
            }

            // ===== XÁO TRỘN CÂU HỎI =====
            if ("TRUE".equalsIgnoreCase(info.get("XAO_CAU"))) {
                clickIfNotSelected(driver,
                        "//label[normalize-space()='Xáo trộn thứ tự câu hỏi']/preceding::input[@type='checkbox'][1]");
            }

            // ===== XÁO TRỘN ĐÁP ÁN =====
            if ("TRUE".equalsIgnoreCase(info.get("XAO_DAP_AN"))) {
                clickIfNotSelected(driver,
                        "//label[normalize-space()='Xáo trộn thứ tự đáp án']/preceding::input[@type='checkbox'][1]");
            }

            // ===== LƯU =====
            WebElement btnSave = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[normalize-space()='Lưu lịch thi']")
                    )
            );

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", btnSave
            );
            btnSave.click();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= HÀM DÙNG CHUNG ================= */

    private static void selectByContainsText(WebElement selectElement, String expectedText) {
        Select select = new Select(selectElement);
        String value = expectedText.trim();

        for (WebElement option : select.getOptions()) {
            if (option.getText().trim().contains(value)) {
                option.click();
                return;
            }
        }

        throw new RuntimeException("❌ Không tìm thấy option: " + value);
    }

    private static void clickIfNotSelected(WebDriver driver, String xpath) {
        WebElement checkbox = driver.findElement(By.xpath(xpath));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }
}
