package automation.glue;

import automation.config.AutomationFrameworkConfiguration;
import automation.drivers.DriverSingleton;
import automation.pages.CheckoutPage;
import automation.pages.HomePage;
import automation.pages.SignInPage;
import automation.utils.*;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@CucumberContextConfiguration
@SpringBootTest(classes = AutomationFrameworkConfiguration.class)
//@ContextConfiguration(classes = AutomationFrameworkConfiguration.class)
public class StepDefinition {
    private WebDriver driver;
    private HomePage homePage;
    private SignInPage signInPage;
    private CheckoutPage checkoutPage;
    ExtentTest test;
    static ExtentReports report = new ExtentReports("report/TestReport.html");

    @Autowired
    ConfigurationProperties configurationProperties;

    @Before
    public void initializeObjects(){
        DriverSingleton.getInstance(configurationProperties.getBrowser());
        homePage = new HomePage();
        signInPage = new SignInPage();
        checkoutPage = new CheckoutPage();
        TestCases[] tests = TestCases.values();
        test = report.startTest(tests[Utils.testCount].getTestName());
        Log.getLogData(Log.class.getName());
        Log.startTest(tests[Utils.testCount].getTestName());
        Utils.testCount++;
    }

    @Given("^I go to the Website")
    public void i_go_to_the_Website(){
        driver = DriverSingleton.getDriver();
        driver.get(Constants.URL);
        Log.info("INFO: Navigating to " + Constants.URL);
        test.log(LogStatus.PASS, "Navigating to " + Constants.URL);
    }

    @When("^I click on Sign In button")
    public void i_click_on_sign_in_button(){
        homePage.clickSignIn();
        test.log(LogStatus.PASS, "Sign In button has been clicked.");
    }

    @When("^I add two elements to the cart")
    public void i_add_two_elements_to_the_cart() {
        homePage.addFirstElementToCart();
        homePage.addSecondElementToCart();
        test.log(LogStatus.PASS, "Two elements were added to the cart");
    }

    @And("^I specify my credentials and click Login")
    public void i_specify_my_credentials_and_click_login(){
        signInPage.logIn(configurationProperties.getEmail(), configurationProperties.getPassword());
        test.log(LogStatus.PASS, "Login has been clicked.");
    }

    @And("^I proceed to checkout")
    public void i_proceed_to_checkout(){
        checkoutPage.goToCheckout();
        test.log(LogStatus.PASS, "We proceed to checkout");
    }

    @And("^I confirm address, shipping, payment and final order")
    public void i_confirm_address_shipping_payment_and_final_order(){
        signInPage.logIn(configurationProperties.getEmail(), configurationProperties.getPassword());
        checkoutPage.confirmAddress();
        checkoutPage.confirmShipping();
        checkoutPage.payByBankWire();
        checkoutPage.confirmFinalOrder();
        test.log(LogStatus.PASS, "We confirm the final order");
    }

    @Then("^I can log into the website")
    public void i_can_log_into_the_website(){
        if(configurationProperties.getUsername().equals(homePage.getUserName()))
            test.log(LogStatus.PASS, "The authentication is successful.");
        else
            test.log(LogStatus.FAIL, "Authentication is not successful.");

        assertEquals(configurationProperties.getUsername(), homePage.getUserName());
    }

    @Then("^The elements are bought")
    public void the_elements_are_bought(){
        if(checkoutPage.checkFinalStatus())
            test.log(LogStatus.PASS, "The two items are bought.");
        else
            test.log(LogStatus.FAIL, "The items weren't bought");

        assertTrue(checkoutPage.checkFinalStatus());
    }

    @After
    public void closeObjects(){
        report.endTest(test);
        report.flush();
        DriverSingleton.closeObjInstance();
    }
}
