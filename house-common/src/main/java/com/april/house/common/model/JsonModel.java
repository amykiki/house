package com.april.house.common.model;

import java.io.Serializable;

public class JsonModel implements Serializable {
    private String[] to;

    /*public void setTo(String to) {
        this.to = new String[] {to};
    }
*/
    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getTo() {
        return this.to;
    }
}
