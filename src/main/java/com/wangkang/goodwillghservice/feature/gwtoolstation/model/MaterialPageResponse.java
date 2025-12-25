package com.wangkang.goodwillghservice.feature.gwtoolstation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MaterialPageResponse {

    @JsonProperty("_embedded")
    private Embedded embedded;

    private Page page;

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public static class Embedded {
        private List<MaterialDTO> materialList;

        public List<MaterialDTO> getMaterialList() {
            return materialList;
        }

        public void setMaterialList(List<MaterialDTO> materialList) {
            this.materialList = materialList;
        }
    }

    public static class Page {
        private int size;
        private long totalElements;
        private int totalPages;
        private int number;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(long totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}