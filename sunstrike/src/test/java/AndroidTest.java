import io.appium.java_client.AppiumBy;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.appium.AppiumSelectors.byText;
import static com.codeborne.selenide.appium.SelenideAppium.$;

public class AndroidTest extends AndroidBaseTest {
    @Test
    void testLoginSuccessWithValidCredentials() {
        $(byText("Login")).click();
        $(AppiumBy.accessibilityId("input-email")).setValue("test@webdriver.io");
        $(AppiumBy.accessibilityId("input-password")).setValue("Test1234!");
        $(AppiumBy.accessibilityId("button-LOGIN")).click();
        $(byText("Success")).shouldBe(visible);
        $(byText("You are logged in!")).shouldBe(visible);
        $(byText("OK"))
                .shouldBe(clickable).click();
    }
}
