package courier;

import com.ya.courier.Courier;
import com.ya.courier.CourierClient;
import com.ya.courier.CourierCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.ya.DataForTest.clearCourierTestData;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Story("Courier can login with valid credentials")
public class CourierLoginPositiveTest extends FeatureLoginCourierTest {

    private final CourierClient courierClient = new CourierClient();
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        Courier courierCredentials = CourierClient.getRandomCredentials();
        courier = Courier.builder()
                .login(courierCredentials.getLogin())
                .password(courierCredentials.getPassword())
                .firstName(courierCredentials.getFirstName())
                .build();
        courierClient.create(courier);
    }

    @After
    public void tearDown() {
        clearCourierTestData(courierId);
    }

    /*курьер может авторизоваться
    для авторизации нужно передать все обязательные поля
    успешный запрос возвращает id*/
    @Test
    @DisplayName("Courier can login with valid credentials")
    @Description("Courier logs in with required valid credentials. Request returns courier's ID")
    public void courierCanLoginWithValidCredentials() {
        CourierCredentials credentials = CourierCredentials.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .build();

        ValidatableResponse loginResponse = courierClient.login(credentials);
        int statusCode = loginResponse.extract().statusCode();
        courierId = loginResponse.extract().path("id");

        assertThat("The status code is " + statusCode, statusCode, equalTo(SC_OK));
        assertThat("The courier's ID is null", courierId, notNullValue());
    }
}