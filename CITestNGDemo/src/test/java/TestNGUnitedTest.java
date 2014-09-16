package test.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

import com.perfectomobile.httpclient.MediaType;
import com.perfectomobile.httpclient.utils.FileUtils;
import com.perfectomobile.selenium.MobileDriver;
import com.perfectomobile.selenium.api.IMobileDevice;
import com.perfectomobile.selenium.api.IMobileDriver;
import com.perfectomobile.selenium.options.MobileDeviceProperty;

public class TestNGUnitedTest {

	String _Device;
	MobileDriver driver;
	@BeforeMethod
	public void beforeMethod() {
	}

	@BeforeTest
	public void beforeTest() {

		String host = Constants.PM_CLOUD;
		String user = Constants.PM_USER;
		String password = Constants.PM_PASSWORD;
		driver = new MobileDriver(host, user, password);
		Reporter.log("Connect to:"+host);
	}

	@AfterTest
	public void afterTest(){
		driver.quit();
		InputStream reportStream = ((IMobileDriver) driver).downloadReport(MediaType.HTML);

		if (reportStream != null) {
			File reportFile = new File(Constants.REPORT_LIB+"TestNG_"+_Device+".HTML");
			FileUtils.write(reportStream, reportFile);
			Reporter.log( Constants.REPORT_LIB+"TestNG_"+_Device+".HTML");

			
			String filename =Constants.REPORT_LIB+"TestNG_"+_Device+".HTML"  ;
			String FileLink ="file:///" + Constants.REPORT_LIB_HTML + filename;

			try {
				BufferedReader br = new BufferedReader(new FileReader(filename));

				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				Reporter.log("</br><DIV>");

				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}
				Reporter.log(sb.toString());

				Reporter.log("</DIV>");

				br.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} 
	}


	@Parameters({ "deviceID" })
	@Test
	public void CheckFlight(String deviceID) {
		_Device = deviceID;
		Reporter.log("device:"+deviceID);
		IMobileDevice device = driver.getDevice(deviceID);
		Reporter.log("device MODEL :"+device.getProperty(MobileDeviceProperty.MODEL));
		Reporter.log("device OS :"+device.getProperty(MobileDeviceProperty.OS));

		String filename ="TestNG_"+_Device+".HTML" ;
		String FileLink =Constants.REPORT_LIB_HTML + filename;
		//String FileLink ="file:///" + Constants.REPORT_LIB_HTML + filename;

		//Reporter.log("</br><b>Report:</b> <a href=" + FileLink + ">Perfecto Mobile Report</a>");
		//Reporter.log("<OBJECT data=\"C:/test/TestNG_D5B3B672.PDF\" TYPE=\"application/x-pdf\" TITLE=\"Perfecto Mobile PDF Report\" WIDTH=200 HEIGHT=100> <a href=\"C:/test/TestNG_D5B3B672.PDF\">Perfecto PDF Report Link</a></object>");
		//Reporter.log("<OBJECT data=\"" + FileLink + "\" width=\"600\" height=\"400\"> <embed src="+ FileLink + " width=\"600\" height=\"400\"> </embed> Error: Embedded data could not be displayed. </object>");
		//Reporter.log("<OBJECT data=\"" + FileLink + "\" TYPE=\"application/html\" TITLE=\"Perfecto Mobile HTML Report\" WIDTH=200 HEIGHT=100> <a href=\"" + FileLink + "\">Perfecto HTML Report Link</a></object>");
		
		
		PerfectoTest t = new PerfectoTest();
		String rc =  t.checkFlights(device);

	
		assert rc.equals("New York/Newark, NJ (EWR)") : "Expected  New York/Newark, NJ (EWR)" + rc;

//file:///C:/Users/tomerg/workspace/CITestNGDemo/test-output/YourFile.pdf
	}

}
