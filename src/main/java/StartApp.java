
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

public class StartApp {

    private static AndroidDriver driver;
    private AppiumDriverLocalService service;
    private AppiumServiceBuilder builder;
    private DesiredCapabilities capabilities;

    @Test
    public void testAppium() throws MalformedURLException, InterruptedException {
        int port = 4723;
        /*if (!checkIfServerIsRunnning(port)){
            startServer();
            stopServer();
        } else {
            System.out.println("Appium server is already running on port number: " + port);
        }*/

        startServer();
        stopServer();
    }


    public void startServer() throws MalformedURLException, InterruptedException {

        //To install app locally
        /*File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "/src/main/resources/");
        File app = new File(appDir, "vff.apk");
        capabilities.setCapability("app", app.getAbsolutePath());*/

        //Set Capabilities
        capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "pixel_2_xl");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", "com.android.calculator2");
        capabilities.setCapability("appActivity", "com.android.calculator2.MainActivity");

        //Build Appium Service
        builder = new AppiumServiceBuilder();
        builder.usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe")).
                withAppiumJS(new File("C:\\Users\\hozefaa\\AppData\\Local\\Programs\\Appium\\resources\\app\\node_modules\\appium\\build\\lib\\main.js")).
                withLogFile(new File("./target/logs/logs.txt"));
        //builder.withIPAddress("127.0.0.1");
        //builder.usingPort(4723);
        builder.withCapabilities(capabilities);
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
        builder.withCapabilities(capabilities);

        //Start the service with the builder
        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
        Thread.sleep(10000);
        driver.quit();
    }

    public void stopServer() {
        service.stop();
        System.out.println("Server stopped!");
    }

    public boolean checkIfServerIsRunnning(int port) {

        boolean isServerRunning = false;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.close();
        } catch (IOException e) {
            //If control comes here, then it means that the port is in use
            isServerRunning = true;
        } finally {
            serverSocket = null;
        }
        return isServerRunning;
    }

}