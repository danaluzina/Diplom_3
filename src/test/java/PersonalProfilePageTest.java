
import io.restassured.response.Response;
import user.User;
import user.UserClient;
import utils.Constants;
import pageObject.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static utils.RandomString.randomString;

public class PersonalProfilePageTest extends BaseTest {
    HomePage homePage;
    LoginPage loginPage;
    ProfilePage profilePage;

    String email;
    String password;
    String name;
    String accessToken;
    UserClient userClient = new UserClient();
    User user;

    @Before
    public void createUser(){
        name = randomString(7);
        email = randomString(6) + "@yandex.ru";
        password = randomString(7);
        user = new User(email, password, name);
        Response createUser = userClient.createUser(user);
        accessToken = createUser.body().path("accessToken").toString().substring(7);

        homePage = new HomePage(webDriver);
        homePage.clickEnterAccountButton();
        loginPage = new LoginPage(webDriver);
        loginPage.waitForPageLoad();
        loginPage.fillInUserData(email, password);
        loginPage.clickEnterButton();
    }

    @Test
    public void testEnterPersonalProfileByClickPersonalProfileButton(){
        homePage.waitForRegisterOrderButton();
        homePage.enterPersonalProfile();
        profilePage = new ProfilePage(webDriver);
        profilePage.waitForPageLoad();
        Assert.assertEquals(Constants.PROFILE_URL, webDriver.getCurrentUrl());
        Assert.assertEquals(name, profilePage.getNameText());
        Assert.assertEquals(email, profilePage.getEmailText());
    }

    @Test
    public void testClickConstructorButtonFromPersonalProfile() {
        homePage.waitForRegisterOrderButton();
        homePage.enterPersonalProfile();
        profilePage = new ProfilePage(webDriver);
        profilePage.waitForPageLoad();
        profilePage.clickConstructorButton();
        Assert.assertEquals(Constants.HOME_URL, webDriver.getCurrentUrl());
    }

    @Test
    public void testClickLogoFromPersonalProfile() {
        homePage.waitForRegisterOrderButton();
        homePage.enterPersonalProfile();
        profilePage = new ProfilePage(webDriver);
        profilePage.waitForPageLoad();
        profilePage.clickLogo();
        Assert.assertEquals(Constants.HOME_URL, webDriver.getCurrentUrl());
    }

    @Test
    public void testExitButton() {
        homePage.waitForRegisterOrderButton();
        homePage.enterPersonalProfile();
        profilePage = new ProfilePage(webDriver);
        profilePage.waitForPageLoad();
        profilePage.clickExitProfileButton();
        loginPage.waitForPageLoad();
        Assert.assertEquals(Constants.LOGIN_URL, webDriver.getCurrentUrl());
        Assert.assertEquals("", loginPage.getEmailInputFieldValue());
        Assert.assertEquals("", loginPage.getPasswordInputFieldValue());
    }

    @After
    public void cleanUp(){
        userClient.deleteUser(accessToken);
    }
}
