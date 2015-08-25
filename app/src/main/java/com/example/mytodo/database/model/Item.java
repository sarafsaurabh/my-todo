package com.example.mytodo.database.model;

import java.sql.Date;

/**
 * Created by ssaraf on 8/24/15.
 */
public class Item {
    private Long id;
    private String value;
    private Date dueDate;

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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
