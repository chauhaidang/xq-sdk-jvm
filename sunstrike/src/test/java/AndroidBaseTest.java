import org.junit.jupiter.api.BeforeEach;
import com.codeborne.selenide.Configuration;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class AndroidBaseTest {
    @BeforeEach
    void setUp() {
        closeWebDriver();
        Configuration.browserSize = null;
        Configuration.browser = AndroidDriverConfiguration.class.getName();
        Configuration.timeout = 1000 * 10;
        open();
    }
}
