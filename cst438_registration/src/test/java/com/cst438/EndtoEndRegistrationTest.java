package com.cst438;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;

@SpringBootTest
public class EndtoEndRegistrationTest {

	public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/aureliolopez/Desktop/chromedriver-mac-x64/chromedriver";
    public static final String URL = "http://localhost:3000/admin";
   
    public static final int TEST_STUDENT_ID = 3;
    public static final int SLEEP_DURATION = 1000;

    @Test 
    public void addStudentTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);

            // Locate and click "Add Student" button
            driver.findElement(By.id("addstudent")).click();
            Thread.sleep(SLEEP_DURATION);
           
            driver.findElement(By.id("student_name")).sendKeys(("Alex"));
            driver.findElement(By.id("student_email")).sendKeys(("Alex@csumb.edu"));
            driver.findElement(By.id("add")).click();
            Thread.sleep(SLEEP_DURATION);
                      
            String expectedStudentName = "Alex";
            WebElement studentElement = driver.findElement(By.xpath("//*[text()='" + expectedStudentName + "']"));
            assertNotNull(studentElement, "Student with name 'Alex' should exist");

        } catch (Exception ex) {
            throw ex;
        } finally {
            driver.quit();
        }
    }

    @Test
    public void updateStudentTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);
            
         // Locate the edit button
            driver.findElement(By.id("editbutton")).click();
            Thread.sleep(SLEEP_DURATION);

            WebElement studentNameInput = driver.findElement(By.id("studentName"));            
//            WebElement studentEmailInput = driver.findElement(By.id("studentEmail"));

            ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", studentNameInput);
//            ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", studentEmailInput);

            // Update student name
            studentNameInput.sendKeys("JustTesting"); 
//            studentEmailInput.sendKeys("JustTesting@csumb.edu");
            
            driver.findElement(By.id("updatebutton")).click();
            driver.findElement(By.id("closebutton")).click();
            Thread.sleep(SLEEP_DURATION);
      
        } catch (Exception ex) {
            throw ex;
        } finally {
            driver.quit();
        }
    }

    @Test
    public void deleteStudentTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);

            // Locate and click "Delete" button 
            driver.findElement(By.id("deleteStudent")).click();
            Thread.sleep(SLEEP_DURATION);

            // Confirm the delete action in the alert dialog.
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert confirmationAlert = driver.switchTo().alert();
            confirmationAlert.accept();

            // Check that the assignment is no longer in the assignment list.
            Thread.sleep(SLEEP_DURATION);
            assertThrows(NoSuchElementException.class, () -> {
                driver.findElement(By.xpath("//tr[td='Name deleted']"));
            });


        } catch (Exception ex) {
            throw ex;
        } finally {
            driver.quit();
        }
    }
}
