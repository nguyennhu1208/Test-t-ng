package GiaoDien;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class DropdownService {

    public static List<String> getDropdownData(String url) {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);

        List<String> result = new ArrayList<>();

        try {
            WebElement dropdownElement = driver.findElement(By.xpath("//select"));
            Select dropdown = new Select(dropdownElement);

            for (WebElement option : dropdown.getOptions()) {
                result.add(option.getText().trim());
            }
        } finally {
            driver.quit();
        }

        return result;
    }
}
