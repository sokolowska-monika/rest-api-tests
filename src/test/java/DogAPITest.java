import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.path.json.JsonPath.from;

public class DogAPITest {

    public static String apiPath = "https://dog.ceo/api/breed/%s/images/random";

    private String requestPath(String breed) {
        return String.format(apiPath, breed);
    }

    private String response(String breed) {
        return get(requestPath(breed)).getBody().asString();
    }

    @Test
    public void getRandomFromBreedCollectionDogAPITest() {
        String breed = "african";
        String actualMessage = from(response(breed)).getString("message");
        String expected = String.format("https://images.dog.ceo/breeds/%s", breed);
        Assert.assertTrue(actualMessage.startsWith(expected),
                String.format("%s should start with %s", actualMessage, expected));
    }
}