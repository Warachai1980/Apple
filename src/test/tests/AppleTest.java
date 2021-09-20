package tests;

import base.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.ApplePage;
import java.util.List;

public class AppleTest extends BaseTest {
    ApplePage applePage;

    @BeforeMethod
    public void setUp(){
        applePage = new ApplePage(driver);
    }

    @Test(testName = "Verify  iphone")
    public void test01(){
        extentTest.assignAuthor("Games");
        extentTest.assignDevice("MAC OS");

        applePage.highlightClick(applePage.iPhoneBtn);

        String actual = "iPhone - Apple";
        Assert.assertEquals(actual,driver.getTitle());
        logScreeenshot("iphone");
    }
}
