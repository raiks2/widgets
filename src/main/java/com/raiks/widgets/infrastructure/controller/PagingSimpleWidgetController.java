package com.raiks.widgets.infrastructure.controller;

import java.util.List;
import java.util.Optional;

import com.raiks.widgets.core.application.service.Bundle;
import com.raiks.widgets.core.application.service.BundleSpecification;
import com.raiks.widgets.core.application.service.IPagingWidgetService;
import com.raiks.widgets.core.domain.Widget;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class PagingSimpleWidgetController {
    private SimpleWidgetController simpleWidgetController;
    private IPagingWidgetService pagingWidgetService;

    public PagingSimpleWidgetController(SimpleWidgetController simpleWidgetController, IPagingWidgetService pagingWidgetService) {
        this.simpleWidgetController = simpleWidgetController;
        this.pagingWidgetService = pagingWidgetService;
    }

    @RequestMapping(value = "/widget", method = RequestMethod.POST)
    public Widget createWidget(@RequestBody Widget widget) {
        return simpleWidgetController.createWidget(widget);
    }

    @RequestMapping(value = "/widget/{guid}", method = RequestMethod.GET)
    public Widget fetchWidget(@PathVariable("guid") String guid) {
        return simpleWidgetController.fetchWidget(guid);
    }

    @RequestMapping(value = "/widget", method = RequestMethod.GET)
    public Bundle<Widget> fetchManyWidgets(
        @RequestParam("page") Optional<Integer> pageNumber,
        @RequestParam("size") Optional<Integer> numElementsPerPage
    ) {
        return pagingWidgetService.fetchManyWidgets(
            new BundleSpecification(
                pageNumber.orElse(0),
                numElementsPerPage.orElse(10)
            )
        );
    }

    @RequestMapping(value = "/widget/{guid}", method = RequestMethod.PATCH)
    public void updateWidget(
        @PathVariable("guid") String guid,
        @RequestBody Widget updatedWidget
    ) {
        simpleWidgetController.updateWidget(guid, updatedWidget);
    }

    @RequestMapping(value = "/widget/{guid}", method = RequestMethod.DELETE)
    public void deleteWidget(@PathVariable("guid") String guid) {
        simpleWidgetController.deleteWidget(guid);
    }
}
