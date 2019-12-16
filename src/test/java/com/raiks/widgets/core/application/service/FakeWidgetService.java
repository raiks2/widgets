package com.raiks.widgets.core.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.raiks.widgets.core.domain.Widget;

public final class FakeWidgetService implements IWidgetService {
    private IWidgetRepository widgetRepository;

    public FakeWidgetService(IWidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    public Widget createWidget(Widget widget) {
        widget = widget.setGuid(generateGuid());
        widgetRepository.createWidget(widget);
        return widget;
    }

    public Widget fetchWidget(String widgetGuid) {
        return widgetRepository.fetchWidget(widgetGuid);
    }

    public List<Widget> fetchAllWidgets() {
        return new ArrayList<>(widgetRepository.fetchAllWidgets());
    }

    public void updateWidget(Widget updatedWidget) {
        widgetRepository.updateWidget(updatedWidget);
    }

    public void deleteWidget(String widgetGuid) {
        widgetRepository.deleteWidget(widgetGuid);
    }

    private String generateGuid() {
        return UUID.randomUUID().toString();
    }
}
