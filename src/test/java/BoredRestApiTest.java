import static io.restassured.RestAssured.get;
import static io.restassured.path.json.JsonPath.from;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BoredRestApiTest {

    public static String apiPath = "http://www.boredapi.com/api/activity?key=%s";

    private String requestPath(String key) {
        return String.format(apiPath, key);
    }

    private String actualResponse(String key) {
        return get(requestPath(key)).getBody().asString();
    }

    public void expectedResponseForBoredAPI(String activity, String type, String participants, String price, String link, String key, String accessibility) {
        String expectedResponse = String.format("""
                {"activity":"%s","type":"%s","participants":%s,"price":%s,"link":"%s","key":"%s","accessibility":%s}""", activity, type, participants, price, link, key, accessibility);
        Assert.assertEquals(actualResponse(key), expectedResponse);
    }

    @Test
    public void getResponseForKeyBoredAPITest() {
        String key = "5881028";
        expectedResponseForBoredAPI("Learn a new programming language", "education", "1", "0.1", "", "5881028", "0.25");
    }
}