package com.example.mytodo.database.model;

/**
 * Created by ssaraf on 8/24/15.
 */
public class Item {
    private Long id;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
