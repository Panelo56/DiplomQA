package ru.netology.test.frontend;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DataBaseHelper;
import ru.netology.page.CardPage;
import ru.netology.page.FormPage;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class InterfacePayCreditTest {
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

    @Test
    public void shouldSuccessfulSending() {
        cardData = DataHelper.getValidApprovedCard();
        tripForm = tripCard.CreditButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.confirmationofsuccessfuloperation();
        payments = DataBaseHelper.getPayments();
        credits = DataBaseHelper.getCreditsRequest();
        orders = DataBaseHelper.getOrders();
        assertEquals(0, payments.size());
        assertEquals(1, credits.size());
        assertEquals(1, orders.size());
        assertTrue(credits.get(0).getStatus().equalsIgnoreCase("approved"));
        assertEquals(credits.get(0).getBank_id(), orders.get(0).getPayment_id());
        assertEquals(credits.get(0).getId(), orders.get(0).getCredit_id());
    }
}