package ru.netology;

import org.junit.jupiter.api.DisplayName;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class LoginPage {

    @DisplayName("make login")
    public void login() {
        $("[name='login']").setValue("vasya");
        $("[name='password']").setValue("qwerty123");
        $("[data-test-id='action-login']").click();
        $("[name='code']").setValue("12345");
        $("[data-test-id='action-verify']").click();
    }
}