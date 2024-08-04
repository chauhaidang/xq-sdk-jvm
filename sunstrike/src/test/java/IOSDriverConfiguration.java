import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

@ParametersAreNonnullByDefault
public class IOSDriverConfiguration implements WebDriverProvider {

    @Nonnull
    @Override
    @CheckReturnValue
    public WebDriver createDriver(Capabilities capabilities) {
        File app = new File(System.getProperty("user.dir") + "/src/test/resources/ios-app.app");
        XCUITestOptions options = new XCUITestOptions();
        options.merge(capabilities);
        options.setApp(app.getAbsolutePath());
        options.setOrientation(ScreenOrientation.PORTRAIT);
        options.setAutomationName("XCUITest");
        options.setPlatformName("iOS");
        options.setUdid("CA0526CA-D52C-484B-BAF7-44805924B2BB");
        options.setNewCommandTimeout(Duration.ofSeconds(30));

        try {
           return new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
