package com.johnpili.spring_request_mapping_extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractedMethod {
    public ExtractedMethod() {
        this.mappings = new ArrayList<>();
        this.produces = new ArrayList<>();
        this.verbs = new HashMap<>();
    }

    private String methodName;

    public List<String> mappings;
    public List<String> produces;
    public Map<String, ExtractedHttpVerb> verbs;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
