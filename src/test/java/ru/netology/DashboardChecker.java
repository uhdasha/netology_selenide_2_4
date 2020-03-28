package ru.netology;

import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DashboardChecker {

    private HashMap<String, String> cards = new HashMap<String, String>();
    public String amount;
    public String cardFrom;
    public String cardTo;
    private BalanceInfo info;

    public DashboardChecker() {
        this.cards.put("0001", "5559 0000 0000 0001");
        this.cards.put("0002", "5559 0000 0000 0002");
        this.info = new BalanceInfo();
    }
    @DisplayName("get balance before")
    public void checkBalanceBeforeTransfer() {
        this.info.parseListCards("before");
    }
    @DisplayName("get balance after")
    public void checkBalanceAfterTransfer() {
        info.parseListCards("after");
    }
    @DisplayName("make transfer random sum from first to second")
    public void transferToSecondCard() {
        $(".CardList_cardBlock__gEjoa").
                findAll("[data-test-id='action-deposit']").get(0).click();

        Random rand_amount = new Random();
        this.amount = String.valueOf(rand_amount.nextInt(9999));
        $("[data-test-id='amount']").find("input").setValue(this.amount);

        this.cardTo = $("[data-test-id='to']").find("input").val().substring(15, 19);

        this.getCardFromTransfer();

        $("[data-test-id='from']").find("input").setValue(cards.get(this.cardFrom));

        $("[data-test-id='action-transfer']").click();
    }

    private void getCardFromTransfer() {
        // проходимся по всем картам, и выбираем карту отличную от той на которую выполняем перевод
        for (Map.Entry<String, String> card : cards.entrySet()) {
            // если карта куда совпадает с откуда, идем дальше по циклу
            if (!this.cardTo.equals(card.getKey())) {
                this.cardFrom = card.getKey();
                break;
            }
        }
    }

    @DisplayName("check balances two card after transfer")
    public void checkCorrectTransfer() {
        assertEquals(this.info.getBalanceBefore(this.cardTo) + Integer.parseInt(amount),
                this.info.getBalanceAfter(this.cardTo));
        assertEquals(this.info.getBalanceBefore(this.cardFrom) - Integer.parseInt(amount),
                this.info.getBalanceAfter(this.cardFrom));
    }

}