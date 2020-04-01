package ru.netology;

import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class BalanceInfo {
    private HashMap<String, String> balancesBefore = new HashMap<String, String>();
    private HashMap<String, String> balancesAfter = new HashMap<String, String>();
    private Matcher matcher;

    public void parseListCards(String moment) {
        // находим все элементы - банковские карты
        int length = $(".CardList_cardBlock__gEjoa").waitWhile(hidden, 5000).
                findAll(".list__item").size();
        // проходимся по каждой банковской карте
        for (int i = 0; i < length; i++) {
            String key = null, balance;
            // проходимся по каждому вхождению в строке, всего их две штуки - 0001 и баланс 10000
            this.setMatcher(i);
            while (this.matcher.find()) {
                // первое вхождение это последние четыре цифры (ключом мапа с балансом), второе это баланс
                if (key == null) {
                    key = this.getCardNum(i);
                } else {
                    // второе вхождение проверка баланса крты по ключу (четыре последние цифры) после совершения перевода
                    balance = this.getCardBalance(i);
                    if (moment.equals("before")) {
                        this.setBalanceBefore(key, balance);
                    }
                    if (moment.equals("after")) {
                        this.setBalanceAfter(key, balance);
                    }
                }
            }
        }
    }

    private void setMatcher(int index) {
        Pattern pattern = Pattern.compile("-\\d+|\\d+");
        // передаем в матчер строку с текстом последние 4 цифры и баланс счета
        this.matcher = pattern.matcher($(".CardList_cardBlock__gEjoa").findAll(".list__item").
                get(index).find(By.tagName("div")).text());
    }

    private String getCardNum(int index) {
        return $(".CardList_cardBlock__gEjoa").findAll(".list__item").
                get(index).find(By.tagName("div")).text().substring(this.matcher.start(), this.matcher.end());
    }

    private String getCardBalance(int index) {
        return $(".CardList_cardBlock__gEjoa").findAll(".list__item").
                get(index).find(By.tagName("div")).text().substring(this.matcher.start(), this.matcher.end());
    }

    public int getBalanceBefore(String card) {
        return Integer.parseInt(this.balancesBefore.get(card));
    }

    public int getBalanceAfter(String card) {
        return Integer.parseInt(this.balancesAfter.get(card));
    }

    public void setBalanceBefore(String key, String val) {
        this.balancesBefore.put(key, val);
    }

    public void setBalanceAfter(String key, String val) {
        this.balancesAfter.put(key, val);
    }
}
