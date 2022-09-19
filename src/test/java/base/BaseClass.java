package base;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;


//import com_init.SessionInit;
import com_listener.WebDriverListener;
import initpackage.SessionInit;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class BaseClass extends EnvBase {
	
	 public static WebDriver driver;
	 public String browserLang;
	 private EventFiringWebDriver eventHandler;
	 private WebDriverListener ecapture;
	 public static String ABC;
	 
	
	 
	
	  @Parameters({ "browser" })
	  @BeforeMethod
	  public void setDriver(@Optional("chrome") String browser) {
			
			SessionInit.getDriverSession().initiateBrowserSession(browser);
			this.driver = SessionInit.getDriverSession().getBrowserSession();

			this.eventHandler = new EventFiringWebDriver(driver);
			this.ecapture = new WebDriverListener();
			this.eventHandler.register(ecapture);
		}
	  
	  public int getListOflinks() {
		  
		  List<WebElement> links = driver.findElements(By.xpath("//a[@href]"));
		  return links.size();
	  }
		 
		 public void takeScreenshot(String filename) {
			 
			Shutterbug.shootPage(driver, Capture.FULL,true).save("D:\\Selenium_framework\\SS_Send_mail\\screenshots");;
			 
			 //Shutterbug.shootPage(driver, Capture.FULL_SCROLL ,500,true).save();
		   	// Shutterbug.shootPage(driver, Capture.FULL_SCROLL).save();
		    // Shutterbug.shootPage(driver).save();
		    // Shutterbug.shootPage(driver,ScrollStrategy.WHOLE_PAGE,500,true).withName(filename).save();
		  
		  }
//---------------------------------------Take SS and copy that -----------------------------------		 
		 public static void Screenshot(String filename) throws Exception {
			File file =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); 
		 FileUtils.copyFile(file, new File("D:\\Automation_project\\SS_file"+filename+".jpg"));
		 System.out.println(filename+".jpg");
		 ABC =filename;
		 
		 }
		 
//---------------------------------------start Sendmail with SS method-----------------------------------
		 
		 public static void sendAttach(String message, String subject, String to, String from) {

				//Variable for gmail
				String host="smtp.gmail.com";
				
				//get the system properties
				Properties properties = System.getProperties();
				//System.out.println("PROPERTIES "+properties);
				
				//setting important information to properties object
				
				//host set
				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port","465");
				properties.put("mail.smtp.ssl.enable","true");
				properties.put("mail.smtp.auth","true");
				//Step 1: to get the session object..
				Session session=Session.getInstance(properties, new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {				
						return new PasswordAuthentication("ddemottest@gmail.com", "cxqfgptvifibqydh");
					}
					});
				session.setDebug(true);
				//Step 2 : compose the message [text,multi media]
				MimeMessage m = new MimeMessage(session);
				try {
				//from email
				m.setFrom(from);
			     //adding recipient to message
				m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				//adding subject to message
				m.setSubject(subject);
			//attachement..
			//file path
			//	String path="C:\\Users\\mona.jha.ININDCRWS\\Desktop\\RWS-Moravia_240W.png";
				String path="D:\\Automation_project\\SS_file" +ABC +".jpg";
			
				MimeMultipart mimeMultipart = new MimeMultipart();
				//text
				//file
				
				MimeBodyPart textMime = new MimeBodyPart();
				
				MimeBodyPart fileMime = new MimeBodyPart();
				
				try {
					
					textMime.setText(message);
					
					File file=new File(path);
					fileMime.attachFile(file);
					
					
					mimeMultipart.addBodyPart(textMime);
					mimeMultipart.addBodyPart(fileMime);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				m.setContent(mimeMultipart);
				//send 
				//Step 3 : send the message using Transport class
				Transport.send(m);
				}catch (Exception e) {
					e.printStackTrace();
				}
				
		 }	 
		 
// ---------------------------------------end Sendmail with SS method-----------------------------------
				// 

				public long unixTimeStamp() {

					Date now = new Date();
					Long longtime = new Long(now.getTime() / 1000);
					return longtime.intValue();
				}
     public  void waitforPageLoad() {
    	
    	  WebDriverWait wait = new WebDriverWait(driver, 30);
		  
		  wait.until(new ExpectedCondition<Boolean>() { public Boolean apply(WebDriver
		  wdriver) { return ((JavascriptExecutor) driver).executeScript(
		  "return document.readyState" ).equals("complete"); } });  
     	
      } 
     public EventFiringWebDriver getDriver() {
  			return this.eventHandler;
  		}
     
      public String getTitle() {
    	  return driver.getTitle();
      }
      
    
      @AfterMethod
      
   
	
      public void teardown() {
    	driver.quit();
       }
  
	

  	public String getBrowser() {
  	
  		return browserLang;
  	}


}
