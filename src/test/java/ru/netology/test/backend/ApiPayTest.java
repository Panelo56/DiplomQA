package ru.netology.test.backend;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.gson.Gson;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.DataBaseHelper;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.AssertJUnit.*;

public class ApiPayTest {
    private static DataHelper.CardData cardData;
    private static final Gson gson = new Gson();
    private static final RequestSpecification spec = new RequestSpecBuilder().setBaseUri("http://localhost").setPort(9999)
            .setAccept(ContentType.JSON).setContentType(ContentType.JSON).log(LogDetail.ALL).build();
    private static final String paymentUrl = "/payment";
    private static List<DataBaseHelper.PaymentEntity> payments;
    private static List<DataBaseHelper.CreditRequestEntity> credits;
    private static List<DataBaseHelper.OrderEntity> orders;

    @BeforeClass
    public void setupClass() {
        DataBaseHelper.setDown();
        SelenideLogger.addListener("allure", new AllureSelenide()
                .screenshots(true).savePageSource(true));
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
        var body = gson.toJson(cardData);
        given().spec(spec).body(body)
                .when().post(paymentUrl)
                .then().statusCode(200);
        payments = DataBaseHelper.getPay();
        credits = DataBaseHelper.getCredit();
        orders = DataBaseHelper.getOrder();
        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals(1, orders.size());
        assertTrue(payments.get(0).getStatus().equalsIgnoreCase("approved"));
        assertEquals(payments.get(0).getTransaction_id(), orders.get(0).getPayment_id());
        assertNull(orders.get(0).getCredit_id());
    }

    @Test
    public void shouldUnsuccessfulSending() {
        cardData = DataHelper.getValidDeclinedCard();
        var body = gson.toJson(cardData);
        given().spec(spec).body(body)
                .when().post(paymentUrl)
                .then().statusCode(200);

        payments = DataBaseHelper.getPay();
        credits = DataBaseHelper.getCredit();
        orders = DataBaseHelper.getOrder();
        assertEquals(1, payments.size());
        assertEquals(0, credits.size());
        assertEquals(1, orders.size());

        assertTrue(payments.get(0).getStatus().equalsIgnoreCase("declined"));
        assertEquals(payments.get(0).getTransaction_id(), orders.get(0).getPayment_id());
        assertNull(orders.get(0).getCredit_id());
    }

    @Test
    public void should400StatusWithAnEmptyBody() {
        cardData = DataHelper.getValidApprovedCard();
        given().spec(spec)
                .when().post(paymentUrl)
                .then().statusCode(400);

        payments = DataBaseHelper.getPay();
        credits = DataBaseHelper.getCredit();
        orders = DataBaseHelper.getOrder();
        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @Test
    public void should400StatusWithAnEmptyNumber() {
        cardData = new DataHelper.CardData(null, DataHelper.genMonth(1), DataHelper.genYear(2),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        var body = gson.toJson(cardData);
        given().spec(spec).body(body)
                .when().post(paymentUrl)
                .then().statusCode(400);

        payments = DataBaseHelper.getPay();
        credits = DataBaseHelper.getCredit();
        orders = DataBaseHelper.getOrder();
        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @Test
    public void should400StatusWithAnEmptyMonth() {
        cardData = new DataHelper.CardData(DataHelper.getStatusNumber("approved"), null, DataHelper.genYear(2),
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        var body = gson.toJson(cardData);
        given().spec(spec).body(body)
                .when().post(paymentUrl)
                .then().statusCode(400);

        payments = DataBaseHelper.getPay();
        credits = DataBaseHelper.getCredit();
        orders = DataBaseHelper.getOrder();
        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @Test
    public void should400StatusWithAnEmptyYear() {
        cardData = new DataHelper.CardData(DataHelper.getStatusNumber("approved"), DataHelper.genMonth(1), null,
                DataHelper.genValidHolder(), DataHelper.genValidCVC());
        var body = gson.toJson(cardData);
        given().spec(spec).body(body)
                .when().post(paymentUrl)
                .then().statusCode(400);

        payments = DataBaseHelper.getPay();
        credits = DataBaseHelper.getCredit();
        orders = DataBaseHelper.getOrder();
        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @Test
    public void should400StatusWithAnEmptyHolder() {
        cardData = new DataHelper.CardData(DataHelper.getStatusNumber("approved"), DataHelper.genMonth(1),
                DataHelper.genYear(2), null, DataHelper.genValidCVC());
        var body = gson.toJson(cardData);
        given().spec(spec).body(body)
                .when().post(paymentUrl)
                .then().statusCode(400);

        payments = DataBaseHelper.getPay();
        credits = DataBaseHelper.getCredit();
        orders = DataBaseHelper.getOrder();
        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }

    @Test
    public void should400StatusWithAnEmptyCVC() {
        cardData = new DataHelper.CardData(DataHelper.getStatusNumber("approved"), DataHelper.genMonth(1),
                DataHelper.genYear(2), DataHelper.genValidHolder(), null);
        var body = gson.toJson(cardData);
        given().spec(spec).body(body)
                .when().post(paymentUrl)
                .then().statusCode(400);

        payments = DataBaseHelper.getPay();
        credits = DataBaseHelper.getCredit();
        orders = DataBaseHelper.getOrder();
        assertEquals(0, payments.size());
        assertEquals(0, credits.size());
        assertEquals(0, orders.size());
    }
}