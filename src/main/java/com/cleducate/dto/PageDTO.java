package com.cleducate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {

    private Object content;
    private long size;
    private  int totalPages;
    private long totalRecords;
    private boolean last;


    public PageDTO(Page page) {
        this.content = page.getContent();
        this.size = page.getNumberOfElements();
        this.totalPages = page.getTotalPages();
        this.totalRecords = page.getTotalElements();
        this.last = page.isLast();
    }



}
