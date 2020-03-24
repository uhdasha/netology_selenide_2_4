package ru.netology;

import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class DashboardChecker {
    private HashMap<String, String> balances = new HashMap<String, String>();
    private HashMap<String, String> cards = new HashMap<String, String>();
    public String amount;
    public String card_from;

    @DisplayName("choosing card and then make transfer")
    public void choose_card_trans() {
        cards.put("0001", "5559 0000 0000 0001");
        cards.put("0002", "5559 0000 0000 0002");

        $(".CardList_cardBlock__gEjoa").
                findAll("[data-test-id='action-deposit']").get(0).click();

        Random rand_amount = new Random();
        amount = String.valueOf(rand_amount.nextInt(9999));
        $("[data-test-id='amount']").find("input").setValue(amount);

        String card_to = $("[data-test-id='to']").find("input").val().substring(15, 19);

        card_from = null;
        // проходимся по всем картам, и выбираем карту отличную от той на которую выполняем перевод
        for (Map.Entry<String, String> card : cards.entrySet()) {
            // если карта куда совпадает с откуда, идем дальше по циклу
            if (card_to == card.getKey()) {
                continue;
            } else {
                card_from = card.getKey();
                break;
            }
        }

        $("[data-test-id='from']").find("input").setValue(cards.get(card_from));

        $("[data-test-id='action-transfer']").click();

        balances.put(card_to, String.valueOf(10000 + Integer.parseInt(amount)));
        balances.put(card_from, String.valueOf(10000 - Integer.parseInt(amount)));

        check_balance();
    }

    private void check_balance() {
        // находим все элементы - банковские карты
        int length = $(".CardList_cardBlock__gEjoa").waitWhile(hidden, 5000).
                findAll(".list__item").size();
        // проходимся по каждой банковской карте
        for (int i = 0; i < length; i++) {
            // ищем в строке типа "**** **** **** 0001, баланс: 10000 р." только числовые значения
            Pattern pattern = Pattern.compile("\\d+");
            // передаем в матчер строку с текстом последние 4 цифры и баланс счета
            Matcher matcher = pattern.matcher($(".CardList_cardBlock__gEjoa").findAll(".list__item").
                    get(i).find(By.tagName("div")).text());
            String key = null;
            // проходимся по каждому вхождению в строке, всего их две штуки - 0001 и баланс 10000
            while (matcher.find()) {
                // первое вхождение это последние четыре цифры (ключом мапа с балансом), второе это баланс
                if (key == null) {
                    key = $(".CardList_cardBlock__gEjoa").findAll(".list__item").
                            get(i).find(By.tagName("div")).text().substring(matcher.start(), matcher.end());
                } else {
                    // второе вхождение проверка баланса крты по ключу (четыре последние цифры) после совершения перевода
                    $(".CardList_cardBlock__gEjoa").findAll(".list__item").
                            get(i).find(By.tagName("div")).shouldHave(text(balances.get(key)));

                }
            }
        }
    }
}