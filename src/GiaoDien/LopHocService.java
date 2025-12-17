package GiaoDien;

import java.io.FileInputStream;
import java.time.Duration;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

public class LopHocService {

    // ===== CALLBACK =====
    public interface LopHocCallback {
        void onRowResult(int stt, String maLop, String tenLop, String result);
        void onProgress(int percent);
    }

    public static void runTest(String url, String excelPath, LopHocCallback callback) throws Exception {

        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\admin\\Downloads\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get(url);

            driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[1]/input"))
                    .sendKeys("admin@test.psi.plt.com");
            driver.findElement(By.xpath("/html/body/div/div[1]/div/form/div[2]/div/input"))
                    .sendKeys("123456");
            driver.findElement(By.xpath("/html/body/div/div[1]/div/form/button"))
                    .click();

            Thread.sleep(2000);

            driver.findElement(By.xpath("/html/body/div/div[1]/div[1]/nav/div[4]/div[1]"))
                    .click();

            FileInputStream fis = new FileInputStream(excelPath);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            int totalRow = sheet.getLastRowNum();

            for (int i = 1; i <= totalRow; i++) {

                Row row = sheet.getRow(i);
                String maLop = row.getCell(0).getStringCellValue();
                String tenLop = row.getCell(1).getStringCellValue();

                driver.findElement(By.xpath(
                        "/html/body/div/div[1]/div[2]/div[2]/div/div[2]/div[2]/div[2]/button"))
                        .click();

                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("/html/body/div/div[1]/div[2]/div[2]/div/div[2]/div[1]/div/input")))
                        .sendKeys(maLop);

                driver.findElement(By.xpath(
                        "/html/body/div/div[1]/div[2]/div[2]/div/div[2]/div[2]/div/input"))
                        .sendKeys(tenLop);

                driver.findElement(By.xpath(
                        "/html/body/div/div[1]/div[2]/div[2]/div/div[3]/button[2]"))
                        .click();

                Thread.sleep(1500);

                WebElement resultEle = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(By.tagName("p")));
                String resultText = resultEle.getText();

                // ===== CALLBACK =====
                callback.onRowResult(i, maLop, tenLop, resultText);

                int percent = i * 100 / totalRow;
                callback.onProgress(percent);
            }

            workbook.close();
            fis.close();

        } finally {
            driver.quit();
        }
    }
}
