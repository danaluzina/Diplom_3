
import utils.Constants;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.ProfilePage;
import pageObject.RegistrationPage;
import org.junit.Assert;
import org.junit.Test;

import static utils.RandomString.randomString;

public class RegistrationTest extends BaseTest {
    HomePage homePage;
    LoginPage loginPage;
    RegistrationPage registrationPage;
    ProfilePage profilePage;
    String name =  randomString(10);
    String email = randomString(7) + "@mail.ru";
    String correctPassword = randomString(6);
    String wrongPassword = randomString(5);

    @Test
    public void testRegistrationCorrectPassword(){
        homePage = new HomePage(webDriver);
        homePage.waitForPersonalProfileButton();
        homePage.enterPersonalProfile();
        loginPage = new LoginPage(webDriver);
        loginPage.waitForPageLoad();
        loginPage.clickRegistrationLink();
        registrationPage = new RegistrationPage(webDriver);
        registrationPage.waitForPageLoad();
        registrationPage.fillInRegistrationForm(name, email, correctPassword);
        loginPage = new LoginPage(webDriver);
        loginPage.waitForPageLoad();
        Assert.assertEquals(Constants.LOGIN_URL, webDriver.getCurrentUrl());
        loginPage.fillInUserData(email, correctPassword);
        loginPage.clickEnterButton();
        homePage.waitForPersonalProfileButton();
        homePage.enterPersonalProfile();
        profilePage = new ProfilePage(webDriver);
        profilePage.waitForPageLoad();
        Assert.assertEquals(name, profilePage.getNameText());
        Assert.assertEquals(email, profilePage.getEmailText());
    }

    @Test
    public void testRegistrationShortPassword(){
        homePage = new HomePage(webDriver);
        homePage.waitForPersonalProfileButton();
        homePage.enterPersonalProfile();
        loginPage = new LoginPage(webDriver);
        loginPage.waitForPageLoad();
        loginPage.clickRegistrationLink();
        registrationPage = new RegistrationPage(webDriver);
        registrationPage.waitForPageLoad();
        registrationPage.fillInRegistrationForm(name, email, wrongPassword);
        Assert.assertEquals("Некорректный пароль", registrationPage.getPasswordFieldErrorText());
        Assert.assertEquals(Constants.REGISTER_URL, webDriver.getCurrentUrl());
    }
}

