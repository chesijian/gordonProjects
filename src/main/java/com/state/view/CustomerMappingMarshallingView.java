/*
 * @(#) CustomerMappingMarshallingView.java    2013-4-2
 * Project  :xdf
 * Copyright: 2013 xdf Inc. All rights reserved.
 */
package com.state.view;

import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.oxm.Marshaller;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.xml.MarshallingView;

/**
 * 
 * @author Administrator
 * @version 1.0 Revise History:
 * 
 */
public class CustomerMappingMarshallingView extends MarshallingView {

    private Marshaller marshaller;

    private String modelKey;

    public Marshaller getMarshaller() {
        return marshaller;
    }

    public String getModelKey() {
        return modelKey;
    }

    public CustomerMappingMarshallingView() {
        super();
    }

    public CustomerMappingMarshallingView(Marshaller marshaller) {
        super(marshaller);
        this.marshaller = marshaller;
    }

    @Override
    public void setMarshaller(Marshaller marshaller) {
        super.setMarshaller(marshaller);
        this.marshaller = marshaller;
    }

    @Override
    public void setModelKey(String modelKey) {
        super.setModelKey(modelKey);
        this.modelKey = modelKey;
    }

    @Override
    protected Object locateToBeMarshalled(Map model) throws ServletException {
        if (this.getModelKey() != null) {
            Object o = model.get(this.getModelKey());
            if (o == null) {
                throw new ServletException("Model contains no object with key [" + modelKey + "]");
            }
            if (!this.getMarshaller().supports(o.getClass())) {
                throw new ServletException("Model object [" + o + "] retrieved via key [" + modelKey + "] is not supported by the Marshaller");
            }
            return o;
        }

        for (Object o : model.values()) {
            if (o != null && !(o instanceof BindingResult) && this.getMarshaller().supports(o.getClass())) {
                return o;
            }
        }
        return null;
    }

}