
package com.example.nanorus.gmobytesttask.model.pojo.api;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestPojo {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private List<DatumPojo> data = null;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DatumPojo> getData() {
        return data;
    }

    public void setData(List<DatumPojo> data) {
        this.data = data;
    }

}
