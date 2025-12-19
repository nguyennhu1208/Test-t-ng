package GiaoDien;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

public class XoaLopHocService {

    // ===== CALLBACK =====
    public interface LopHocCallback {
        void onRowResult(int stt, String maLop, String tenLop, String result);
        void onProgress(int percent);
    }

    public static void runTest(String url, String excelPath, LopHocCallback callback) throws Exception {

        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\admin\\Downloads\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // ===== LOGIN =====
            driver.get(url);

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@type='email' or @placeholder='Email']")))
                    .sendKeys("admin@test.psi.plt.com");

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@type='password']")))
                    .sendKeys("123456");

            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Đăng nhập') or @type='submit']")))
                    .click();

            // ===== VÀO MENU LỚP HỌC (GIỮ NGUYÊN XPATH) =====
            Thread.sleep(2000);
            driver.findElement(By.xpath("/html/body/div/div[1]/div[1]/nav/div[4]/div[1]"))
                    .click();
            Thread.sleep(2000);

            // ===== ĐỢI BẢNG LOAD =====
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//table//tbody//tr")));

            // ===== ĐỌC EXCEL =====
            FileInputStream fis = new FileInputStream(excelPath);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            int totalRow = sheet.getLastRowNum();

            for (int i = 1; i <= totalRow; i++) {

                Row row = sheet.getRow(i);
                String maLop = row.getCell(0).getStringCellValue().trim();
                String tenLop = row.getCell(1).getStringCellValue().trim();

                // ===== KIỂM TRA LỚP CÓ TỒN TẠI =====
                List<WebElement> rows = driver.findElements(
                        By.xpath("//tr[td[normalize-space()='" + maLop + "']]")
                );

                if (rows.isEmpty()) {
                    callback.onRowResult(i, maLop, tenLop, "❌ Không tìm thấy lớp");
                    callback.onProgress(i * 100 / totalRow);
                    continue;
                }

                // ===== CLICK ICON XÓA (GIỮ NGUYÊN XPATH) =====
                WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//tr[td[normalize-space()='" + maLop +
                                "']]//*[name()='svg' and contains(@class,'trash')]/ancestor::button")
                ));
                deleteBtn.click();

                // ===== HANDLE JS CONFIRM POPUP (SỬA LỖI) =====
                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                String alertText = alert.getText(); // nếu muốn log
                alert.accept(); // click OK

                // ===== LẤY MESSAGE KẾT QUẢ =====
                WebElement resultEle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//p")
                ));

                String resultText = resultEle.getText();

                // ===== CALLBACK =====
                callback.onRowResult(i, maLop, tenLop, resultText);
                callback.onProgress(i * 100 / totalRow);

                // ===== ĐỢI TABLE REFRESH =====
                wait.until(ExpectedConditions.invisibilityOf(rows.get(0)));
            }

            workbook.close();
            fis.close();

        } finally {
            driver.quit();
        }
    }
}
