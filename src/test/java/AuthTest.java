import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AuthTest {
        private static RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

            @BeforeEach
            void setup() {
                open("http://localhost:9999");
            }

            @Test
            @DisplayName("Success registered user")
            void successRegisteredUser() {
                DataGenerator.RegistrationDto registeredUser;
                registeredUser = DataGenerator.Registration.getRegisteredUser("active");
                $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
                $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
                $("[data-test-id='action-login'] .button__content").click();
                $$(".heading").findBy(text("  Личный кабинет")).shouldBe(visible);
            }
            @Test
            @DisplayName("Error login not registered")
            void errorLoginNotRegistered() {
                DataGenerator.RegistrationDto notRegisteredUser;
                notRegisteredUser = DataGenerator.Registration.getUser("active");
                $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
                $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
                $("[data-test-id='action-login'] .button__content").click();
                $("[data-test-id='error-notification'] .notification__title").shouldBe(visible).shouldBe(exactText("Ошибка"));
                $("[data-test-id='error-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("Ошибка! Неверно указан логин или пароль"));
            }

            @Test
            @DisplayName("Error login blocked")
            void errorLoginBlocked() {
                DataGenerator.RegistrationDto blockedUser;
                blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
                $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
                $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
                $("[data-test-id='action-login'] .button__content").click();
                $("[data-test-id='error-notification'] .notification__title").shouldBe(visible).shouldBe(exactText("Ошибка"));
                $("[data-test-id='error-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("Ошибка! Пользователь заблокирован"));
            }

            @Test
            @DisplayName("Error login invalid login")
            void errorInvalidLogin() {
                DataGenerator.RegistrationDto registeredUser;
                registeredUser = DataGenerator.Registration.getRegisteredUser("active");
                String wrongLogin;
                wrongLogin = DataGenerator.getRandomLogin();
                $("[data-test-id='login'] input").setValue(wrongLogin);
                $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
                $("[data-test-id='action-login'] .button__content").click();
                $("[data-test-id='error-notification'] .notification__title").shouldBe(visible).shouldBe(exactText("Ошибка"));
                $("[data-test-id='error-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("Ошибка! Неверно указан логин или пароль"));
            }

            @Test
            @DisplayName("Error invalid password")
            void errorInvalidPassword() {
                DataGenerator.RegistrationDto registeredUser;
                registeredUser = DataGenerator.Registration.getRegisteredUser("active");
                String wrongPassword;
                wrongPassword = DataGenerator.getRandomPassword();
                $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
                $("[data-test-id='password'] input").setValue(wrongPassword);
                $("[data-test-id='action-login'] .button__content").click();
                $("[data-test-id='error-notification'] .notification__title").shouldBe(visible).shouldBe(exactText("Ошибка"));
                $("[data-test-id='error-notification'] .notification__content").shouldBe(visible).shouldBe(exactText("Ошибка! Неверно указан логин или пароль"));
            }
        }
