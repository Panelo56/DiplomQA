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

    @Test
    public void shouldSuccessfulSending() {
        cardData = DataHelper.getValidApprovedCard();
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.confirmationofsuccessfuloperation();
        payments = DataBaseHelper.getPay();
        credits = DataBaseHelper.getCredit();
        orders = DataBaseHelper.getOrder();
        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals(1, orders.size());
        assertEquals(tripCard.getAmount() * 100, payments.get(0).getAmount());
        assertTrue(payments.get(0).getStatus().equalsIgnoreCase("approved"));
        assertEquals(payments.get(0).getTransaction_id(), orders.get(0).getPayment_id());
        assertNull(orders.get(0).getCredit_id());
    }

    @Test
    public void shouldUnsuccessfulSending() {
        cardData = DataHelper.getValidDeclinedCard();

        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.purchaseconfirmationwitherrormessage();
        payments = DataBaseHelper.getPay();
        credits = DataBaseHelper.getCredit();
        orders = DataBaseHelper.getOrder();
        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals(1, orders.size());
        assertEquals(tripCard.getAmount() * 100, payments.get(0).getAmount());
        assertTrue(payments.get(0).getStatus().equalsIgnoreCase("declined"));
        assertEquals(payments.get(0).getTransaction_id(), orders.get(0).getPayment_id());
        assertNull(orders.get(0).getCredit_id());
    }

    @Test
    public void shouldImmutableInputValuesAfterClickButton() {
        cardData = DataHelper.getValidApprovedCard();

        tripForm = tripCard.CreditButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripCard.PayButton();
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cardData.getCvc());
    }

    @Test
    public void shouldMessageEmptyNumber() {
        cardData = DataHelper.getValidApprovedCard();
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform("", cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues("", cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.emptynumberfield();
    }

    @Test
    public void shouldMessageEmptyMonth() {
        cardData = DataHelper.getValidApprovedCard();
        var month = "";
        var matchesMonth = "";
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), month, cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), matchesMonth, cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.emptymonthfield();
    }

    @Test
    public void shouldAddNullWithMonth() {
        cardData = DataHelper.getValidApprovedCard();
        var month = DataHelper.genRandomOne();
        var matchesMonth = "0" + month;
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), month, cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), matchesMonth, cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.confirmationofsuccessfuloperation();
    }

    @Test
    public void shouldMessageInvalid00Month() {
        cardData = DataHelper.getValidApprovedCard();
        var month = "00";
        var matchesMonth = month;
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), month, cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), matchesMonth, cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.invalidvaluemonthfield();
    }

    @Test
    public void shouldSuccessMessage01Month() {
        cardData = DataHelper.getValidApprovedCard();
        var month = "01";
        var matchesMonth = month;
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), month, cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), matchesMonth, cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.confirmationofsuccessfuloperation();
    }

    @Test
    public void shouldSuccessMessage12Month() {
        cardData = DataHelper.getValidApprovedCard();
        var month = "12";
        var matchesMonth = month;
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), month, cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), matchesMonth, cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.confirmationofsuccessfuloperation();
    }

    @Test
    public void shouldMessageInvalid13Month() {
        cardData = DataHelper.getValidApprovedCard();
        var month = "13";
        var matchesMonth = month;
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), month, cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), matchesMonth, cardData.getYear(), cardData.getHolder(), cardData.getCvc());
        tripForm.invalidvaluemonthfield();
    }

    @Test
    public void shouldMessageEmptyYear() {
        cardData = DataHelper.getValidApprovedCard();
        var year = "";
        var matchesYear = year;
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), year, cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), matchesYear, cardData.getHolder(), cardData.getCvc());
        tripForm.emptyyearfield();
    }

    @Test
    public void shouldMessageInvalidPreYear() {
        cardData = DataHelper.getValidApprovedCard();
        var year = DataHelper.genYear(-1);
        var matchesYear = year;
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), year, cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), matchesYear, cardData.getHolder(), cardData.getCvc());
        tripForm.invalidvalueyearfield();
    }

    @Test
    public void shouldMessageInvalidPreMonth() {
        cardData = DataHelper.getValidApprovedCard();
        var month = DataHelper.genMonth(-1);
        var matchesMonth = month;
        var year = DataHelper.genYear(0);
        var matchesYear = year;
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), month, year, cardData.getHolder(), cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), matchesMonth, matchesYear, cardData.getHolder(), cardData.getCvc());
        tripForm.invalidvaluemonthfield();
    }

    @Test
    public void shouldMessageEmptyHolder() {
        cardData = DataHelper.getValidApprovedCard();
        var holder = "";
        var matchesHolder = holder;
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), holder, cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), matchesHolder, cardData.getCvc());
        tripForm.emptyholderfield();
    }

    @Test
    public void shouldGetResultHyphenHolder() {
        cardData = DataHelper.getValidApprovedCard();
        var holder = DataHelper.genValidHolderLastNameDouble();
        var matchesHolder = holder;
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), holder, cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), matchesHolder, cardData.getCvc());
        tripForm.confirmationofsuccessfuloperation();
    }

    @Test
    public void shouldAutoUpNameHolder() {
        cardData = DataHelper.getValidApprovedCard();
        var holder = cardData.getHolder().toLowerCase();
        var matchesHolder = cardData.getHolder();
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), holder, cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), matchesHolder, cardData.getCvc());
        tripForm.confirmationofsuccessfuloperation();
    }

    @Test
    public void shouldMessageInvalidCyrillicHolder() {
        cardData = DataHelper.getValidApprovedCard();
        var holder = DataHelper.genInvalidCyrillicHolder();
        var matchesHolder = "";
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), holder, cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), matchesHolder, cardData.getCvc());
        tripForm.emptyholderfield();
    }

    @Test
    public void shouldMessageInvalidHolder() {
        cardData = DataHelper.getValidApprovedCard();
        var holder = DataHelper.genInvalidHolderSymbols();
        var matchesHolder = "";
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), holder, cardData.getCvc());
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), matchesHolder, cardData.getCvc());
        tripForm.emptyholderfield();
    }

    @Test
    public void shouldMessageEmptyCVC() {
        cardData = DataHelper.getValidApprovedCard();
        var cvc = "";
        var matchesCvc = cvc;
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cvc);
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), matchesCvc);
        tripForm.emptycvcfield();
    }

    @Test
    public void shouldMessageInvalidCVC() {
        cardData = DataHelper.getValidApprovedCard();
        var cvc = DataHelper.genInvalidCVC2();
        var matchesCvc = cvc;
        tripForm = tripCard.PayButton();
        tripForm.valuesfortheform(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), cvc);
        tripForm.matchingvalues(cardData.getNumber(), cardData.getMonth(), cardData.getYear(), cardData.getHolder(), matchesCvc);
        tripForm.invalidvaluecvcfield();
    }
}