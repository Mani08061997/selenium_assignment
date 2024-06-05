package com.lambdatest;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import org.asynchttpclient.util.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasicAuthentication {
    public static String hubURL = "https://hub.lambdatest.com/wd/hub";
    private WebDriver driver;

    public void setup() throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "88");
        HashMap<String, Object> ltOptions = new HashMap<String, Object>();
        ltOptions.put("user", System.getenv("LT_USERNAME"));
        ltOptions.put("accessKey", System.getenv("LT_ACCESS_KEY"));
        ltOptions.put("visual", true);
        ltOptions.put("video", true);
        ltOptions.put("resolution", "1920x1080");
        ltOptions.put("network", true);
        ltOptions.put("console", "true");
        ltOptions.put("build", "Selenium 4");
        ltOptions.put("name", this.getClass().getName());
        ltOptions.put("platformName", "Windows 10");
        ltOptions.put("seCdp", true);
        ltOptions.put("selenium_version", "4.0.0");
        capabilities.setCapability("LT:Options", ltOptions);

        driver = new RemoteWebDriver(new URL(hubURL), capabilities);
        System.out.println(driver);
    }

    public void authentication() {
        Augmenter augmenter = new Augmenter();
        driver = augmenter.augment(driver);

        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        driver = augmenter.addDriverAugmentation("chrome", HasAuthentication.class,
                (caps, exec) -> (whenThisMatches, useTheseCredentials) -> devTools.getDomains().network()
                        .addAuthHandler(whenThisMatches, useTheseCredentials))
                .augment(driver);

        ((HasAuthentication) driver).register(UsernameAndPassword.of("foo", "bar"));

        driver.get("https://httpbin.org/basic-auth/foo/bar");

        String text = driver.findElement(By.tagName("body")).getText();
        System.out.println(text);
        if (text.contains("authenticated")) {
            markStatus("passed", "Authentication Successful", driver);
        } else {
            markStatus("failed", "Authentication Failure", driver);
        }

    }

    public void testScenario1() {
        Augmenter augmenter = new Augmenter();
        driver = augmenter.augment(driver);

        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        driver = augmenter.addDriverAugmentation("chrome", HasAuthentication.class,
                (caps, exec) -> (whenThisMatches, useTheseCredentials) -> devTools.getDomains().network()
                        .addAuthHandler(whenThisMatches, useTheseCredentials))
                .augment(driver);
        driver.get("https://www.lambdatest.com/selenium-playground");
        driver.findElement(By.linkText("Simple Form Demo")).click();

        // Validate that the URL contains "simple-form-demo"
        String currentUrl = driver.getCurrentUrl();
        if(currentUrl.contains("simple-form-demo")){
            markStatus("passed", "URL contains \"simple-form-demo\" ", driver);
        }

        // Create a variable for a string value
        String message = "Welcome to LambdaTest";

        // Enter value in the "Enter Message" text box
        WebElement messageInput = driver.findElement(By.id("user-message"));
        messageInput.sendKeys(message);

        // Click "Get Checked Value"
        driver.findElement(By.id("showInput")).click();

        // Validate the displayed message
        WebElement displayedMessage = driver.findElement(By.id("message"));
        if(displayedMessage.getText().equals(message)) {
            markStatus("passed", "display message matches ", driver);
        }
    }

    public void testScenario2() {
        Augmenter augmenter = new Augmenter();
        driver = augmenter.augment(driver);

        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();

        driver = augmenter.addDriverAugmentation("chrome", HasAuthentication.class,
                (caps, exec) -> (whenThisMatches, useTheseCredentials) -> devTools.getDomains().network()
                        .addAuthHandler(whenThisMatches, useTheseCredentials))
                .augment(driver);

        driver.get("https://www.lambdatest.com/selenium-playground");
        driver.findElement(By.linkText("Drag & Drop Sliders")).click();

        // Locate the slider and drag it to the value of 95
        WebElement slider = driver.findElement(By.xpath("//input[@value='15']"));
        WebElement output = driver.findElement(By.id("rangeSuccess"));

        Actions action = new Actions(driver);
        action.dragAndDropBy(slider, 80, 0).perform();

        // Validate the range value shows 95
        if(output.getText().equals("95")) {
            markStatus("passed", "range shows 95 ", driver);
        }
    }

public void testScenario3() {
    Augmenter augmenter = new Augmenter();
    driver = augmenter.augment(driver);

    DevTools devTools = ((HasDevTools) driver).getDevTools();
    devTools.createSession();

    driver = augmenter.addDriverAugmentation("chrome", HasAuthentication.class,
            (caps, exec) -> (whenThisMatches, useTheseCredentials) -> devTools.getDomains().network()
                    .addAuthHandler(whenThisMatches, useTheseCredentials))
            .augment(driver);

    driver.get("https://www.lambdatest.com/selenium-playground");
    driver.findElement(By.linkText("Input Form Submit")).click();

    // // Click "Submit" without filling in any information
    // driver.findElement(By.xpath("//button[text()='Submit']")).click();

    // // Assert the "Please fill in the fields" error message
    // WebElement errorMessage = driver.findElement(By.xpath("//p[contains(text(), 'Please fill in the fields')]"));
    // if(errorMessage.isDisplayed()) {
    //     markStatus("passed", "Error Message Displayed", driver);
    // }
    //add wait
  
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


    // Fill in the form

    WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("name")));
    nameInput.sendKeys("John Doe");
    WebElement emailInput = driver.findElement(By.name("email"));
    emailInput.sendKeys("johndoe@example.com");
    WebElement password = driver.findElement(By.name("password"));
    password.sendKeys("password123");
    WebElement company = driver.findElement(By.name("company"));
    company.sendKeys("LambdaTest");
    WebElement website = driver.findElement(By.name("website"));
    website.sendKeys("https://www.example.com");
    WebElement country = driver.findElement(By.name("country"));
    country.sendKeys("United States");
    WebElement city = driver.findElement(By.name("city"));
    city.sendKeys("San Francisco");
    WebElement address1 = driver.findElement(By.name("address1"));
    address1.sendKeys("123 Main St");
    WebElement address2 = driver.findElement(By.name("address2"));
    address2.sendKeys("Suite 100");
    WebElement state = driver.findElement(By.name("state"));
    state.sendKeys("CA");
    WebElement zip = driver.findElement(By.name("zip"));
    zip.sendKeys("94105");

    // Click "Submit"
    driver.findElement(By.xpath("//button[text()='Submit']")).click();

    // Validate the success message
    WebElement successMessage = driver.findElement(By.xpath("//p[contains(text(), 'Thanks for contacting us, we will get back to you shortly.')]"));
    if(successMessage.isDisplayed()) {
        markStatus("passed", "Success Message Displayed", driver);
    }

}


    public void tearDown() {
        try {
            driver.quit();
        } catch (

        Exception e) {
            markStatus("failed", "Got exception!", driver);
            e.printStackTrace();
            driver.quit();
        }
    }

    public static void markStatus(String status, String reason, WebDriver driver) {
        JavascriptExecutor jsExecute = (JavascriptExecutor) driver;
        jsExecute.executeScript("lambda-status=" + status);
        System.out.println(reason);
    }

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        BasicAuthentication test = new BasicAuthentication();
        test.setup();
        // test.authentication();
        test.testScenario1();
        test.testScenario2();
        // test.testScenario3();
        test.tearDown();
    }
}
