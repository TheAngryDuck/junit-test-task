package com.example.testtask;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.*;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class MainPageTest {
    private WebDriver driver;
    private PrintWriter out;

@BeforeEach public void setUp() throws IOException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //Open file writer for recording task results
        out = new PrintWriter(new FileWriter("TestResults.txt",true));

        //Get page ready for each test
        driver.get("https://www.playtechpeople.com/");
        driver.findElement(By.xpath("//*[@id=\"CybotCookiebotDialogBodyButtonDecline\"]")).click();
    }

@AfterEach public void tearDown() {
        //Close browser and file writer after each test
        out.println(" ");
        out.close();
        driver.quit();
    }

    @Test
    public void locations() {
        out.println("==LOCATIONS==");

        //Open the locations section
        driver.findElement(By.xpath("//*[@id=\"menu-item-82\"]/a")).click();

        //List out all the "locations" in order
        List<WebElement> elements = driver.findElements(By.className("locations-wrap__item"));
        for (int i = 0; i < elements.size() - 1; i++) {
            out.println(elements.get(i).getText());
        }
    }

    @Test
    public void casinoText() {
        out.println("==CASINO TEXT==");

        //Open the "Life at Playtech" section
        driver.findElement(By.xpath("//*[@id=\"menu-item-49\"]/a")).click();

        //Select the "Who we are" option
        driver.findElement(By.xpath("//*[@id=\"menu-item-47\"]/a")).click();

        //Find the Casino products description text
        WebElement casino = driver.findElement(By.cssSelector("#component-4 > div.container > div > div > div.product-cards > div:nth-child(1) > div > p"));
        out.println(casino.getText());
    }

    @Test
    public void jobs() {
        out.println("==JOBS==");

        //Navigate to the "All Jobs" button
        driver.findElement(By.cssSelector("body > div.wrapper > header > div.container > div > div > div.right-header > a")).click();

        //Make a list of all the jobs
        List<WebElement> jobs = driver.findElements(By.className("job-item"));
        Actions actions = new Actions(driver);

        //Set booleans for the Tallinn and Tartu jobs to keep track if we have found one already
        boolean tartuJob = false;
        boolean tallinnJob = false;

        //Open the ads for all the Estonia positions
        for (WebElement job : jobs) {
            if(Objects.equals(job.getAttribute("data-location"), "estonia")){
                actions.moveToElement(job).click().perform();
            }
        }

        //Get a list of all the tabs for navigation
        Object[] windowHandles=driver.getWindowHandles().toArray();

            // Go Through the ads and check for the location
            for (int i = 1; i < windowHandles.length; i++) {
                driver.switchTo().window(String.valueOf(windowHandles[i]));
                String element = driver.findElement(By.className("job-detail")).getText();

                //If the add is for a tartu position and if we haven't found a tartu position already then save to file
                if(element.contains("Tartu") && !tartuJob){
                    out.println("tartu job");
                    out.println(driver.getCurrentUrl());
                    tartuJob = true;

                //If the add is for a tallinn position and if we haven't found a tallinn position already then save to file
                }else if(element.contains("Tallinn") && !tallinnJob) {
                    out.println("tallinn job");
                    out.println(driver.getCurrentUrl());
                    tallinnJob = true;
                }
            }
    }
}
