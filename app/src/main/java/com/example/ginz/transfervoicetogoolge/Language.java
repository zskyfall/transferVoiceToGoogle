package com.example.ginz.transfervoicetogoolge;

public class Language {
    private Integer flag;
    private String name;
    private String code;

    public Language(Integer flag, String name, String code) {
        this.flag = flag;
        this.name = name;
        this.code = code;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }
}
