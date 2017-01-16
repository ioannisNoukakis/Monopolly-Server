package testImpl;

import TestUtils.JSONUtils;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import dto.Token;
import dto.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by lux on 16.01.17.
 */
public class UserAuth {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final SharedData sharedData = new SharedData();

    @Given("^a user payload$")
    public void aUserPayload() throws Throwable {
        User user = new User("test" + System.currentTimeMillis(), "test" + System.currentTimeMillis());
        sharedData.setPayload(JSONUtils.toJson(user));
        sharedData.setUser(user);
    }

    @When("^the user send create request on \"([^\"]*)\"$")
    public void theUserSendCreateRequestOn(String arg0) throws Throwable {
        HttpPost request = new HttpPost(TestConfig.BASE_URL_API + arg0);
        request.addHeader("content-type", "application/json");
        StringEntity entity = new StringEntity(sharedData.getPayload());
        request.setEntity(entity);
        sharedData.setHttpResponse(httpClient.execute(request));
    }

    @Then("^the server should reply with (\\d+)$")
    public void theServerShouldReplyWith(int arg0) throws Throwable {
        assertEquals(arg0, sharedData.getHttpResponse().getStatusLine().getStatusCode());
    }

    @Given("^an empty user payload$")
    public void anEmptyUserPayload() throws Throwable {
        sharedData.setPayload("");
    }

    @Given("^a user payload with empty fields$")
    public void aUserPayloadWithEmptyFields() throws Throwable {
        sharedData.setPayload(JSONUtils.toJson(new User(null, "test" + System.currentTimeMillis())));
    }


    @Given("^an auth payload$")
    public void anAuthPayload() throws Throwable {
        sharedData.setPayload(JSONUtils.toJson(sharedData.getUser()));
    }

    @Then("^the response body should contains a token$")
    public void theResponseBodyShouldContainsAToken() throws Throwable {
        String responseBody = convertResponseToString(sharedData.getHttpResponse());
        assertNotEquals("", responseBody);
        Token token = (Token)JSONUtils.parse(responseBody, Token.class);
        assertNotNull(token);
        sharedData.setToken(token);
        System.out.println(responseBody);
    }

    private String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }
}
