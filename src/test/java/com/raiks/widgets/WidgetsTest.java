package com.raiks.widgets;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.raiks.widgets.core.domain.Point;
import com.raiks.widgets.core.domain.Widget;
import com.raiks.widgets.core.application.service.WidgetBundle;

import io.restassured.RestAssured;

import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import org.hamcrest.Matchers;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Widgets.class, webEnvironment = WebEnvironment.DEFINED_PORT)
/**
 * This is an integrations test that starts up an applications and calls it endpoints
 */
public class WidgetsTest {
    private static RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    private static final String BASE_URL = "http://localhost:8089";
    private static final String WIDGET_BASE_URL = BASE_URL + "/widget";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @After
    public void teardown() {
        purgeWidgets();
    }

    /**
     * Ideally, we should setup the test data by using class collaborators to avoid cascading failures and
     * double testing. However, in case of in-memory persistence we can't do that, so setup and teardown are
     * done in terms of the application itself (by calling its endpoints)
     */
    private void purgeWidgets() {
        ResponseEntity<WidgetBundle> responseEntity = restTemplate.getForEntity(WIDGET_BASE_URL, WidgetBundle.class);
        List<Widget> widgets = responseEntity.getBody().getElements();
        widgets.forEach(
            widget ->  {
                String deleteWidgetUrl = WIDGET_BASE_URL + "/" + widget.getGuid();
                restTemplate.delete(deleteWidgetUrl);
            }
        );
    }

    @Test
    public void test_givenWidgetDoesntExist_whenCreateWidgetIsCalled_thenItReturns200AndBodyIsCorrect() throws JsonProcessingException {
        Widget widget = new Widget(new Point(10, 20), 100, 150, 3);
        HttpEntity<String> request = createRequestEntity(widget);
        ResponseEntity<Widget> response = restTemplate.exchange(WIDGET_BASE_URL, HttpMethod.POST, request, Widget.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody().getGuid());
        Assert.assertEquals(widget.getBottomLeftCorner().getX(), response.getBody().getBottomLeftCorner().getX());
        Assert.assertEquals(widget.getBottomLeftCorner().getY(), response.getBody().getBottomLeftCorner().getY());
        Assert.assertEquals(widget.getWidth(), response.getBody().getWidth());
        Assert.assertEquals(widget.getHeight(), response.getBody().getHeight());
    }

    @Test
    public void test_givenNonExistentGuid_whenFetchWidgetIsCalledWithIt_thenItReturns404() {
        String url = WIDGET_BASE_URL + "/{widgetGuid}";
        RestAssured.given()
            .pathParam("widgetGuid", "AA-BB-CC")
            .when().get(url)
            .then().assertThat()
            .statusCode(404);
    }

    @Test
    public void test_givenOneWidget_whenFetchWidgetIsCalled_thenItReturnsThatWidget() throws JsonProcessingException {
        Widget widget = new Widget(new Point(10, 20), 100, 150, 333);
        HttpEntity<String> request = createRequestEntity(widget);
        ResponseEntity<Widget> response = restTemplate.exchange(WIDGET_BASE_URL, HttpMethod.POST, request, Widget.class);
        String url = WIDGET_BASE_URL + "/{widgetGuid}";
        RestAssured.given()
            .pathParam("widgetGuid", response.getBody().getGuid())
            .when().get(url)
            .then().assertThat()
            .statusCode(200)
            .body("bottomLeftCorner.x", Matchers.is(10))
            .body("bottomLeftCorner.y", Matchers.is(20))
            .body("width", Matchers.is(100))
            .body("height", Matchers.is(150))
            .body("guid", Matchers.is(response.getBody().getGuid()))
            .body("zIndex", Matchers.is(333));
    }

    @Test
    public void test_givenTwoWidgetsExist_whenFetchManyWidgetsIsCalled_thenItReturns200AndBodyIsCorrect() throws JsonProcessingException {
        Widget w1 = new Widget(new Point(10, 20), 100, 150, 3);
        Widget w2 = new Widget(new Point(100, 200), 300, 250, 5);
        restTemplate.exchange(WIDGET_BASE_URL, HttpMethod.POST, createRequestEntity(w1), Widget.class);
        restTemplate.exchange(WIDGET_BASE_URL, HttpMethod.POST, createRequestEntity(w2), Widget.class);
        RestAssured
            .when().get(WIDGET_BASE_URL)
            .then().assertThat()
            .statusCode(200)
            .body("elements.size()", Matchers.is(2))
            .body("elements[0].width", Matchers.equalTo(100));
    }

    @Test
    public void test_givenTwoWidgetsExist_whenFirstPageOfSizeOneRequested_thenItReturns200AndOneWidget() throws JsonProcessingException {
        Widget w1 = new Widget(new Point(10, 20), 100, 150, 3);
        Widget w2 = new Widget(new Point(100, 200), 300, 250, 5);
        restTemplate.exchange(WIDGET_BASE_URL, HttpMethod.POST, createRequestEntity(w1), Widget.class);
        restTemplate.exchange(WIDGET_BASE_URL, HttpMethod.POST, createRequestEntity(w2), Widget.class);
        RestAssured
            .given()
            .queryParam("page", 0)
            .queryParam("size", 1)
            .when().get(WIDGET_BASE_URL)
            .then().assertThat()
            .statusCode(200)
            .body("elements.size()", Matchers.is(1));
    }

    @Test
    public void test_givenOneWidget_whenUpdateWidgetIsCalled_thenItGetsUpdatedCorrectly() throws JsonProcessingException {
        Widget w1 = new Widget(new Point(10, 20), 100, 150, 3);
        w1 = restTemplate.postForObject(WIDGET_BASE_URL, createRequestEntity(w1), Widget.class);
        String updateUrl = WIDGET_BASE_URL + "/" + w1.getGuid();
        int newWidth = w1.getWidth() * 2;
        restTemplate.exchange(updateUrl, HttpMethod.PATCH, createRequestEntity(w1.setWidth(newWidth)), Widget.class);
        String raUrl = WIDGET_BASE_URL + "/{widgetGuid}";
        RestAssured.given()
            .pathParam("widgetGuid", w1.getGuid())
            .when().get(raUrl)
            .then().assertThat()
            .statusCode(200)
            .body("width", Matchers.equalTo(200));
    }

    @Test
    public void test_givenOneWidget_whenDeleteWidgetIsCalled_thenItGetsDeleted() throws JsonProcessingException {
        Widget w1 = new Widget(new Point(10, 20), 100, 150, 3);
        w1 = restTemplate.postForObject(WIDGET_BASE_URL, createRequestEntity(w1), Widget.class);
        String widgetUrl = WIDGET_BASE_URL + "/" + w1.getGuid();
        restTemplate.delete(widgetUrl);
        ResponseEntity<WidgetBundle> responseEntity = restTemplate.getForEntity(WIDGET_BASE_URL, WidgetBundle.class);
        List<Widget> widgets = responseEntity.getBody().getElements();
        Assert.assertTrue(widgets.isEmpty());
    }

    private HttpEntity<String> createRequestEntity(Widget widget) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(OBJECT_MAPPER.writeValueAsString(widget), headers);
        return request;
    }
}
