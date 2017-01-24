import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by lux on 18.01.17.
 */
public class SpammerWorker implements Runnable{
    private Random random;

    public SpammerWorker(Random random) {
        this.random = random;
    }

    public void run() {
        try {
            StringEntity entity = createUser();
            spamAnswer(auth(entity));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StringEntity createUser() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost("http://localhost:8080/api/user/");
        request.addHeader("content-type", "application/json");
        StringEntity entity = null;
        try {
            entity = new StringEntity("{\n" +
                    "  \"password\": \"a\",\n" +
                    "  \"username\": \"" + random.nextLong() + "\"\n" +
                    "}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request.setEntity(entity);
        httpClient.execute(request).getStatusLine().getStatusCode();
        return entity;
    }

    private String auth(StringEntity stringEntity) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost("http://localhost:8080/api/auth/");
        request.addHeader("content-type", "application/json");
        request.setEntity(stringEntity);
        HttpResponse httpResponse = httpClient.execute(request);
        TokenModel tokenModel = (TokenModel)JSONParser.parse(convertResponseToString(httpResponse), TokenModel.class);
        return tokenModel.getToken();
    }

    private void spamAnswer(String token)
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost("http://localhost:8080/api/answer/");
        request.addHeader("content-type", "application/json");
        request.addHeader("token", token);
        StringEntity entity = null;
        try {
            entity = new StringEntity("{\n" +
                    "  \"body\": \"string\",\n" +
                    "  \"id\": 1,\n" +
                    "  \"isValid\": false\n" +
                    "}");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request.setEntity(entity);
        try {
            System.out.println(httpClient.execute(request).getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        String encoding = "UTF-8";
        String rtn = IOUtils.toString(responseStream, encoding);
        return rtn;
    }
}
