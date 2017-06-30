package konferencja;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import org.testng.annotations.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;


public class AppTest
{
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyMMdd_HHmmss");
	/*private static final String DATE = DATE_FORMAT.format(new Date());
	private static final String LOGIN_USER = "uLogin" + DATE;
	private static final String PASS_USER = "uPass" + DATE;
	private static final String NAME_USER = "uName" + DATE;
	private static final String DEGREE_USER = "uDegree" + DATE;
	private static final String MAIL_USER = "uMail" + DATE;
	private static final String ARTICLE_NAME = "aName" + DATE;
	private static final String ARTICLE_CATEGORY = "aCategory" + DATE;*/

	WebDriver driver;
	
	WebDriverWait wait;
	
	WebElement submitInput;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
    	wait = new WebDriverWait(driver, 10);
    }
    
    @BeforeClass
    public void beforeClass(){
    	System.setProperty("webdriver.chrome.driver", "chromedriver.exe"); // path to chromedriver	
    	driver = new ChromeDriver();	//starts Chrome
    }

    @Test(description = "Creation of users and articles", priority = 1)
	public void addUsersArticles()
    {
    	String name = DATE_FORMAT.format(new Date());
    	addUser(name);
    	addArticle("Art1" + name);
    	addArticle("Art2" + name);
    	name = DATE_FORMAT.format(new Date());
    	addUser(name);
    	addArticle("Art1" + name);
    	addArticle("Art2" + name);
    	logout();	
    }
    
    @Test(description = "Adding reviewers", priority = 2)
    public void addReviewers(){
    	loginAsUser("admin", "admin", "administrator");
    	addReviewer();
    }
    
    public void addUser(String name){
    	openPage();
    	clickLink(By.cssSelector("a[href='registration.php']"));
    	createUser(name);
    	loginAsUser("uLogin" + name, "uPass" + name, "uName" + name);
    }
    
    public void openPage(){
    	driver.get("http://127.0.0.1/konferencja");
    	assertThat(driver.getTitle(), startsWith("Konferencja"));
    }
    
    private void createUser(String name){
    	setInput("LOGIN", "uLogin" + name);
    	setInput("PASSWORD", "uPass" + name);
    	setInput("NAME", "uName" + name);
    	setInput("DEGREE", "uDegree" + name);
    	setInput("E_MAIL", "uMail" + name);
    	submit();
    	wait.until(ExpectedConditions.textToBePresentInElement(By.xpath("/html/body/center"), "Nowe konto użytkownika zostało dodane"));
    }
    
    private void loginAsUser(String userLogin, String userPassword, String userName){
    	wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='login']")));
    	setInput("login", userLogin);
    	setInput("pass", userPassword);
    	submit();
    	By byComment = By.xpath("/html/body/center");
    	wait.until(ExpectedConditions.textToBePresentInElement(byComment, "Zalogowany jako "));
    	wait.until(ExpectedConditions.textToBePresentInElement(byComment, userName));
    }
    
    public void addArticle(String name){
    	clickLink(By.cssSelector("a[href='newArticleForm.php']"));
    	setInput("ARTICLE_NAME", "aName" + name);
    	setInput("CATEGORY_NAME", "aCategory" + name);
    	submit();
    	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[text()='" + "aName" + name + "']")));
    	assertThat(driver.getTitle(), startsWith("Konferencja"));
    }
    
    public void addReviewer(){
    	clickLink(By.cssSelector("a[href='addReviewer.php']"));
    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/center/form")));    	
    	String reviewerLogin = driver.findElement(By.xpath("/html/body/center/form")).getAttribute("text()[1]");
    	String reviewerPassword = driver.findElement(By.xpath("/html/body/center/form")).getAttribute("text()[2]");
    	String name = DATE_FORMAT.format(new Date());
    	setInput("NAME", "rName" + name);
    	setInput("DEGREE", "rDegree" + name);
    	setInput("E_MAIL", "rMail" + name);
    	submit();
    }
    
    public void logout(){
    	clickLink(By.cssSelector("a[href='doLogout.php']"));
    	wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='login']")));
    }
    
    private void clickLink(By by){
    	WebElement addArticleLink = driver.findElement(by);
    	wait.until(ExpectedConditions.elementToBeClickable(by));
    	addArticleLink.click();
    }
    
    private void setInput(String inputName, String content){
    	By by = By.cssSelector("input[name='" + inputName + "']");
    	WebDriverWait wait = new WebDriverWait(driver, 10);
    	wait.until(ExpectedConditions.elementToBeClickable(by));
    	WebElement inputField = driver.findElement(by);
    	inputField.click();
    	inputField.clear();
    	inputField.sendKeys(content);
    }
    
    public void submit(){
    	submitInput = driver.findElement(By.cssSelector("input[type='submit']"));
    	submitInput.submit();
    }
    
}
