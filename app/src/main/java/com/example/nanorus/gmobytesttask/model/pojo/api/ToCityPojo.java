
package com.example.nanorus.gmobytesttask.model.pojo.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ToCityPojo {

    @SerializedName("highlight")
    @Expose
    private int highlight;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;

    public int getHighlight() {
        return highlight;
    }

    public void setHighlight(int highlight) {
        this.highlight = highlight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
