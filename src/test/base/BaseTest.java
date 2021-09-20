package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.Screenshot;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class BaseTest {
    public WebDriver driver;
    private ExtentReports extentReports;
    protected ExtentTest extentTest;
    private String configFilePath = "src/test/data/config.properties";

    @BeforeMethod()
    public void SetUp(Method method) {
        initializeDriver(ConfigReader.readProperty(configFilePath, "browser"));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        extentTest = extentReports.createTest("Verify Test");
        logTestGroups(method);

        driver.get(ConfigReader.readProperty(configFilePath, "url"));
    }

    @AfterMethod()
    public void tearDown() {
        driver.quit();
    }

    @BeforeSuite
    public void startReporter() {
        //initiating extent report
        extentReports = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("reports.html");

        //adding some configurations
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle("My Report");
        spark.config().setReportName("AutomationTLA Tests");
        extentReports.attachReporter(spark);
    }

    @AfterSuite
    public void closeReporter() {
        //closing the extent reporter
        extentReports.flush();
    }

    public void initializeDriver(String browser) {
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "ie":
                WebDriverManager.iedriver().setup();
                driver = new InternetExplorerDriver();
        }
    }
    public void logTestGroups(Method method){
        Test testClass = method.getAnnotation(Test.class);
        for(String e : testClass.groups()){
            extentTest.assignCategory(e);
        }
    }

    public void logScreeenshot(String titlte){
        extentTest.info(titlte,
                MediaEntityBuilder.createScreenCaptureFromBase64String(Screenshot.takeScreenshot(driver)).build());
    }
    public void logScreeenshot(){
        extentTest.info(MediaEntityBuilder.createScreenCaptureFromBase64String(Screenshot.takeScreenshot(driver)).build());
    }
}
