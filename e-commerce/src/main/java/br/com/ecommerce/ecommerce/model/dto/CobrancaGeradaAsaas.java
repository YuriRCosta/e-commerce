package br.com.ecommerce.ecommerce.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CobrancaGeradaAsaas {

    private String object;
    private boolean hasMore;
    private Integer totalCount;
    private Integer limit;
    private Integer offset;
    private List<DataCobrancaGeradaAsaas> data = new ArrayList<>();

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public List<DataCobrancaGeradaAsaas> getData() {
        return data;
    }

    public void setData(List<DataCobrancaGeradaAsaas> data) {
        this.data = data;
    }
}
