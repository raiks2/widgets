package com.raiks.widgets.core.application.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.raiks.widgets.infrastructure.repository.FakeWidgetRepository;
import com.raiks.widgets.core.domain.Point;
import com.raiks.widgets.core.domain.Widget;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleWidgetServiceTest {
    private static IWidgetRepository repository;
    private static IWidgetService service;

    @Before
    public void recreateFixture() {
        repository = new FakeWidgetRepository();
        service = new SimpleWidgetService(repository);
    }

    @Test
    public void test_givenWidgetWithMissingZIndex_whenCreateWidgetIsCalled_thenWidgetIsMovedToForeground() {
        int zIndex = 5;
        service.createWidget(new Widget(new Point(100, 200), 50, 100, zIndex));
        Widget w2 = service.createWidget(
            new Widget(new Point(150, 250), 60, 150)
        );
        w2 = repository.fetchWidget(w2.getGuid());
        boolean isForegroundWidget = w2.getZIndex() == zIndex + 1;
        Assert.assertTrue(isForegroundWidget);
    }

    @Test
    public void test_givenWidgetWithNoZIndex_whenCreateWidgetIsCalled_thenExistingWidgetsAreShiftedUpward() {
        Widget w1 = service.createWidget(new Widget(new Point(100, 200), 50, 100, 15));
        Widget w2 = service.createWidget(new Widget(new Point(50, 20), 700, 300, 16));
        Widget w3 = service.createWidget(new Widget(new Point(150, 250), 70, 150, 15));
        w1 = repository.fetchWidget(w1.getGuid());
        w2 = repository.fetchWidget(w2.getGuid());
        Assert.assertEquals((int)w1.getZIndex(), 16);
        Assert.assertEquals((int)w2.getZIndex(), 17);
    }

    @Test
    public void test_whenCreateWidgetIsCalled_thenFetchWidgetReturnsItWithAssignedGuid() {
        Widget widget = new Widget(new Point(100, 200), 50, 100, 10);
        widget = service.createWidget(widget);
        widget = repository.fetchWidget(widget.getGuid());
        Assert.assertNotNull(widget.getGuid());
    }

    @Test
    public void test_givenOneWidget_whenFetchWidgetIsCalled_thenItReturnsThatWidget() {
        Point bottomLeftCorner = new Point(100, 200);
        Widget widget = new Widget(bottomLeftCorner, 50, 100, 10);
        repository.createWidget(widget);
        widget = service.fetchWidget(widget.getGuid());
        Assert.assertEquals(bottomLeftCorner.getX(), widget.getBottomLeftCorner().getX());
        Assert.assertEquals(bottomLeftCorner.getY(), widget.getBottomLeftCorner().getY());
        Assert.assertEquals(50, (int)widget.getWidth());
        Assert.assertEquals(100, (int)widget.getHeight());
        Assert.assertEquals(10, (int)widget.getZIndex());
    }

    @Test
    public void test_whenUpdateWidgetIsCalled_thenWidgetIsUpdatedSuccessfully() {
        Widget widget = new Widget(new Point(100, 200), 50, 100, 10);
        repository.createWidget(widget);
        service.updateWidget(widget.setWidth(300));
        widget = service.fetchWidget(widget.getGuid());
        Assert.assertEquals(300, widget.getWidth());
    }

    public void test_givenOneWidget_whenDeleteWidgetIsCalled_thenFetchAllWidgetsReturnsEmptyList() {
        String guid = "ABCDEF";
        Widget widget = new Widget(new Point(100, 200), 50, 100, 10, guid);
        repository.createWidget(widget);
        service.deleteWidget(guid);
        Assert.assertTrue(service.fetchAllWidgets().isEmpty());
    }
}
