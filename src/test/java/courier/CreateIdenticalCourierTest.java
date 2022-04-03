package courier;

import com.ya.courier.Courier;
import com.ya.courier.CourierClient;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Story("Creating identical couriers")
public class CreateIdenticalCourierTest extends FeatureCreatingCourierTest {

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

    //нельзя создать двух одинаковых курьеров
    @Test
    @DisplayName("Creating identical courier returns conflict")
    @Description("Create a courier with random credentials and register him. Register another one courier with same credentials.")
    public void createIdenticalCourierReturnsConflict() {
        Courier courierIdentical = new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName());
        ValidatableResponse registrationResponse = courierClient.create(courierIdentical);
        statusCode = registrationResponse.extract().statusCode();
        message = registrationResponse.extract().path("message");

        assertThat("The message is: " + message, message, equalTo("Этот логин уже используется. Попробуйте другой."));
        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_CONFLICT));
    }

    // если создать пользователя с логином, который уже есть, возвращается ошибка
    @Test
    @DisplayName("Creating courier with existing login returns conflict")
    @Description("Create a courier with random credentials and register him. Register another one courier with same login.")
    public void createCourierWithExistingLoginReturnsConflict() {
        Courier courierWithExistingLogin = new Courier(courier.getLogin(), RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
        ValidatableResponse registrationResponse = courierClient.create(courierWithExistingLogin);
        statusCode = registrationResponse.extract().statusCode();
        message = registrationResponse.extract().path("message");

        assertThat("The message is: " + message, message, equalTo("Этот логин уже используется. Попробуйте другой."));
        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_CONFLICT));
    }
}