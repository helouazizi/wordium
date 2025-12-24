// src/main/java/com/wordium/posts/dto/PaginationRequest.java
package com.wordium.posts.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.validation.constraints.Min;

public record PaginationRequest(
    @Min(value = 0, message = "Page number must be >= 0")
    Integer page,  

    @Min(value = 1, message = "Page size must be >= 1")
    Integer size,  // optional, default 20

    String sortBy,     // optional field name, e.g., "createdAt"
    String sortDir     // optional: "asc" or "desc"
) {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final String DEFAULT_SORT_BY = "createdAt";
    private static final String DEFAULT_SORT_DIR = "desc";

    public Pageable toPageable() {
        int actualPage = (page != null && page >= 0) ? page : DEFAULT_PAGE;
        int actualSize = (size != null && size >= 1) ? size : DEFAULT_SIZE;
        String actualSortBy = (sortBy != null && !sortBy.isBlank()) ? sortBy : DEFAULT_SORT_BY;
        String actualSortDir = (sortDir != null && sortDir.equalsIgnoreCase("asc")) ? "asc" : "desc";

        Sort sort = Sort.by(Sort.Direction.fromString(actualSortDir), actualSortBy);

        return PageRequest.of(actualPage, actualSize, sort);
    }
}