package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class aTest {

    private static final String faculdade = "FMF | WYDEN";
    private static final String username = "";
    private static final String password = "";
    private static final String graduacao = "Segurança da Informação";
    private static final String data = "18/03/2020";

    enum Curso {

        C1("Estudos de Caso em Carreira e Empreendedorismo", "Profº Edilon Mendes"),
        C2("Informática e Sociedade", "Professor Jean Turet"),
        C3("Temas Tecnológicos em Humanidades", "Professor Lucas Azambuja"),
        C4("Carreira, Liderança e Trabalho em Equipe", "Profª. Floriana Aguiar"),
        C5("Algoritmos Computacionais", "Professora Katia Nelles"),
        C6("Cálculo", "Professor Jean Turet"),
        C7("Empreendedorismo", "Professor Isaac Albagli");

        private String nome;
        private String prof;

        Curso(final String nome, final String prof) {
            this.nome = nome;
            this.prof = prof;
        }

        public String getNome() {
            return nome;
        }

        public String getProf() {
            return prof;
        }

    }

    private WebDriver driver;
    private ArrayList<WebElement> msg;
    private List<WebElement> msgs;

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "D:\\Documentos\\drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        msg = new ArrayList();
    }

    @Test
    public void testEntrarNoIntegrees() {
        driver.get("http://rematricula.integrees.net.s3-website-sa-east-1.amazonaws.com/rematricula.html");

        JavascriptExecutor ex = (JavascriptExecutor) driver;

        //login
        driver.findElement(By.linkText(faculdade)).click();
        WebElement loginForm = driver.findElement(By.id("loginForm"));
        loginForm.findElement(By.id("username")).sendKeys(username);
        loginForm.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();
        driver.get("http://rematricula.integrees.net.s3-website-sa-east-1.amazonaws.com/rematricula.html");

        driver.findElement(By.linkText(faculdade)).click();

        driver.findElement(By.xpath("//img[@alt=\"Turmas\"]")).click();

        driver.get("http://rematricula.integrees.net.s3-website-sa-east-1.amazonaws.com/rematricula.html");

        driver.findElement(By.linkText(faculdade)).click();
        WebElement loginForm2 = driver.findElement(By.id("loginForm"));
        loginForm2.findElement(By.id("username")).sendKeys(username);
        loginForm2.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();

        driver.findElement(By.xpath("//img[@alt=\"Turmas\"]")).click();

        //acessa cursos
        WebDriverWait time = new WebDriverWait(driver, 15);
        Curso[] cursos = Curso.values();
        for (Curso c : cursos) {

            time.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.linkText(c.getNome()))));
            //clica na turma determinada
            driver.findElement(By.linkText(c.getNome())).click();

            //clica em fórum
            ex.executeScript("arguments[0].click();", driver.findElement(By.xpath("//a[@ng-click=\"goCourseForums(turma)\"]"))); //sem precisar rolar pagina

            time.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.partialLinkText(graduacao))));
            //clica no nome do fórum
            driver.findElement(By.partialLinkText(graduacao)).click();

            //recupera msgs
            msgs = driver.findElements(By.xpath("//*[contains(text(), \"" + c.getProf() + "\")]/../../following-sibling::div/p[contains(text(), \"Postado em " + data + "\")]"));
            System.out.println("//*[contains(text(), \"" + c.getProf() + "\")]/../../following-sibling::div/p[contains(text(), \"Postado em " + data + "\")]");

            System.out.println(msgs.size());

//            for (int i = 0; i < msgs.size(); i ++) {
//                System.out.println (msgs.get (i) .getAttribute ("value"));
//            }

            //volta pra turmas
            ex.executeScript("arguments[0].click();", driver.findElement(By.xpath("//a[@href=\"#/courses\"][text()=\"Turmas\"]")));
        }

        //indicar se tem mensagens - nvl 1
        if (!msgs.isEmpty()) {
            System.out.println("Tem mensagem da data " + data);
        } else {
            System.out.println("Não há mensagem da data " + data);
        }

    }

    @After
    public void tearDown() {
        //browser.quit();
    }

}
