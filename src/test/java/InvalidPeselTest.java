import static io.restassured.RestAssured.get;
import static io.restassured.path.json.JsonPath.from;

import org.testng.Assert;
import org.testng.annotations.Test;

public class InvalidPeselTest {
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

    private String actualDateOfBirth(String pesel) {
        return from(actualResponse(pesel)).getString("dateOfBirth");
    }

    private String actualGender(String pesel) {
        return from(actualResponse(pesel)).getString("gender");
    }

    private void expectedResponseForError_INVL(String pesel, boolean isValid, String dateOfBirth, String gender, String errorCode, String errorMessage) {
        String expectedResponse = String.format("""
                        {"pesel":"%s","isValid":%s,"dateOfBirth":%s,"gender":%s,"errors":[{"errorCode":%s,"errorMessage":%s}]}""",
                pesel, isValid, dateOfBirth, gender, errorCode, errorMessage);
        Assert.assertEquals(actualResponse(pesel), expectedResponse);
    }

    private void expectedResponseForError_NBRQ(String pesel, boolean isValid, String dateOfBirth, String gender, String errorCode, String errorMessage) {
        String expectedResponse = String.format("""
                        {"pesel":"%s","isValid":%s,"dateOfBirth":%s,"gender":%s,"errors":[{"errorCode":%s,"errorMessage":%s}]}""",
                pesel, isValid, dateOfBirth, gender, errorCode, errorMessage);
        Assert.assertEquals(actualResponse(pesel), expectedResponse);
    }

    private void expectedResponseForError_INVY_INVM(String pesel, boolean isValid, String dateOfBirth, String gender, String errorCode, String errorMessage, String errorCode1, String errorMessage1) {
        String expectedResponse = String.format("""
                        {"pesel":"%s","isValid":%s,"dateOfBirth":%s,"gender":"%s","errors":[{"errorCode":%s,"errorMessage":%s},{"errorCode":%s,"errorMessage":%s}]}""",
                pesel, isValid, dateOfBirth, gender, errorCode, errorMessage, errorCode1, errorMessage1);
        Assert.assertEquals(actualResponse(pesel), expectedResponse);
    }

    private void expectedResponseForError_INVD(String pesel, boolean isValid, String dateOfBirth, String gender, String errorCode, String errorMessage) {
        String expectedResponse = String.format("""
                        {"pesel":"%s","isValid":%s,"dateOfBirth":%s,"gender":"%s","errors":[{"errorCode":%s,"errorMessage":%s}]}""",
                pesel, isValid, dateOfBirth, gender, errorCode, errorMessage);
        Assert.assertEquals(actualResponse(pesel), expectedResponse);
    }

    private void expectedResponseForError_INVY_INVM_INVD(String pesel, boolean isValid, String dateOfBirth, String gender, String errorCode, String errorMessage, String errorCode1, String errorMessage1, String errorCode2, String errorMessage2) {
        String expectedResponse = String.format("""
                        {"pesel":"%s","isValid":%s,"dateOfBirth":%s,"gender":"%s","errors":[{"errorCode":%s,"errorMessage":%s},{"errorCode":%s,"errorMessage":%s},{"errorCode":%s,"errorMessage":%s}]}""",
                pesel, isValid, dateOfBirth, gender, errorCode, errorMessage, errorCode1, errorMessage1, errorCode2, errorMessage2);
        Assert.assertEquals(actualResponse(pesel), expectedResponse);
    }

    private void expectedResponseForError_INVC(String pesel, boolean isValid, String dateOfBirth, String gender, String errorCode, String errorMessage) {
        String expectedResponse = String.format("""
                        {"pesel":"%s","isValid":%s,"dateOfBirth":"%s","gender":"%s","errors":[{"errorCode":%s,"errorMessage":%s}]}""",
                pesel, isValid, dateOfBirth, gender, errorCode, errorMessage);
        Assert.assertEquals(actualResponse(pesel), expectedResponse);
    }

    @Test
    public void peselWithStatusCode400_MissingPeselParameterTest() {
        String pesel = "";
        Assert.assertEquals(actualStatusCode(pesel), 400);
    }

