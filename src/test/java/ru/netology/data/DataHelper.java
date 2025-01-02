package ru.netology.data;

import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Value;
import com.github.javafaker.Faker;

public class DataHelper {
    private static final Faker faker = new Faker(Locale.ENGLISH);
    private static final Faker fakerWithCyrillicLocale = new Faker(new Locale("ru", "RU"));

    @Value
    public static class CardData {
        private final String number;
        private final String month;
        private final String year;
        private final String holder;
        private final String cvc;
    }

    public static CardData getValidApprovedCard() {
        return new CardData(getStatusNumber("approved"), genMonth(1), genYear(2),
                genValidHolder(), genValidCVC());
    }

    public static String getStatusNumber(String status) {
        if (status.equalsIgnoreCase("APPROVED")) {
            return "4444 4444 4444 4441";
        } else if (status.equalsIgnoreCase("DECLINED")) {
            return "4444 4444 4444 4442";
        }
        return null;
    }

    public static String genMonth(int shiftMonth) {
        return LocalDate.now().plusMonths(shiftMonth).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String genYear(int shiftYear) {
        return LocalDate.now().plusYears(shiftYear).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String genValidHolder() {
        return faker.name().fullName().toUpperCase();
    }

    public static String genValidCVC() {
        return faker.numerify("###");
    }

    public static CardData getValidDeclinedCard() {
        return new CardData(getStatusNumber("declined"), genMonth(1), genYear(2),
                genValidHolder(), genValidCVC());
    }

    public static String genRandomInvalidCardNumber() {
        return faker.letterify("???? ???? ???? ????");
    }

    public static String genRandomMonth() {
        return faker.letterify("??");
    }

    public static String genValidHolderLastNameDouble() {
        return faker.name().lastName().toUpperCase() + "-" + faker.name().lastName().toUpperCase() + " "
                + faker.name().firstName().toUpperCase();
    }

    public static String genInvalidCyrillicHolder() {
        return fakerWithCyrillicLocale.name().firstName().toUpperCase() + " "
                + fakerWithCyrillicLocale.name().lastName().toUpperCase();
    }

    public static String genInvalidHolderSymbols() {
        return faker.numerify("#### #### #### ####");
    }

    public static String genInvalidCVC2() {
        return faker.numerify("##");
    }

    public static String genRandomInvalidCVC() {
        return faker.letterify("???");
    }

    public static String genRandomOne() {
        return faker.numerify("#");
    }
}