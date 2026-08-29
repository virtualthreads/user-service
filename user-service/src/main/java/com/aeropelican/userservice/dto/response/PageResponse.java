package com.aeropelican.userservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElement;
    private  int totalPage;
    private boolean hasNext;
    private  boolean hasPrevious;
}
