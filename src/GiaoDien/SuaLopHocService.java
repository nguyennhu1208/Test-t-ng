package GiaoDien;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

public class SuaLopHocService {

    // ===== CALLBACK =====
    public interface LopHocCallback {
        void onRowResult(int stt, String maLop, String tenMoi, String result);
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
                    By.xpath("//button[@type='submit']")))
                    .click();

            // ===== MENU LỚP HỌC =====
            Thread.sleep(2000);
            driver.findElement(By.xpath(
                    "/html/body/div/div[1]/div[1]/nav/div[4]/div[1]")).click();

            // ===== ĐỢI BẢNG =====
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//table//tbody//tr")));

            // ===== EXCEL =====
            FileInputStream fis = new FileInputStream(excelPath);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            int totalRow = sheet.getLastRowNum();

            for (int i = 1; i <= totalRow; i++) {

                Row row = sheet.getRow(i);
                String maLop = row.getCell(0).getStringCellValue().trim();
                String tenMoi = row.getCell(1).getStringCellValue().trim();

                // ===== CHECK TỒN TẠI (GIỐNG XÓA) =====
                List<WebElement> rows = driver.findElements(
                        By.xpath("//tr[td[normalize-space()='" + maLop + "']]"));

                if (rows.isEmpty()) {
                    callback.onRowResult(i, maLop, tenMoi, "❌ Không tìm thấy lớp");
                    callback.onProgress(i * 100 / totalRow);
                    continue;
                }

                // ===== CLICK ICON SỬA =====
                WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//tr[td[normalize-space()='" + maLop + "']]//td[last()]//button[1]")
                ));
                editBtn.click();

                // ===== ĐỢI MODAL =====
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("/html/body/div[2]")));

                // ===== INPUT TÊN LỚP (XPATH TUYỆT ĐỐI – GIỐNG THÊM) =====
                WebElement txtTen = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("/html/body/div/div[1]/div[2]/div[2]/div/div[2]/div[2]/div/input")
                ));

                txtTen.sendKeys(Keys.CONTROL + "a");
                txtTen.sendKeys(Keys.DELETE);
                txtTen.sendKeys(tenMoi);

                // ===== CLICK CẬP NHẬT =====
                WebElement btnUpdate = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("/html/body/div/div[1]/div[2]/div[2]/div/div[3]/button[2]")
                ));
                btnUpdate.click();

                // ===== TOAST =====
                WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//p")));
                String resultText = toast.getText(); 
                // ===== CALLBACK =====
                callback.onRowResult(i, maLop, tenMoi, resultText);
                callback.onProgress(i * 100 / totalRow);
                // ===== QUAY LẠI MENU LỚP HỌC =====
                wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("/html/body/div/div[1]/div[1]/nav/div[4]/div[1]")
                )).click();

                // ===== ĐỢI BẢNG LOAD LẠI =====
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//table//tbody//tr")));

            }

            workbook.close();
            fis.close();

        } finally {
            driver.quit();
        }
    }
}
