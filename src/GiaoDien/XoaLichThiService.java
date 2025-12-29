package GiaoDien;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.List;


public class XoaLichThiService {

    public interface LichThiCallback {
        void onRowResult(int stt, String maLop, String tenLop, String result);
        void onProgress(int percent);
    }

    public static void runTest(String url, String excelPath, LichThiCallback callback) throws Exception {

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // ===== LOGIN =====
            driver.get(url);

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@type='email']")))
                    .sendKeys("admin@test.psi.plt.com");

            driver.findElement(By.xpath("//input[@type='password']"))
                    .sendKeys("123456");

            driver.findElement(By.xpath("//button[contains(text(),'Đăng nhập')]"))
                    .click();

            // ===== VÀO MENU LỊCH THI =====
            wait.until(ExpectedConditions.urlContains("/admin"));
            driver.get("https://test.psi.plt.pro.vn/admin/quiz-management/quiz-schedule");

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//table//tbody//tr")));

            // ===== ĐỌC EXCEL =====
            Workbook workbook = new XSSFWorkbook(new FileInputStream(excelPath));
            Sheet sheet = workbook.getSheetAt(0);

            int totalRow = sheet.getLastRowNum();

            for (int i = 1; i <= totalRow; i++) {

                Row row = sheet.getRow(i);
                String maLop = row.getCell(0).getStringCellValue().trim();
                String tenLop = row.getCell(1).getStringCellValue().trim();

                List<WebElement> rows = driver.findElements(
                        By.xpath("//tr[td[normalize-space()='" + maLop + "']]")
                );

                if (rows.isEmpty()) {
                    callback.onRowResult(i, maLop, tenLop, "❌ Không tìm thấy lịch thi");
                    callback.onProgress(i * 100 / totalRow);
                    continue;
                }

                WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//tr[td[normalize-space()='" + maLop +
                                "']]//*[name()='svg' and contains(@class,'trash')]/ancestor::button")
                ));
                deleteBtn.click();

                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                alert.accept();

                WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//p")
                ));

                callback.onRowResult(i, maLop, tenLop, msg.getText());
                callback.onProgress(i * 100 / totalRow);

                wait.until(ExpectedConditions.invisibilityOf(rows.get(0)));
            }

            workbook.close();

        } finally {
            driver.quit();
        }
    }
}

