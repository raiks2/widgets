package com.raiks.widgets.core.application.service;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.raiks.widgets.core.domain.Widget;

public final class WidgetBundle implements Bundle<Widget> {
    private final BundleSpecification bundleSpecification;
    private final List<Widget> elements;

    @JsonCreator
    public WidgetBundle(
        @JsonProperty("bundleSpecification") BundleSpecification bundleSpecification,
        @JsonProperty("elements") List<Widget> elements
    ) {
        this.bundleSpecification = bundleSpecification;
        this.elements = elements;
    }

    public BundleSpecification getBundleSpecification() {
        return bundleSpecification;
    }

    public List<Widget> getElements() {
        return elements;
    }
}
