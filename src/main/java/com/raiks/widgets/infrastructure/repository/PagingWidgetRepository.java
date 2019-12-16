package com.raiks.widgets.infrastructure.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.raiks.widgets.core.application.service.IPagingWidgetRepository;
import com.raiks.widgets.core.application.service.Bundle;
import com.raiks.widgets.core.application.service.BundleSpecification;
import com.raiks.widgets.core.application.service.WidgetBundle;
import com.raiks.widgets.core.application.service.IWidgetRepository;
import com.raiks.widgets.core.domain.Widget;

public final class PagingWidgetRepository implements IPagingWidgetRepository {
    private IWidgetRepository widgetRepository;

    public PagingWidgetRepository(
            IWidgetRepository widgetRepository
    ) {
        this.widgetRepository = widgetRepository;
    }

    @Override
    public void createWidget(Widget widget) {
        widgetRepository.createWidget(widget);
    }

    @Override
    public Widget fetchWidget(String widgetGuid) {
        return widgetRepository.fetchWidget(widgetGuid);
    }

    @Override
    public void updateWidget(Widget updatedWidget) {
        widgetRepository.updateWidget(updatedWidget);
    }

    @Override
    public void deleteWidget(String widgetGuid) {
        widgetRepository.deleteWidget(widgetGuid);
    }

    @Override
    public List<Widget> fetchAllWidgets() {
        return widgetRepository.fetchAllWidgets();
    }

    @Override
    public Bundle<Widget> fetchManyWidgets(BundleSpecification bundleSpecification) {
        List<Widget> allWidgets = widgetRepository.fetchAllWidgets();
        int numElementsPerPage = bundleSpecification.getNumElementsPerPage();
        int startWidgetIndex = bundleSpecification.getPageNumber() * numElementsPerPage;
        List<Widget> widgetPage = allWidgets
            .stream()
            .skip(startWidgetIndex)
            .limit(numElementsPerPage)
            .collect(Collectors.toList());
        return new WidgetBundle(new BundleSpecification(startWidgetIndex, numElementsPerPage), widgetPage);
    }
}
