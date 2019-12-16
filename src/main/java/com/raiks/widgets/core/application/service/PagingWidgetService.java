package com.raiks.widgets.core.application.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.raiks.widgets.core.domain.Widget;

public final class PagingWidgetService implements IPagingWidgetService {
    private IWidgetService widgetService;
    private IPagingWidgetRepository pagingWidgetRepository;

    public PagingWidgetService(IWidgetService widgetService, IPagingWidgetRepository pagingWidgetRepository) {
        this.widgetService = widgetService;
        this.pagingWidgetRepository = pagingWidgetRepository;
    }

    @Override
    public Widget createWidget(Widget widget) {
        return widgetService.createWidget(widget);
    }

    @Override
    public Widget fetchWidget(String widgetGuid) {
        return widgetService.fetchWidget(widgetGuid);
    }

    @Override
    public List<Widget> fetchAllWidgets() {
        return widgetService.fetchAllWidgets();
    }

    @Override
    public void updateWidget(Widget updatedWidget) {
        widgetService.updateWidget(updatedWidget);
    }

    @Override
    public void deleteWidget(String widgetGuid) {
        widgetService.deleteWidget(widgetGuid);
    }

    @Override
    public Bundle<Widget> fetchManyWidgets(BundleSpecification bundleSpecfication) {
        return pagingWidgetRepository.fetchManyWidgets(bundleSpecfication);
    }
}
