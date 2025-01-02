package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;
import static org.testng.AssertJUnit.assertEquals;

public class FormPage {
    private static final SelenideElement dailyTripCard = $x("//div[@id='root']/div/div[contains(@class, 'card')]");
    private static final SelenideElement payButton = $x("//span[text()='Купить']//ancestor::button");
    private static final SelenideElement creditButton = $x("//span[text()='Купить в кредит']//ancestor::button");
    private static final SelenideElement form = $x("//form");
    private static final SelenideElement numberLabel = form.$x(".//span[text()='Номер карты']//ancestor::div/span");
    private static final SelenideElement numberInput = numberLabel.$x(".//ancestor::span//input");
    private static final SelenideElement monthLabel = form.$x(".//span[text()='Месяц']//ancestor::div/span/span[1]/span");
    private static final SelenideElement monthInput = monthLabel.$x(".//input");
    private static final SelenideElement yearLabel = form.$x(".//span[text()='Год']//ancestor::div/span/span[2]/span");
    private static final SelenideElement yearInput = yearLabel.$x(".//input");
    private static final SelenideElement holderLabel = form.$x(".//span[text()='Владелец']//ancestor::div/span/span[1]/span");
    private static final SelenideElement holderInput = holderLabel.$x(".//input");
    private static final SelenideElement cvcLabel = form.$x(".//span[text()='CVC/CVV']//ancestor::div/span/span[2]/span");
    private static final SelenideElement cvcInput = cvcLabel.$x(".//input");
    private static final SelenideElement continuousButton = form.$x(".//span[text()='Продолжить']//ancestor::button");
    private static final SelenideElement successNotification = $x("//div[contains(@class, 'notification_status_ok')]");
    private static final SelenideElement successCloseButton = successNotification.$x("./button");
    private static final SelenideElement errorNotification = $x("//div[contains(@class, 'notification_status_error')]");
    private static final SelenideElement errorCloseButton = errorNotification.$x("./button");

    public FormPage() {
        dailyTripCard.should(Condition.visible);
        payButton.should(Condition.visible);
        creditButton.should(Condition.visible);
        form.should(Condition.visible);
        successNotification.should(Condition.hidden);
        errorNotification.should(Condition.hidden);
    }

    public void valuesfortheform(String number, String month, String year, String holder, String cvc) {
        numberLabel.click();
        numberInput.val(number);
        monthLabel.click();
        monthInput.val(month);
        yearLabel.click();
        yearInput.val(year);
        holderLabel.click();
        holderInput.val(holder);
        cvcLabel.click();
        cvcInput.val(cvc);
        continuousButton.click();
    }

    public void matchingvalues(String number, String month, String year, String holder, String cvc) {
        assertEquals(number, numberInput.getValue());
        assertEquals(month, monthInput.getValue());
        assertEquals(year, yearInput.getValue());
        assertEquals(holder, holderInput.getValue());
        assertEquals(cvc, cvcInput.getValue());
    }

    public void confirmationofsuccessfuloperation() {
        successNotification.should(Condition.visible, Duration.ofSeconds(15));
        successNotification.should(Condition.cssClass("notification_visible"));
        successNotification.$x("./div[@class='notification__title']").should(Condition.text("Успешно"));
        successNotification.$x("./div[@class='notification__content']").should(Condition.text("Операция одобрена Банком."));
        successCloseButton.click();
        successNotification.should(Condition.hidden);
    }

    public void purchaseconfirmationwitherrormessage() {
        errorNotification.should(Condition.visible, Duration.ofSeconds(15));
        errorNotification.should(Condition.cssClass("notification_visible"));
        errorNotification.$x("/div[@class='notification__title']").should(Condition.text("Ошибка"));
        errorNotification.$x("/div[@class='notification__content']").should(Condition.text("Ошибка! Банк отказал в проведении операции."));
        errorCloseButton.click();
        errorNotification.should(Condition.hidden);
    }

    public void emptynumberfield() {
        numberLabel.should(Condition.cssClass("input_invalid")).shouldNot(Condition.cssClass("input_has-value"));
        numberLabel.$x(".//span[@class='input__sub']").should(Condition.visible, Condition.text("Поле обязательно для заполнения"));
    }

    public void emptymonthfield() {
        monthLabel.should(Condition.cssClass("input_invalid")).shouldNot(Condition.cssClass("input_has-value"));
        monthLabel.$x(".//span[@class='input__sub']").should(Condition.visible, Condition.text("Поле обязательно для заполнения"));
    }

    public void invalidvaluemonthfield() {
        monthLabel.should(Condition.cssClass("input_invalid"), Condition.cssClass("input_has-value"));
        monthLabel.$x(".//span[@class='input__sub']").should(Condition.visible, Condition.text("Неверно указан срок действия карты"));
    }

    public void emptyyearfield() {
        yearLabel.should(Condition.cssClass("input_invalid"));
        yearLabel.$x(".//span[@class='input__sub']").should(Condition.visible, Condition.text("Поле обязательно для заполнения"));
    }

    public void invalidvalueyearfield() {
        yearLabel.should(Condition.cssClass("input_invalid"), Condition.cssClass("input_has-value"));
        yearLabel.$x(".//span[@class='input__sub']").should(Condition.visible, Condition.text("Истёк срок действия карты"));
    }

    public void emptyholderfield() {
        holderLabel.should(Condition.cssClass("input_invalid")).shouldNot(Condition.cssClass("input_has-value"));
        holderLabel.$x(".//span[@class='input__sub']").should(Condition.visible, Condition.text("Поле обязательно для заполнения"));
    }

    public void emptycvcfield() {
        cvcLabel.should(Condition.cssClass("input_invalid")).shouldNot(Condition.cssClass("input_has-value"));
        cvcLabel.$x(".//span[@class='input__sub']").should(Condition.visible, Condition.text("Поле обязательно для заполнения"));
    }

    public void invalidvaluecvcfield() {
        cvcLabel.should(Condition.cssClass("input_invalid"), Condition.cssClass("input_has-value"));
        cvcLabel.$x(".//span[@class='input__sub']").should(Condition.visible, Condition.text("Неверный формат"));
    }
}