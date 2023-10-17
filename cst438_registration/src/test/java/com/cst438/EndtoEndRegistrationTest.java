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

    public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32/chromedriver.exe";
    public static final String URL = "http://localhost:3000";
    public static final int TEST_STUDENT_ID = 12345;
    public static final int SLEEP_DURATION = 1000;

    @Test
    public void addStudentTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);

            List<WebElement> weList = driver.findElements(By.xpath("//input"));

            weList.get(2).click();

            // Locate and click "View Students" button.
            driver.findElement(By.id("viewStudents")).click();
            Thread.sleep(SLEEP_DURATION);

            // Locate and click "Add Student" button, which is the first and only button on the page.
            driver.findElement(By.id("addStudent")).click();
            Thread.sleep(SLEEP_DURATION);

            // Enter student ID and click the "Add" button.
            driver.findElement(By.id("studentId")).sendKeys(Integer.toString(TEST_STUDENT_ID));
            driver.findElement(By.id("add")).click();
            Thread.sleep(SLEEP_DURATION);

            // Verify that the new student is registered by searching for their ID in the updated list.
            WebElement studentRow = driver.findElement(By.xpath("//tr[td='" + TEST_STUDENT_ID + "']"));
            assertNotNull(studentRow, "Test student not found in the student list.");

            // Drop the student
            WebElement dropButton = studentRow.findElement(By.xpath("//button"));
            assertNotNull(dropButton);
            dropButton.click();

            // The drop student action causes an alert to occur.
            WebDriverWait wait = new WebDriverWait(driver, 1);
            wait.until(ExpectedConditions.alertIsPresent());

            Alert simpleAlert = driver.switchTo().alert();
            simpleAlert.accept();

            // Check that the student is no longer in the student list
            Thread.sleep(SLEEP_DURATION);
            assertThrows(NoSuchElementException.class, () -> {
                driver.findElement(By.xpath("//tr[td='" + TEST_STUDENT_ID + "']"));
            });

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

            List<WebElement> weList = driver.findElements(By.xpath("//input"));

            weList.get(2).click();

            // Locate and click "View Students" button.
            driver.findElement(By.id("viewStudents")).click();
            Thread.sleep(SLEEP_DURATION);

            WebElement editButton = driver.findElement(By.xpath("//button[text()='Edit']"));
            assertNotNull(editButton);
            editButton.click();

            // Update student information here (change student name or email).
            driver.findElement(By.id("studentName")).clear();
            driver.findElement(By.id("studentName")).sendKeys("Updated Student Name");
            driver.findElement(By.id("studentEmail")).clear();
            driver.findElement(By.id("studentEmail")).sendKeys("Updated Student Email");

            // Click the "Save" button to save the changes.
            driver.findElement(By.id("save")).click();
            Thread.sleep(SLEEP_DURATION);

            // Verify that the student information has been updated.
            WebElement updatedStudentName = driver.findElement(By.xpath("//tr[td='Updated Student Name']"));
            assertNotNull(updatedStudentName, "Updated student not found in the student list");
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

            List<WebElement> weList = driver.findElements(By.xpath("//input"));

            weList.get(2).click();

            // Locate and click "View Students" button.
            driver.findElement(By.id("viewStudents")).click();
            Thread.sleep(SLEEP_DURATION);

            WebElement deleteButton = driver.findElement(By.xpath("//button[text()='Delete']"));
            assertNotNull(deleteButton);
            deleteButton.click();

            // The delete student action causes an alert to occur.
            WebDriverWait wait = new WebDriverWait(driver, 1);
            wait.until(ExpectedConditions.alertIsPresent());

            Alert confirmationAlert = driver.switchTo().alert();
            confirmationAlert.accept();

            // Check that the student is no longer in the student list
            Thread.sleep(SLEEP_DURATION);
            assertThrows(NoSuchElementException.class, () -> {
                driver.findElement(By.xpath("//tr[td='Updated Student Name']"));
            });

        } catch (Exception ex) {
            throw ex;
        } finally {
            driver.quit();
        }
    }
}
