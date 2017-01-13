package com.shaw.vo;

/**
 * Created by shaw on 2017/1/13 0013.
 */
public enum RemoteTaskPermission {
    DOWNLOAD("Download", 1),
    PYTHON("Python", 2);

    private String name;
    private Integer value;

    RemoteTaskPermission(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
