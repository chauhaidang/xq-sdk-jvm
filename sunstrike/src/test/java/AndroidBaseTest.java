import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeMethod;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class AndroidBaseTest {
    @BeforeMethod
    void setUp() {
        closeWebDriver();
        Configuration.browserSize = null;
        Configuration.browser = AndroidDriverConfiguration.class.getName();
        Configuration.timeout = 1000 * 10;
        open();
    }
}
