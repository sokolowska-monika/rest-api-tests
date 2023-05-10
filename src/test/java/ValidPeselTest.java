import static io.restassured.RestAssured.get;
import static io.restassured.path.json.JsonPath.from;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ValidPeselTest {
    public static String apiPath = "https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=%s";

    private String requestPath(String pesel) {
        return String.format(apiPath, pesel);
    }

    private int actualStatusCode(String pesel) {
        return get(requestPath(pesel)).getStatusCode();
    }

    private String actualResponse(String pesel) {
        return get(requestPath(pesel)).getBody().asString();
    }

    private boolean isValid(String pesel) {
        return from(actualResponse(pesel)).getBoolean("isValid");
    }

    private void expectedResponseForPesel(String pesel, boolean isValid, String dateOfBirth, String gender) {
        String expectedResponse = String.format("""
                        {"pesel":"%s","isValid":%s,"dateOfBirth":"%sT00:00:00","gender":"%s","errors":[]}""", pesel, isValid, dateOfBirth, gender);
        Assert.assertEquals(actualResponse(pesel), expectedResponse);
    }

    @Test
    public void peselWithStatusCode_200ForValidPeselTest() {
        String pesel = "48100779844";
        Assert.assertEquals(actualStatusCode(pesel), 200);
    }

    @Test
    public void peselForAnyMonthFor1800To1899Test() {
        String pesel = "33810294428";
        expectedResponseForPesel(pesel, true, "1833-01-02", "Female");
    }

    @Test
    public void peselForAnyMonthFor1900To1999Test() {
        String pesel = "55111275317";
        expectedResponseForPesel(pesel, true, "1955-11-12", "Male");;
    }

    @Test
    public void peselForAnyMonthFor2000To2099Test() {
        String pesel = "14261740535";
        expectedResponseForPesel(pesel, true, "2014-06-17", "Male");
    }

    @Test
    public void peselForAnyMonthFor2100To2199Test() {
        String pesel = "47492549062";
        expectedResponseForPesel(pesel, true, "2147-09-25", "Female");
    }

    @Test
    public void peselForAnyMonthFor2200To2299Test() {
        String pesel = "78632013851";
        expectedResponseForPesel(pesel, true, "2278-03-20", "Male");
    }

    @Test
    public void peselForJanuary31th1983Test() {
        String pesel = "83013123335";
        expectedResponseForPesel(pesel, true, "1983-01-31", "Male");
    }

    @Test
    public void peselForFerbuary29thForLeapYearOf2020Test() {
        String pesel = "20222950545";
        expectedResponseForPesel(pesel, true, "2020-02-29", "Female");
    }

    @Test
    public void peselForFerbuary28thForNonLeapYearOf2019Test() {
        String pesel = "19222884354";
        expectedResponseForPesel(pesel, true, "2019-02-28", "Male");
    }

    @Test
    public void peselForMarch31th1982Test() {
        String pesel = "82033119951";
        expectedResponseForPesel(pesel, true, "1982-03-31", "Male");
    }

    @Test
    public void peselForApril30th1979Test() {
        String pesel = "79043096307";
        expectedResponseForPesel(pesel, true, "1979-04-30", "Female");
    }

    @Test
    public void peselForMay31th1800Test() {
        String pesel = "00853116286";
        expectedResponseForPesel(pesel, true, "1800-05-31", "Female");
    }

    @Test
    public void peselForJune30th1899Test() {
        String pesel = "99863057713";
        expectedResponseForPesel(pesel, true, "1899-06-30", "Male");
    }

    @Test
    public void peselForJuly31th1996Test() {
        String pesel = "96073187401";
        expectedResponseForPesel(pesel, true, "1996-07-31", "Female");
    }

    @Test
    public void peselForAugust31th1965Test() {
        String pesel = "65083128739";
        expectedResponseForPesel(pesel, true, "1965-08-31", "Male");
    }

    @Test
    public void peselForSeptember30th2250Test() {
        String pesel = "50693070901";
        expectedResponseForPesel(pesel, true, "2250-09-30", "Female");
    }

    @Test
    public void peselForOctober31th2157Test() {
        String pesel = "57503181084";
        expectedResponseForPesel(pesel, true, "2157-10-31", "Female");
    }

    @Test
    public void peselForNovember30th1864Test() {
        String pesel = "64913017397";
        expectedResponseForPesel(pesel, true, "1864-11-30", "Male");
    }

    @Test
    public void peselForDecember31th2034Test() {
        String pesel = "34323196041";
        expectedResponseForPesel(pesel, true, "2034-12-31", "Female");
    }
}