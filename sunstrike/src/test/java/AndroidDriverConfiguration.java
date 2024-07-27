import com.codeborne.selenide.WebDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
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
public class AndroidDriverConfiguration implements WebDriverProvider {

    @Nonnull
    @Override
    @CheckReturnValue
    public WebDriver createDriver(Capabilities capabilities) {
        File app = new File(System.getProperty("user.dir") + "/src/test/resources/android-app.apk");
        UiAutomator2Options options = new UiAutomator2Options();
        options.merge(capabilities);
        options.setApp(app.getAbsolutePath());
        options.setPlatformName("Android");
        options.setPlatformVersion("11.0");
        options.setDeviceName("emulator-5554");
        options.setOrientation(ScreenOrientation.PORTRAIT);
        options.setAutomationName("UiAutomator2");
        options.setAppWaitActivity("com.wdiodemoapp.MainActivity");
        options.setNewCommandTimeout(Duration.ofSeconds(240));

        try {
           return new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
