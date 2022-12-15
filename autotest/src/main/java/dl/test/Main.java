// Зиёев Рустам - тестовое задание в Лигу Цифровой Экономики

package dl.test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        // Инициализация драйвера
        WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        driver.manage().window().maximize();

        try {

            // Открытие браузера на странице ya.ru
            driver.get("https://ya.ru/");

            // Клик по группе иконок на стартовой странице ya.ru
            WebElement element = driver.findElement(By.xpath("//a[@title = \"Все сервисы\"]"));
            element.click();

            // Клик на маркет
            element = driver.findElement(By.xpath("//a[@data-statlog=\"services_pinned.more_popup.item.market\"]"));
            element.click();

            // Переход на вкладку с маркетом
            ArrayList<String> allTabs = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(allTabs.get(1));

            // Кнопка "Каталог" -> Категория "Электроника" -> Подкатегория "Смартфоны"
            element = driver.findElement(By.xpath("//*[@id=\"catalogPopupButton\"]"));
            element.click(); // Клик по каталогу

            Thread.sleep(2000);


            Actions chooseCategory = new Actions(driver);
            chooseCategory
                    .moveToElement(driver.findElement(By.linkText("Электроника")))
                    .perform(); // Клик по категории

            Thread.sleep(2000);


            Actions chooseSmartphones = new Actions(driver);
            chooseSmartphones
                    .moveToElement(driver.findElement(By.xpath("//*[@href=\"/catalog--smartfony/26893750/list?hid=91491&glfilter=21194330%3A34066443\"]")))
                    .click()
                    .perform(); // Клик по подкатегории

            Thread.sleep(5000);


            // Переход на "Все фильтры"
            Actions allFilters = new Actions(driver);
            allFilters
                    .moveToElement(driver.findElement(By.linkText("Все фильтры")))
                    .click()
                    .perform();

            Thread.sleep(2000);


            // Ввод верхней границы стоимости 20 000 руб
            Actions Filters = new Actions(driver);
            Filters
                    .moveToElement(driver.findElement(By.xpath("//input[@placeholder=\"299 900\"]")))
                    .click()
                    .sendKeys("20000")
                    .perform();

            Thread.sleep(5000);


            // Выбор диагонали от трёх дюймов
            element = driver.findElement(By.xpath("/html/body/div[4]/section/div[2]/div/div/div[2]/div[1]/div[15]/div"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            Thread.sleep(2000);
            executor.executeScript("arguments[0].scrollIntoView(true);", element); // Скролл до блока с выбором диагонали
            element.click();

            Thread.sleep(5000);

            Filters
                    .moveToElement(driver.findElement(By.xpath("//input[@placeholder = \"1.77\"]")))
                    .click()
                    .sendKeys("3")
                    .build()
                    .perform(); // Ввод необходимого значения

            Thread.sleep(2000);


            // Скролл до производителей и выбор пяти популярных
            element = driver.findElement(By.xpath("/html/body/div[4]/section/div[2]/div/div/div[2]/div[1]/div[10]/div/div"));
            executor.executeScript("arguments[0].scrollIntoView(true);", element);

            Thread.sleep(2000);

            Filters
                    .moveToElement(driver.findElement(By.xpath("//input[@value=\"Apple\"]")))
                    .click()
                    .moveToElement(driver.findElement(By.xpath("//input[@value=\"HUAWEI\"]")))
                    .click()
                    .moveToElement(driver.findElement(By.xpath("//input[@value=\"HONOR\"]")))
                    .click()
                    .moveToElement(driver.findElement(By.xpath("//input[@value=\"Samsung\"]")))
                    .click()
                    .moveToElement(driver.findElement(By.xpath("//input[@value=\"Xiaomi\"]")))
                    .click()
                    .perform(); // Клик пяти по чек-блокам

            driver.findElement(By.xpath("//a[@data-auto=\"result-filters-link\"]")).click(); // Клик по кнопке "Показать предложения" с применёнными фильтрами

            // Скролл до футера для обновления ДОМа, следом заполнение массива позициями на странице
            executor.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@data-auto=\"pager-more\"]")));
            List<WebElement> elements = driver.findElements(By.xpath("//article"));

            // Проверка отображения именно 10 элементов
            if (elements.size() != 10) {
                System.out.println(">> Внимание! Количество элементов на странице больше 10!");
            } else {
                System.out.println(">> Количество элементов на странице равно 10");
            }

            // Сохранение первой позиции в поиске
            WebElement RequiredPhone = driver.findElement(By.xpath("//article[1]/div[3]/div/h3/a[@title]"));
            String SAVE = RequiredPhone.getText();

            // Клик для перехода на другую сортировку: по рейтингу
            driver.findElement(By.xpath("//*[@id=\"serpTop\"]/div/div/div[1]/div/div/noindex/div/button[3]")).click();

            // Цикл поиска
            boolean flag = false; // Флажок остановки поиска
            WebElement searchResult = null; // Переменная для сохранения нужного телефона
            while (flag == false) {
                Thread.sleep(3000); // Пауза для прогрузки ДОМа
                executor.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@data-auto=\"pager-more\"]"))); // Скролл для футера для заполнения ДОМа
                elements = driver.findElements(By.xpath("//article/div[3]/div/h3/a[@title]")); // Сохранение всех позиций на странице в лист
                for (WebElement snippet : elements) {                       // Прогонка каждого элемента в листе на признак соответствия необходмой модели телефона
                    if (snippet.getText().equals(SAVE)) {
                        searchResult = snippet;
                        flag = true;                                        // Остановка поиска
                    }
                }
                if (flag == false) {                                         // Дополнительная проверка для избежания ошибки пустого ДОМа при переходе на новую вкладку
                    driver.findElement(By.xpath("//div[@data-baobab-name=\"next\"]")).click(); // Переход на следующую страницу в случае если на данной телефон не нашёлся
                }
            }
            searchResult.click(); // Клик по карточке необходимого телефона

            // Переход на открывшуюся страницу с телефоном
            allTabs = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(allTabs.get(2));

            // Вывод рейтинга
            String rate = driver.findElement(By.xpath("//div[@class=\"_1EOgH _2I6wc _1NfPD\"]/span[@class=\"_2v4E8\"]")).getAttribute("innerHTML");
            System.out.println("Рейтинг телефона " + SAVE + " составляет : " + rate);

        } catch (InterruptedException NoSuchElementException) {

            System.out.println("Телефон не был найден");
            throw new RuntimeException(NoSuchElementException);

        } finally {
            driver.quit();
        }
    }
}