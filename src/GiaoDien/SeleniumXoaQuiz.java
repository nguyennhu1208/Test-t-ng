package GiaoDien;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class SeleniumXoaQuiz {

    DefaultTableModel model;
    JTextArea log;
    WebDriver driver;

    public SeleniumXoaQuiz(DefaultTableModel model, JTextArea log) {
        this.model = model;
        this.log = log;
    }

    public void run(List<String> quizzes, String url) {

        new Thread(() -> {
            try {            	
                driver = new ChromeDriver();
                driver.manage().window().maximize();

                /* ===== LOGIN ===== */
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

                /* ===== KIỂM TRA & XÓA ===== */
                for (int i = 0; i < quizzes.size(); i++) {
                    String name = quizzes.get(i);
                    int row = i;

                    log("Kiểm tra: " + name);

                    boolean exists = driver.findElements(
                    		By.xpath(
                    				   "//p[normalize-space()='" + name + "']" +
                    				   "/ancestor::div[contains(@class,'bg-white')]" +
                    				   "//button[contains(@class,'red')]"
                    				)
                    ).size() > 0;

                    if (!exists) {
                        update(row, "Không tồn tại");
                        log("Không tồn tại: " + name);
                        continue;
                    }

                    update(row, "Đang hoạt động");

                    // CLICK XÓA 
                    driver.findElement(
                    	    By.xpath("//button[contains(@class,'bg-red-100') and contains(@class,'text-red-600')]")
                    	).click();

                    Thread.sleep(500);

                    driver.findElement(
                            By.xpath("//button[contains(text(),'Xóa')]")
                    ).click();

                    update(row, "Đã xóa");
                    log("Đã xóa: " + name);

                    Thread.sleep(800);
                }

                driver.quit();
                log("Hoàn tất!");

            } catch (Exception e) {
                log("Lỗi: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    void update(int row, String status) {
        SwingUtilities.invokeLater(() ->
                model.setValueAt(status, row, 2)
        );
    }

    void log(String msg) {
        SwingUtilities.invokeLater(() ->
                log.append("> " + msg + "\n")
        );
    }
}
