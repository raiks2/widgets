package com.raiks.widgets.core.application.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class BundleSpecification {
    private final int pageNumber;
    private final int numElementsPerPage;

    @JsonCreator
    public BundleSpecification(
        @JsonProperty("pageNumber") int pageNumber,
        @JsonProperty("numElementsPerPage") int numElementsPerPage
     ) {
        this.pageNumber = pageNumber;
        this.numElementsPerPage = numElementsPerPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getNumElementsPerPage() {
        return numElementsPerPage;
    }
}
