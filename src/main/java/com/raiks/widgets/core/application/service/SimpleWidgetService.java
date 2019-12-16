package com.raiks.widgets.core.application.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.raiks.widgets.core.domain.Widget;

public final class SimpleWidgetService implements IWidgetService {
    private IWidgetRepository widgetRepository;

    public SimpleWidgetService(IWidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    @Override
    public Widget createWidget(Widget widget) {
        widget = widget.setGuid(generateGuid());

        if (widgetWantsToBeInForeground(widget)) {
            int foregroundZIndex = getForegroundZIndex();
            widget = widget.setZIndex(foregroundZIndex + 1);
        } else {
            if (zIndexOccupiedForWidget(widget.getZIndex())) {
                shiftWidgetsWithZIndexEqualOrAbove(widget.getZIndex()).stream()
                    .forEach(widgetRepository::updateWidget);
            }
        }

        widgetRepository.createWidget(widget);
        return widget;
    }

    @Override
    public Widget fetchWidget(String widgetGuid) {
        return widgetRepository.fetchWidget(widgetGuid);
    }

    @Override
    public List<Widget> fetchAllWidgets() {
        return widgetRepository.fetchAllWidgets();
    }

    @Override
    public void updateWidget(Widget updatedWidget) {
        widgetRepository.updateWidget(updatedWidget);
    }

    @Override
    public void deleteWidget(String widgetGuid) {
        widgetRepository.deleteWidget(widgetGuid);
    }

    private String generateGuid() {
        return UUID.randomUUID().toString();
    }

    private boolean widgetWantsToBeInForeground(Widget widget) {
        return widget.getZIndex() == null;
    }

    private int getForegroundZIndex() {
        int foregroundZIndex = widgetRepository.fetchAllWidgets().stream()
            .map(w -> w.getZIndex())
            .max(Comparator.naturalOrder())
            .orElse(IWidgetService.INITIAL_WIDGET_ZINDEX);
        return foregroundZIndex;
    }

    private boolean zIndexOccupiedForWidget(int zIndex) {
        long numWidgetsWithGivenZIndex = widgetRepository.fetchAllWidgets().stream()
            .filter(w -> w.getZIndex() == zIndex)
            .count();
        if (numWidgetsWithGivenZIndex > 1) {
            throw new IllegalStateException("Widgets must not have the same zIndex");
        }
        boolean exactlyOneWidgetWithGivenZIndexExists = numWidgetsWithGivenZIndex == 1;
        return exactlyOneWidgetWithGivenZIndexExists;
    }

    private List<Widget> shiftWidgetsWithZIndexEqualOrAbove(int zIndex) {
        return widgetRepository.fetchAllWidgets().stream()
            .filter(w -> w.getZIndex() >= zIndex)
            .map(w -> w.setZIndex(w.getZIndex() + 1))
            .collect(Collectors.toList());
    }
}
