import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Random;

public class MainTest {
    public WebDriver driver;
    public String alphanumericString, emailString, numericString;
    public int Month, Year, Day;
    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        generateRandomValues();
    }
    private void generateRandomValues() {
        alphanumericString = RandomStringUtils.randomAlphanumeric(8);
        emailString = alphanumericString + "@mail.com";
        numericString = RandomStringUtils.randomNumeric(10);
        Month = new Random().nextInt(12);
        Year = new Random().nextInt(201) + 1900;
        Day = new Random().nextInt(28) + 1;
    }
    @Test
    public void test() {
        driver.get("https://demoqa.com/automation-practice-form");

        WebElement firstName = driver.findElement(By.id("firstName"));
        firstName.sendKeys(alphanumericString);

        WebElement lastName = driver.findElement(By.xpath("//*[@id=\"lastName\"]"));
        lastName.sendKeys(alphanumericString);

        WebElement userEmail = driver.findElement(By.id("userEmail"));
        userEmail.sendKeys(emailString);

        WebElement gender = driver.findElement(By.id("gender-radio-1"));
        Actions actions = new Actions(driver);
        actions.moveToElement(gender).click().build().perform();

        WebElement userNumber = driver.findElement(By.id("userNumber"));
        userNumber.sendKeys(numericString);

        WebElement dateOfBirth = driver.findElement(By.id("dateOfBirthInput"));
        dateOfBirth.click();

        Select drpMonth = new Select(driver.findElement(By.xpath("//*[@class=\"react-datepicker__month-select\"]")));
        drpMonth.selectByValue(String.format("%s", Month));
        String monthString = drpMonth.getFirstSelectedOption().getText();

        Select drpYear = new Select(driver.findElement(By.xpath("//*[@class=\"react-datepicker__year-select\"]")));
        drpYear.selectByValue(String.format("%s", Year));

        String dayPath = String.format("//div[contains(@class, 'datepicker__day--%03d')]", Day);
        WebElement day = driver.findElement(By.xpath(dayPath));
        day.click();

        /*В задании сказано про произвольную строку, однако этот элемент принимает только конкретные значения,
            при вводе неверного он просто сотрет его содержимое при смене фокуса*/
        WebElement subjects = driver.findElement(By.cssSelector("input#subjectsInput"));
        subjects.sendKeys(alphanumericString);

        WebElement picture = driver.findElement(By.id("uploadPicture"));
        String picturePath = System.getProperty("user.dir") + "\\src\\main\\resources\\image.jpg";
        picture.sendKeys(picturePath);

        WebElement currentAddress = driver.findElement(By.id("currentAddress"));
        currentAddress.sendKeys(alphanumericString);

        WebElement state = driver.findElement(By.id("state"));
        state.click();
        Actions keyDown = new Actions(driver);
        keyDown.sendKeys(Keys.DOWN, Keys.ENTER).perform();
        String stateString = driver.findElement(By.xpath("//*[@id=\"state\"]/div/div[1]/div[1]")).getText();

        WebElement city = driver.findElement(By.id("city"));
        city.click();
        keyDown.sendKeys(Keys.DOWN, Keys.ENTER).perform();
        String cityString = driver.findElement(By.xpath("//*[@id=\"city\"]/div/div[1]/div[1]")).getText();

        driver.findElement(By.id("submit")).sendKeys(Keys.ENTER);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("example-modal-sizes-title-lg")));

        Assert.assertEquals("Thanks for submitting the form", driver.findElement(By.id("example-modal-sizes-title-lg")).getText());

        String studentName = alphanumericString + ' ' + alphanumericString;
        Assert.assertEquals(studentName, driver.findElement(By.xpath("/html/body/div[4]/div/div/div[2]/div/table/tbody/tr[1]/td[2]")).getText());

        Assert.assertEquals(emailString, driver.findElement(By.xpath("/html/body/div[4]/div/div/div[2]/div/table/tbody/tr[2]/td[2]")).getText());

        String genderString = driver.findElement(By.xpath("//*[@id=\"genterWrapper\"]/div[2]/div[1]/label")).getText();
        Assert.assertEquals(genderString, driver.findElement(By.xpath("/html/body/div[4]/div/div/div[2]/div/table/tbody/tr[3]/td[2]")).getText());

        Assert.assertEquals(numericString, driver.findElement(By.xpath("/html/body/div[4]/div/div/div[2]/div/table/tbody/tr[4]/td[2]")).getText());

        String dayString = String.format("%02d", Day);
        String yearString = String.format("%s", Year);
        String dob = dayString + ' ' + monthString + ',' + yearString;
        Assert.assertEquals(dob, driver.findElement(By.xpath("/html/body/div[4]/div/div/div[2]/div/table/tbody/tr[5]/td[2]")).getText());

        //Так как Subjects заполнен неверно, строка ниже всегда будет выдавать ошибку (если чудом не зарандомит подходящий вариант, конечно)
        //Assert.assertEquals(alphanumericString, driver.findElement(By.xpath("/html/body/div[4]/div/div/div[2]/div/table/tbody/tr[6]/td[2]")).getText());

        Assert.assertEquals("image.jpg", driver.findElement(By.xpath("/html/body/div[4]/div/div/div[2]/div/table/tbody/tr[8]/td[2]")).getText());

        Assert.assertEquals(alphanumericString, driver.findElement(By.xpath("/html/body/div[4]/div/div/div[2]/div/table/tbody/tr[9]/td[2]")).getText());

        String stateAndCity = stateString + ' ' + cityString;
        Assert.assertEquals(stateAndCity, driver.findElement(By.xpath("/html/body/div[4]/div/div/div[2]/div/table/tbody/tr[10]/td[2]")).getText());
    }
    @After
    public void tearDown() {
        driver.quit();
    }
}