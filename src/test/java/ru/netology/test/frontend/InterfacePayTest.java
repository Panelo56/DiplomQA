package ru.netology.test.frontend;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DataBaseHelper;
import ru.netology.page.CardPage;
import ru.netology.page.FormPage;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static org.testng.AssertJUnit.*;

public class InterfacePayTest {
    private static DataHelper.CardData cardData;
    private static CardPage tripCard;
    private static FormPage tripForm;
    private static List<DataBaseHelper.PaymentEntity> payments;
    private static List<DataBaseHelper.CreditRequestEntity> credits;
    private static List<DataBaseHelper.OrderEntity> orders;

    @BeforeClass
    public void setupClass() {
        DataBaseHelper.setDown();
        SelenideLogger.addListener("allure", new AllureSelenide()
                .screenshots(true).savePageSource(true));
    }

    @BeforeMethod
    public void setupMethod() {
        open("http://localhost:8080/");
        tripCard = new CardPage();
    }

    @AfterMethod
    public void setDownMethod() {
        DataBaseHelper.setDown();
    }

    @AfterClass
    public void setDownClass() {
        SelenideLogger.removeListener("allure");
    }

    @Story("HappyPath")
    @Severity(SeverityLevel.BLOCKER)
    @Test
    public void shouldSuccessfulSending() {
        cardData = DataHelper.getValidApprovedCard();
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.confirmationofsuccessfuloperation();
        payments = DataBaseHelper.getPayments();
        credits = DataBaseHelper.getCreditsRequest();
        orders = DataBaseHelper.getOrders();
        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals(1, orders.size());
        assertEquals(tripCard.getAmount() * 100, payments.get(0).getAmount());
        assertTrue(payments.get(0).getStatus().equalsIgnoreCase("approved"));
        assertEquals(payments.get(0).getTransaction_id(), orders.get(0).getPayment_id());
        assertNull(orders.get(0).getCredit_id());
    }
}