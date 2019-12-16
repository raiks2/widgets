package com.raiks.widgets.infrastructure.repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.raiks.widgets.core.application.service.IWidgetRepository;
import com.raiks.widgets.core.domain.Point;
import com.raiks.widgets.core.domain.Widget;

import org.junit.Assert;
import org.junit.Test;

public class InMemoryWidgetRepositoryTest {
    @Test(expected = IllegalArgumentException.class)
    public void test_whenCreateWidgetIsCalledWithMissingGuid_thenExceptionIsThrown() {
        IWidgetRepository repository = new InMemoryWidgetRepository();
        repository.createWidget(
            new Widget(new Point(10, 20), 100, 150, 3)
        );
    }

    @Test(expected = NoSuchElementException.class)
    public void test_givenWidgetWithNonExistentGuid_whenFetchWidgetIsCalled_thenExceptionIsThrown() {
        IWidgetRepository repository = new InMemoryWidgetRepository();
        String nonExistentGuid = "AAA-BBB-CCC";
        repository.fetchWidget(nonExistentGuid);
    }

    @Test
    public void test_whenUpdateWidgetIsCalled_thenItCopiesAllFieldsCorrectly() {
        IWidgetRepository repository = new InMemoryWidgetRepository();
        String guid = generateGuid();
        repository.createWidget(new Widget(new Point(10, 20), 100, 150, 3, guid));
        Widget updatedWidget = new Widget(new Point(100, 250), 100, 150, 2, guid);
        repository.updateWidget(updatedWidget);
        Widget widget = repository.fetchWidget(guid);
        Assert.assertEquals(widget.getBottomLeftCorner(), updatedWidget.getBottomLeftCorner());
        Assert.assertEquals(widget.getWidth(), updatedWidget.getWidth());
        Assert.assertEquals(widget.getHeight(), updatedWidget.getHeight());
        Assert.assertEquals(widget.getZIndex(), updatedWidget.getZIndex());
    }

    @Test(expected = NoSuchElementException.class)
    public void test_whenAttemptIsMadeToFetchDeletedWidget_thenExceptionIsThrown() {
        IWidgetRepository repository = new InMemoryWidgetRepository();
        String guid = generateGuid();
        repository.createWidget(new Widget(new Point(10, 20), 100, 150, 3, guid));
        repository.deleteWidget(guid);
        repository.fetchWidget(guid);
    }

    @Test
    public void test_givenRandomlyInsertedWidgets_whenFetchAllWidgetsIsCalled_thenItReturnsThemSorted() {
        IWidgetRepository repository = new InMemoryWidgetRepository();
        repository.createWidget(new Widget(new Point(100, 200), 50, 100, 7, "ABCDEF1"));
        repository.createWidget(new Widget(new Point(50, 20), 50, 100, 1, "ABCDEF2"));
        repository.createWidget(new Widget(new Point(50, 20), 700, 300, 5, "ABCDEF3"));
        List<Widget> sortedWidgets = repository.fetchAllWidgets();
        Assert.assertEquals(1, (int)sortedWidgets.get(0).getZIndex());
        Assert.assertEquals(7, (int)sortedWidgets.get(sortedWidgets.size() - 1).getZIndex());
    }

    private String generateGuid() {
        return UUID.randomUUID().toString();
    }

}
