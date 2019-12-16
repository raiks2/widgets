package com.raiks.widgets.infrastructure.controller;

import java.util.List;
import java.util.NoSuchElementException;

import com.raiks.widgets.core.application.service.IWidgetService;
import com.raiks.widgets.core.domain.Widget;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

public final class SimpleWidgetController {
    private IWidgetService widgetService;

    public SimpleWidgetController(IWidgetService widgetService) {
        this.widgetService = widgetService;
    }

    public Widget createWidget(Widget widget) {
        return widgetService.createWidget(widget);
    }

    public Widget fetchWidget(String guid) {
        try {
            return widgetService.fetchWidget(guid);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, createWidgetDoesntExistMsg(guid), e);
        }
    }

    public List<Widget> fetchAllWidgets() {
        return widgetService.fetchAllWidgets();
    }

    public void updateWidget(
        String guid,
        Widget updatedWidget
    ) {
        if (!guid.equals(updatedWidget.getGuid())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Guid in path param is not equal to the guid in the payload");
        }
        try {
            widgetService.updateWidget(updatedWidget);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, createWidgetDoesntExistMsg(guid), e);
        }
    }

    public void deleteWidget(String guid) {
        try {
            widgetService.deleteWidget(guid);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, createWidgetDoesntExistMsg(guid), e);
        }
    }

    private String createWidgetDoesntExistMsg(String widgetGuid) {
        return "Widget with guid " + widgetGuid + " doesn't exist";
    }
}
