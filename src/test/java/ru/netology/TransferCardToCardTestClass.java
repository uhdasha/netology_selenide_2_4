package ru.netology;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class TransferCardToCardTestClass {
    @Test
    @DisplayName("This trigger main test sequence")
    public void runMainTestClass() {
        open("http://127.0.0.1:9999");

        LoginPage pageLog = new LoginPage();
        // fill login info
        pageLog.auth();
        // fill code verify
        pageLog.verify();

        DashboardChecker dash = new DashboardChecker();
        // set balances before transfer
        dash.checkBalanceBeforeTransfer();
        // make transfer
        dash.transferToSecondCard();
        // set balances after transfer
        dash.checkBalanceAfterTransfer();
        // make assert check
        dash.checkCorrectTransfer();
    }
}