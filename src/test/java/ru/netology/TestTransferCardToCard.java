package ru.netology;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class TestTransferCardToCard {
    @Test
    @DisplayName("This trigger main test sequence")
    public void main_test_runner() {
        open("http://127.0.0.1:9999");

        LoginPage page_log = new LoginPage();
        page_log.login();

        DashboardChecker dash = new DashboardChecker();
        dash.choose_card_trans();
    }
}