package com.johnpili.spring_request_mapping_extractor;

public class ExtractedMethodParam {
    private String type;
    private String name;
    private boolean required;
    private String defaultValue;

    public ExtractedMethodParam(String type, String name, boolean required, String defaultValue) {
        this.type = type;
        this.name = name;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
