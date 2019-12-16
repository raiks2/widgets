package com.raiks.widgets.core.application.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.raiks.widgets.infrastructure.controller.SimpleWidgetController;
import com.raiks.widgets.infrastructure.repository.FakeWidgetRepository;
import com.raiks.widgets.core.domain.Point;
import com.raiks.widgets.core.domain.Widget;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.web.server.ResponseStatusException;

public class SimpleWidgetControllerTest {
    private static IWidgetRepository repository;
    private static IWidgetService service;
    private static SimpleWidgetController controller;

    private static final String NONEXISTENT_GUID = "AAA-BBB-CCC";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        recreateFixture();
    }

    private void recreateFixture() {
        repository = new FakeWidgetRepository();
        service = new SimpleWidgetService(repository);
        controller = new SimpleWidgetController(service);
    }

    @Test
    public void test_whenCreateWidgetIsCalled_thenFetchWidgetReturnsThatWidget() {
        Widget widget = new Widget(new Point(10, 20), 100, 150, 3);
        String guid = controller.createWidget(widget).getGuid();
        widget = service.fetchWidget(guid);
        Assert.assertEquals(guid, widget.getGuid());
    }

    @Test
    public void test_givenNonExistentGuid_whenFetchWidgetIsCalled_thenItThrowsAnException() {
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("Widget with guid " + NONEXISTENT_GUID + " doesn't exist");
        controller.fetchWidget(NONEXISTENT_GUID);
    }

    @Test
    public void test_givenExistingWidget_whenUpdateWidgetIsCalledForIt_thenItGetsUpdatedCorrectly() {
        Widget widget = new Widget(new Point(10, 20), 100, 150, 3);
        widget = service.createWidget(widget);
        controller.updateWidget(widget.getGuid(), new Widget(new Point(10, 20), 100, 150, 30, widget.getGuid()));
        widget = service.fetchWidget(widget.getGuid());
        Assert.assertEquals(30, (int)widget.getZIndex());
    }

    @Test
    public void test_givenNonExistentWidget_whenUpdateWidgetIsCalledForIt_thenItThrowsException() {
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("Widget with guid " + NONEXISTENT_GUID + " doesn't exist");
        controller.updateWidget(NONEXISTENT_GUID, new Widget(new Point(10, 20), 100, 150, 3, NONEXISTENT_GUID));
    }

    @Test
    public void test_givenOneWidget_whenUpdateWidgetIsCalledAndGuidsInParamAndBodyDiffer_thenItThrowsException() {
        Widget widget = new Widget(new Point(10, 20), 100, 150, 3);
        widget = service.createWidget(widget);
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("Guid in path param is not equal to the guid in the payload");
        controller.updateWidget(NONEXISTENT_GUID, widget.setZIndex(10));
    }

    @Test
    public void test_givenExistingWidget_whenDeleteWidgetIsCalledForIt_thenItGetsDeleted() {
        Widget widget = new Widget(new Point(10, 20), 100, 150, 3);
        widget = service.createWidget(widget);
        service.deleteWidget(widget.getGuid());
        List<Widget> widgets = controller.fetchAllWidgets();
        Assert.assertTrue(widgets.isEmpty());
    }

    @Test
    public void test_givenNonExistentWidget_whenDeleteWidgetIsCalledForIt_thenItThrowsException() {
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("Widget with guid " + NONEXISTENT_GUID + " doesn't exist");
        controller.deleteWidget(NONEXISTENT_GUID);
    }

}
