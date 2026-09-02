package com.aeropelican.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.query.SortDirection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDTO {
    private Integer page;

    private Integer size;

    private String sortBy;

    private SortDirection sortDirection;

    public String getSortDir;

    public String getSortDir() {
        return null;
    }
}
