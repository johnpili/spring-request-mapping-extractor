package com.johnpili.spring_request_mapping_extractor;

import java.util.ArrayList;
import java.util.List;

public class ExtractedHttpVerb {
    public ExtractedHttpVerb() {
        this.parameters = new ArrayList<>();
    }

    public List<ExtractedMethodParam> parameters;
}
