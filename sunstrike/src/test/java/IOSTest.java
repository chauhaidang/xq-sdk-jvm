import io.appium.java_client.AppiumBy;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.appium.AppiumSelectors.byName;
import static com.codeborne.selenide.appium.SelenideAppium.$;

public class IOSTest extends IOSBaseTest {
    @Test
    void testLoginSuccessWithValidCredentials() {
        $(byName("Login")).click();
        $(AppiumBy.accessibilityId("input-email")).sendKeys("test@webdriver.io");
        $(AppiumBy.accessibilityId("input-password")).setValue("Test1234!");
        $(AppiumBy.accessibilityId("button-LOGIN")).click();
        $(AppiumBy.accessibilityId("Success")).shouldBe(visible);
        $(AppiumBy.accessibilityId("You are logged in!")).shouldBe(visible);
        $(AppiumBy.accessibilityId("OK"))
                .shouldBe(clickable).click();
    }
}

