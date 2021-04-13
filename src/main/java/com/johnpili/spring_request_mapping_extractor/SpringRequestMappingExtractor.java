package com.johnpili.spring_request_mapping_extractor;

import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author John Pili
 * @since 2021-04-12
 */

public class SpringRequestMappingExtractor {

    AbstractHandlerMethodMapping abstractHandlerMethodMapping;

    public SpringRequestMappingExtractor(ApplicationContext applicationContext) throws Exception {
        if(applicationContext == null) {
            throw new Exception("applicationContext is null");
        }
        this.abstractHandlerMethodMapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
    }

    public Map<String, Map<String, ExtractedMethod>> ExtractRequestMappings() throws Exception {
        if(this.abstractHandlerMethodMapping == null) {
            throw new Exception("abstractHandlerMethodMapping is null");
        }

        Map<String, Map<String, ExtractedMethod>> controllers = new HashMap<>();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = this.abstractHandlerMethodMapping.getHandlerMethods();
        for (Map.Entry item : handlerMethods.entrySet()) {
            RequestMappingInfo requestMappingInfo = (RequestMappingInfo) item.getKey();
            HandlerMethod handlerMethod = (HandlerMethod) item.getValue();

            if (controllers.containsKey(handlerMethod.getBean().toString())) {
                Map<String, ExtractedMethod> methods = controllers.get(handlerMethod.getBean().toString());
                if (methods != null) {
                    if (!methods.containsKey(handlerMethod.getMethod().getName())) {
                        ExtractedMethod method = new ExtractedMethod();
                        method.setMethodName(handlerMethod.getMethod().getName());

                        injectMapping(requestMappingInfo, method);
                        injectHttpVerb(requestMappingInfo, handlerMethod, method);
                        injectProduce(requestMappingInfo, method);

                        methods.put(handlerMethod.getMethod().getName(), method);
                    } else {
                        ExtractedMethod method = methods.get(handlerMethod.getMethod().getName());

                        injectMapping(requestMappingInfo, method);
                        injectHttpVerb(requestMappingInfo, handlerMethod, method);
                        injectProduce(requestMappingInfo, method);

                        methods.put(handlerMethod.getMethod().getName(), method);
                    }
                }
            } else {
                Map<String, ExtractedMethod> methods = new HashMap<>();
                ExtractedMethod method = new ExtractedMethod();
                method.setMethodName(handlerMethod.getMethod().getName());
                methods.put(handlerMethod.getMethod().getName(), method);

                injectMapping(requestMappingInfo, method);
                injectHttpVerb(requestMappingInfo, handlerMethod, method);
                injectProduce(requestMappingInfo, method);

                controllers.put(handlerMethod.getBean().toString(), methods);
            }
        }
        return controllers;
    }

    private List<ExtractedMethodParam> extractMethodParameters(HandlerMethod handlerMethod) {
        List<ExtractedMethodParam> parameters = new ArrayList<>();
        for (MethodParameter methodParameter : handlerMethod.getMethodParameters()) {
            Annotation a = methodParameter.getParameterAnnotation(RequestParam.class);
            if (a != null) {
                ExtractedMethodParam extractedMethodParam = new ExtractedMethodParam(methodParameter.getParameter().getParameterizedType().getTypeName(),
                        ((RequestParam) a).value(),
                        ((RequestParam) a).required(),
                        (((RequestParam) a).defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n") ? "" : ((RequestParam) a).defaultValue()));
                parameters.add(extractedMethodParam);
            } else {
                ExtractedMethodParam extractedMethodParam = new ExtractedMethodParam(methodParameter.getParameter().getParameterizedType().getTypeName(),
                        methodParameter.getParameter().getName(),
                        true,
                        null);
                parameters.add(extractedMethodParam);
            }
        }
        return parameters;
    }

    private void injectMapping(RequestMappingInfo requestMappingInfo, ExtractedMethod method) {
        for (Iterator iterator = requestMappingInfo.getPatternsCondition().getPatterns().iterator(); iterator.hasNext(); ) {
            String delta = iterator.next().toString();
            if (delta != null) {
                if (!method.mappings.contains(delta)) {
                    method.mappings.add(delta);
                }
            }
        }
    }

    private void injectHttpVerb(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod, ExtractedMethod method) {
        for (org.springframework.web.bind.annotation.RequestMethod requestMethod : requestMappingInfo.getMethodsCondition().getMethods()) {
            String delta = requestMethod.toString();
            if (delta != null) {
                if (!method.verbs.containsKey(delta)) {
                    //method.httpVerbs.add(delta);

                    ExtractedHttpVerb verb = new ExtractedHttpVerb();
                    verb.parameters.addAll(extractMethodParameters(handlerMethod));
                    method.verbs.put(delta, verb);
                }
            }
        }
    }

    private void injectProduce(RequestMappingInfo requestMappingInfo, ExtractedMethod method) {
        for (Iterator iterator = requestMappingInfo.getProducesCondition().getExpressions().iterator(); iterator.hasNext(); ) {
            String delta = iterator.next().toString();
            if (delta != null) {
                if (!method.produces.contains(delta)) {
                    method.produces.add(delta);
                }
            }
        }
    }
}
