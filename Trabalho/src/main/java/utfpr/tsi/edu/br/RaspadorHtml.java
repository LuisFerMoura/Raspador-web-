package utfpr.tsi.edu.br;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utfpr.tsi.edu.br.models.MedicoDocument;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RaspadorHtml {

    public List<MedicoDocument> buscarMedicos(String uf) {
        ChromeOptions options = new ChromeOptions();

        //options.addArguments("--headless=new");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");

        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        WebDriver driver = new ChromeDriver(options);
        List<MedicoDocument> medicos = new ArrayList<>();

        try {
            ((JavascriptExecutor) driver).executeScript(
                    "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
            );

            driver.get("https://portal.cfm.org.br/busca-medicos");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            WebElement ufDropdownElement = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("uf"))
            );

            Select ufSelect = new Select(ufDropdownElement);
            ufSelect.selectByValue(uf);

            WebElement formDaBusca = ufDropdownElement.findElement(By.xpath("./ancestor::form"));
            formDaBusca.submit();

            int paginaAtual = 1;
            boolean temProximaPagina = true;

            while (temProximaPagina && paginaAtual < 4) {

                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading")));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".resultado-item")));

                List<WebElement> elementos = driver.findElements(By.cssSelector(".resultado-item"));

                WebElement cardAntigo = elementos.isEmpty() ? null : elementos.get(0);

                for (WebElement bloco : elementos) {
                    MedicoDocument m = new MedicoDocument();

                    try {
                        try {
                            m.setNome(bloco.findElement(By.tagName("h4")).getText());
                        } catch (Exception ignored) {}

                        try {
                            m.setCrm(bloco.findElement(By.xpath(".//b[contains(text(),'CRM:')]/parent::div")).getText());
                        } catch (Exception ignored) {}

                        try {
                            m.setDataInscricao(bloco.findElement(By.xpath(".//b[contains(text(),'Data de Inscrição:')]/parent::div")).getText());
                        } catch (Exception ignored) {}

                        try {
                            m.setPrimeiraInscricaoUf(bloco.findElement(By.xpath(".//b[contains(text(),'Primeira inscrição na UF:')]/parent::div")).getText());
                        } catch (Exception ignored) {}

                        try {
                            m.setInscricao(bloco.findElement(By.xpath(".//b[contains(text(),'Inscrição:')]/parent::div")).getText());
                        } catch (Exception ignored) {}

                        try {
                            m.setSituacao(bloco.findElement(By.xpath(".//b[contains(text(),'Situação')]/parent::div")).getText());
                        } catch (Exception ignored) {}

                        try {
                            m.setInscricoesOutroEstado(bloco.findElement(By.xpath(".//b[text()='Inscrições em outro estado:']/parent::div")).getText());
                        } catch (Exception ignored) {}

                        try {
                            List<WebElement> divComEspec = bloco.findElements(By.xpath(".//div[contains(@class, 'espec_')]"));

                            if (!divComEspec.isEmpty()) {
                                m.setEspecialidades(divComEspec.get(0).getAttribute("innerText").trim());
                            } else {
                                List<WebElement> divSemEspec = bloco.findElements(
                                        By.xpath(".//b[contains(text(),'Especialidades')]/ancestor::div[contains(@class,'row')][1]")
                                );

                                if (!divSemEspec.isEmpty()) {
                                    m.setEspecialidades(divSemEspec.get(0).getAttribute("innerText").trim());
                                }
                            }
                        } catch (Exception ignored) {}

                        try {
                            m.setEndereco(bloco.findElement(By.cssSelector(".row.endereco")).getText());
                        } catch (Exception ignored) {}

                        try {
                            m.setTelefone(bloco.findElement(By.cssSelector(".row.telefone")).getText());
                        } catch (Exception ignored) {}

                        try {
                            List<WebElement> divGraduacaoList = bloco.findElements(By.cssSelector(".graduacao_nacional, .graduacao_estrangeira"));

                            if (!divGraduacaoList.isEmpty()) {
                                String textoCompleto = divGraduacaoList.get(0).getAttribute("innerText");

                                if (textoCompleto.contains("Ano de Formatura:")) {
                                    String[] partes = textoCompleto.split("Ano de Formatura:");
                                    m.setInstituicaoGraduacao(partes[0].trim());
                                    if (partes.length > 1) {
                                        m.setAnoFormatura(partes[1].trim());
                                    }
                                } else {
                                    m.setInstituicaoGraduacao(textoCompleto.trim());
                                }
                            }
                        } catch (Exception ignored) {}

                        medicos.add(m);

                    } catch (Exception e) {

                    }
                }

                try {
                    int proximaPagina = paginaAtual + 1;
                    By proximaLocator = By.cssSelector("#paginacao .paginationjs-page[data-num='" + proximaPagina + "'] a");
                    List<WebElement> botoesProxima = driver.findElements(proximaLocator);

                    if (!botoesProxima.isEmpty() && botoesProxima.get(0).isDisplayed()) {
                        WebElement botaoProxima = botoesProxima.get(0);

                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", botaoProxima);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoProxima);

                        paginaAtual++;

                        if (cardAntigo != null) {
                            try {
                                wait.until(ExpectedConditions.stalenessOf(cardAntigo));
                            } catch (Exception ignored) {}
                        }

                    } else {
                        temProximaPagina = false;
                    }

                } catch (Exception e) {
                    temProximaPagina = false;
                }
            }

        } catch (Exception e) {
            System.err.println("Erro na execução do Selenium: " + e.getMessage());
            e.printStackTrace();
        } finally {

                driver.quit();

        }

        return medicos;
    }
}