    @Test
    public void peselWithError_INVL_TooShortPeselLenghtTest() {
        String pesel = "123";
        expectedResponseForError_INVL(pesel, false, null, null, "\"INVL\"", "\"Invalid length. Pesel should have exactly 11 digits.\"");
    }

    @Test
    public void peselWithError_INVL_TooLongPeselLenghtTest() {
        String pesel = "232307459866";
        expectedResponseForError_INVL(pesel, false, null, null, "\"INVL\"", "\"Invalid length. Pesel should have exactly 11 digits.\"");
    }

    @Test
    public void peselWithError_NBRQ_PeselCharacterTest() {
        String pesel = "2323074598a";
        expectedResponseForError_INVL(pesel, false, null, null, "\"NBRQ\"", "\"Invalid characters. Pesel should be a number.\"");
    }

    @Test
    public void peselWithError_INVY_INVM_NonexistentMonthFor1800To1899Test() {
        String pesel = "50930163317";
        expectedResponseForError_INVY_INVM(pesel, false, null, actualGender(pesel), "\"INVY\"", "\"Invalid year.\"", "\"INVM\"", "\"Invalid month.\"");
    }

    @Test
    public void peselWithError_INVY_INVM_NonexistentMonthFor1900To1999Test() {
        String pesel = "84131246502";
        expectedResponseForError_INVY_INVM(pesel, false, null, actualGender(pesel), "\"INVY\"", "\"Invalid year.\"", "\"INVM\"", "\"Invalid month.\"");
    }

    @Test
    public void peselWithError_INVY_INVM_NonexistentMonthFor2000To2099Test() {
        String pesel = "50330175277";
        expectedResponseForError_INVY_INVM(pesel, false, null, actualGender(pesel), "\"INVY\"", "\"Invalid year.\"", "\"INVM\"", "\"Invalid month.\"");
    }

    @Test
    public void peselWithError_INVY_INVM_NonexistentMonthFor2100To2199Test() {
        String pesel = "50530193109";
        expectedResponseForError_INVY_INVM(pesel, false, null, actualGender(pesel), "\"INVY\"", "\"Invalid year.\"", "\"INVM\"", "\"Invalid month.\"");
    }

    @Test
    public void peselWithError_INVY_INVM_NonexistentMonthFor2200To2299Test() {
        String pesel = "50730141979";
        expectedResponseForError_INVY_INVM(pesel, false, null, actualGender(pesel), "\"INVY\"", "\"Invalid year.\"", "\"INVM\"", "\"Invalid month.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthJanuary00th2034Test() {
        String pesel = "34210017002";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthJanuary32th1983Test() {
        String pesel = "82013232979";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthFerbuary30thForLeapYear2020Test() {
        String pesel = "20223068641";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthFerbuary29thForNonLeapYear2019Test() {
        String pesel = "19222931506";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthMarch32th1982Test() {
        String pesel = "82033261016";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthApril31th1979Test() {
        String pesel = "79043191484";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthMay32th1800Test() {
        String pesel = "00853280073";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthJune31th1899Test() {
        String pesel = "99863125047";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthJuly32th1996Test() {
        String pesel = "96073236396";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthAugust32th1965Test() {
        String pesel = "65083228903";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthSeptember31th2250Test() {
        String pesel = "50693122356";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthOctober32th2157Test() {
        String pesel = "57503260749";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthNovember31th1864Test() {
        String pesel = "64913157617";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVD_NonexistentDayOfMonthDecember32th2034Test() {
        String pesel = "34323238684";
        expectedResponseForError_INVD(pesel, false, null, actualGender(pesel), "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVY_INVM_INVD_NonexistentDataTest() {
        String pesel = "00000000000";
        expectedResponseForError_INVY_INVM_INVD(pesel, false, null, actualGender(pesel), "\"INVY\"", "\"Invalid year.\"", "\"INVM\"", "\"Invalid month.\"", "\"INVD\"", "\"Invalid day.\"");
    }

    @Test
    public void peselWithError_INVC_InvalidCheckSumTest() {
        String pesel = "48100779842";
        expectedResponseForError_INVC(pesel, false, actualDateOfBirth(pesel), actualGender(pesel), "\"INVC\"", "\"Check sum is invalid. Check last digit.\"");
    }
}