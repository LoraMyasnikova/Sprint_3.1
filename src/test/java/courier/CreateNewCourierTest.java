package courier;

import com.ya.courier.Courier;
import com.ya.courier.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static com.ya.DataForTest.clearCourierTestData;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Story("Creating new courier")
public class CreateNewCourierTest extends FeatureCreatingCourierTest {

    private final CourierClient courierClient = new CourierClient();
    private Courier courier;
    private int statusCode;
    private boolean ok;
    private String message;

    @After
    public void tearDown() {
        clearCourierTestData(courier);
    }

    /* курьера можно создать,
     * запрос возвращает правильный код ответа,
     * успешный запрос возвращает ok: true*/
    @Test
    @DisplayName("Can create new courier")
    @Description("Request returns status code 201. Successful message: \"true\". \"" +
            "Postcondition: Login with valid credentials. Get courier's ID and delete the courier.")
    public void createNewCourierReturnsCreated() {
        Courier courierCredentials = CourierClient.getRandomCredentials();
        courier = Courier.builder()
                .login(courierCredentials.getLogin())
                .password(courierCredentials.getPassword())
                .firstName(courierCredentials.getFirstName())
                .build();

        ValidatableResponse registrationResponse = courierClient.create(courier);
        statusCode = registrationResponse.extract().statusCode();
        ok = registrationResponse.extract().path("ok");

        assertThat("The response is: " + ok, ok, equalTo(true));
        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_CREATED));
    }

    // если одного из полей нет, запрос возвращает ошибку;
    @Test
    @DisplayName("Request returns an error if there is no password field")
    @Description("Register a courier without password field. The system should return an error message.")
    public void createCourierWithoutPasswordFieldReturnsBadRequest() {
        Courier courierCredentials = CourierClient.getRandomCredentials();
        courier = Courier.builder()
                .login(courierCredentials.getLogin())
                .firstName(courierCredentials.getFirstName())
                .build();

        ValidatableResponse registrationResponse = courierClient.create(courier);
        statusCode = registrationResponse.extract().statusCode();
        message = registrationResponse.extract().path("message");

        assertThat("The message is: " + message, message, equalTo("Недостаточно данных для создания учетной записи"));
        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_BAD_REQUEST));
    }

    @Test
    @DisplayName("Request returns an error if there is no login field")
    @Description("Register a courier without login field. The system should return an error message.")
    public void createCourierWithoutLoginFieldReturnsBadRequest() {
        Courier courierCredentials = CourierClient.getRandomCredentials();
        courier = Courier.builder()
                .password(courierCredentials.getPassword())
                .firstName(courierCredentials.getFirstName())
                .build();

        ValidatableResponse registrationResponse = courierClient.create(courier);
        statusCode = registrationResponse.extract().statusCode();
        message = registrationResponse.extract().path("message");

        assertThat("The message is: " + message, message, equalTo("Недостаточно данных для создания учетной записи"));
        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_BAD_REQUEST));
    }

    @Test
    @DisplayName("Request returns an error if there is no first name field")
    @Description("Register a courier without first name field. The system should return an error message.")
    public void createCourierWithoutFirstNameFieldReturnsBadRequest() {
        Courier courierCredentials = CourierClient.getRandomCredentials();
        courier = Courier.builder()
                .login(courierCredentials.getLogin())
                .password(courierCredentials.getPassword())
                .build();

        ValidatableResponse registrationResponse = courierClient.create(courier);
        statusCode = registrationResponse.extract().statusCode();
        message = registrationResponse.extract().path("message");

        assertThat("The message is: " + message, message, equalTo("Недостаточно данных для создания учетной записи"));
        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_BAD_REQUEST));
    }

    // чтобы создать курьера, нужно передать в ручку все обязательные поля
    @Test
    @DisplayName("Create a new courier with required fields - login and password")
    @Description("Register a courier with only login and password fields. " +
            "Postcondition: Login with valid credentials. Get courier's ID and delete the courier.")
    public void createCourierWithOnlyRequiredFieldsReturnsCreated() {
        Courier courierCredentials = CourierClient.getRandomCredentials();

        courier = Courier.builder()
                .login(courierCredentials.getLogin())
                .password(courierCredentials.getPassword())
                .firstName(courierCredentials.getFirstName())
                .build();

        ValidatableResponse registrationResponse = courierClient.create(courier);
        statusCode = registrationResponse.extract().statusCode();
        ok = registrationResponse.extract().path("ok");

        assertThat("The response is: " + ok, ok, equalTo(true));
        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_CREATED));
    }
}