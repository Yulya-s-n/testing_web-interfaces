package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class DebitFormTest {

    private WebDriver driver;

    @BeforeAll
    static void setupAll() {
        System.setProperty("webdriver.chrome.driver", "./driver/win/chromedriver.exe");
    }


        @BeforeEach
        void setup () {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--headless");
            driver = new ChromeDriver(options);
        }

        @AfterEach
        void teardown () {
            driver.quit();
            driver = null;
        }

        @Test
        void testValidData () {
            driver.get("http://localhost:9999");
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Виктория");
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79095654235");
            driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
            driver.findElement(By.className("button__text")).click();

            String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
            String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();

            assertEquals(expected, actual);
        }

        @Test
        void testInvalidName () {
            driver.get("http://localhost:9999");
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Viktoria Velikaya");
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79056788787");
            driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
            driver.findElement(By.className("button__text")).click();

            String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
            String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();

            assertEquals(expected, actual);
        }

        @Test
        void testInvalidNumber () {
            driver.get("http://localhost:9999");
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Екатерина Славная-Преславная");
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+790998778987654");
            driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
            driver.findElement(By.className("button__text")).click();

            String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
            String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();

            assertEquals(expected, actual);
        }

        @Test
        void testValidNameWithNoPhone () {
            driver.get("http://localhost:9999");
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Екатерина Славная-Преславная");
            driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
            driver.findElement(By.className("button__text")).click();

            String expected = "Поле обязательно для заполнения";
            String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();

            assertEquals(expected, actual);
        }

        @Test
        void testValidPhoneWithNoName () {
            driver.get("http://localhost:9999");
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79056788787");
            driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
            driver.findElement(By.className("button__text")).click();

            String expected = "Поле обязательно для заполнения";
            String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();

            assertEquals(expected, actual);
        }

        @Test
        void testValidDataWithNoCheckbox () {
            driver.get("http://localhost:9999");
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Екатерина Славная-Преславная");
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79059158232");
            driver.findElement(By.className("button__text")).click();

            String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
            String actual = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).getText();

            assertEquals(expected, actual);
        }
    }

