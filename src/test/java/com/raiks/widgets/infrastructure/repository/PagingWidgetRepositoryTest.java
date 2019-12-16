package com.raiks.widgets.infrastructure.repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.raiks.widgets.core.application.service.Bundle;
import com.raiks.widgets.core.application.service.BundleSpecification;
import com.raiks.widgets.core.application.service.IPagingWidgetRepository;
import com.raiks.widgets.core.domain.Point;
import com.raiks.widgets.core.domain.Widget;

import org.junit.Assert;
import org.junit.Test;

public class PagingWidgetRepositoryTest {
    @Test
    public void test_givenTwoWidgets_whenFetchManyIsCalledWithSinglePageOfSizeOne_thenItReturnsOneWidget() {
        IPagingWidgetRepository repository = new PagingWidgetRepository(new FakeWidgetRepository());
        repository.createWidget(new Widget(new Point(10, 20), 100, 150, 3, generateGuid()));
        repository.createWidget(new Widget(new Point(100, 200), 200, 150, 5, generateGuid()));
        Bundle<Widget> widgetBundle = repository.fetchManyWidgets(new BundleSpecification(0, 1));
        Assert.assertEquals(1, widgetBundle.getBundleSpecification().getNumElementsPerPage());
    }

    @Test
    public void test_givenTwoWidgets_whenFetchManyIsCalledWithSinglePageOfSizeTwo_thenItReturnsTwoWidgets() {
        IPagingWidgetRepository repository = new PagingWidgetRepository(new FakeWidgetRepository());
        repository.createWidget(new Widget(new Point(10, 20), 100, 150, 3, generateGuid()));
        repository.createWidget(new Widget(new Point(100, 200), 200, 150, 5, generateGuid()));
        Bundle<Widget> widgetBundle = repository.fetchManyWidgets(new BundleSpecification(0, 2));
        Assert.assertEquals(2, widgetBundle.getBundleSpecification().getNumElementsPerPage());
    }

    @Test
    public void test_givenOneWidget_whenFetchManyIsCalledWithPageOutOfRange_thenItReturnsNoWidgets() {
        IPagingWidgetRepository repository = new PagingWidgetRepository(new FakeWidgetRepository());
        repository.createWidget(new Widget(new Point(10, 20), 100, 150, 3, generateGuid()));
        Bundle<Widget> widgetBundle = repository.fetchManyWidgets(new BundleSpecification(1, 0));
        Assert.assertEquals(0, widgetBundle.getBundleSpecification().getNumElementsPerPage());
        Assert.assertTrue(widgetBundle.getElements().isEmpty());
    }

    private String generateGuid() {
        return UUID.randomUUID().toString();
    }
}
