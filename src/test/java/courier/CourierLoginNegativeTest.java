package courier;

import com.ya.courier.Courier;
import com.ya.courier.CourierClient;
import com.ya.courier.CourierCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.ya.DataForTest.clearCourierTestData;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Story("Courier can't login with invalid credentials")
public class CourierLoginNegativeTest extends FeatureLoginCourierTest {

    private final CourierClient courierClient = new CourierClient();
    private Courier courier;
    private int statusCode;
    private String message;

    @Before
    public void setUp() {
        Courier courierCredentials = CourierClient.getRandomCredentials();
        courier = new Courier(courierCredentials.getLogin(), courierCredentials.getPassword(), courierCredentials.getFirstName());
        courierClient.create(courier);
    }

    @After
    public void tearDown() {
        clearCourierTestData(courier);
    }

    //система вернёт ошибку, если неправильно указать логин или пароль;
    @Test
    @DisplayName("Request returns an error if login is invalid")
    @Description("Create a courier with valid credentials. Log in with wrong login. Response should contain an error message. " +
            "Postcondition: Log in with correct credentials. Get courier's ID and delete the courier.")
    public void courierCantLoginWithWrongLogin() {
        CourierCredentials credentials = new CourierCredentials(RandomStringUtils.randomAlphabetic(10), courier.getPassword());
        ValidatableResponse loginResponse = courierClient.login(credentials);
        statusCode = loginResponse.extract().statusCode();
        message = loginResponse.extract().path("message");

        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_NOT_FOUND));
        assertThat("The message is: " + message, message, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Request returns an error if password is invalid")
    @Description("Create a courier with valid credentials. Log in with wrong password. Response should contain an error message. " +
            "Postcondition: Log in with correct credentials. Get courier's ID and delete the courier.")
    public void courierCantLoginWithWrongPassword() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse loginResponse = courierClient.login(credentials);
        statusCode = loginResponse.extract().statusCode();
        message = loginResponse.extract().path("message");

        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_NOT_FOUND));
        assertThat("The message is: " + message, message, equalTo("Учетная запись не найдена"));
    }

    //если какого-то поля нет, запрос возвращает ошибку;
    @Test
    @DisplayName("Request returns an error if password field is empty")
    @Description("Create a courier with valid credentials. Log in with only login field. Response should contain an error message. " +
            "Postcondition: Log in with correct credentials. Get courier's ID and delete the courier.")
    public void courierCantLoginWithLoginFieldOnly() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "");
        ValidatableResponse loginResponse = courierClient.login(credentials);
        statusCode = loginResponse.extract().statusCode();
        message = loginResponse.extract().path("message");

        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("The message is: " + message, message, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Request returns an error if login field is empty")
    @Description("Create a courier with valid credentials. Log in with only password field. Response should contain an error message. " +
            "Postcondition: Log in with correct credentials. Get courier's ID and delete the courier.")
    public void courierCantLoginWithPasswordFieldOnly() {
        CourierCredentials credentials = new CourierCredentials("", courier.getPassword());
        ValidatableResponse loginResponse = courierClient.login(credentials);
        statusCode = loginResponse.extract().statusCode();
        message = loginResponse.extract().path("message");

        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("The message is: " + message, message, equalTo("Недостаточно данных для входа"));
    }

    //если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
    @Test
    @DisplayName("Request returns an error if login with non-existing user's credentials")
    @Description("Create a courier with valid credentials. Log in with invalid credentials. Response should contain an error message. " +
            "Postcondition: Log in with correct credentials. Get courier's ID and delete the courier.")
    public void courierCantLoginWithInvalidCredentials() {
        String invalidLogin = courier.getLogin() + "q";
        CourierCredentials credentials = new CourierCredentials(invalidLogin, courier.getPassword());
        ValidatableResponse loginResponse = courierClient.login(credentials);
        statusCode = loginResponse.extract().statusCode();
        message = loginResponse.extract().path("message");

        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_NOT_FOUND));
        assertThat("The message is: " + message, message, equalTo("Учетная запись не найдена"));
    }
}