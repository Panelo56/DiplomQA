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
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class ApiPayCreditTest {
    private static DataHelper.CardData cardData;
    private static final Gson gson = new Gson();
    private static final RequestSpecification spec = new RequestSpecBuilder().setBaseUri("http://localhost").setPort(9999)
            .setAccept(ContentType.JSON).setContentType(ContentType.JSON).log(LogDetail.ALL).build();
    private static final String creditUrl = "/credit";
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
                .when().post(creditUrl)
                .then().statusCode(200);
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