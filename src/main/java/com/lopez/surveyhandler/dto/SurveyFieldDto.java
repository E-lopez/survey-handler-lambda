package com.lopez.surveyhandler.dto;

import java.util.List;
import java.util.Map;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@RegisterForReflection
@DynamoDbBean
public class SurveyFieldDto {
    public enum FieldType {
        TEXT, CHECKBOX, ID_NUMBER, EMAIL, DATE, DROPDOWN, LIKERT, MULTIPLE_CHOICE, TEXTAREA, FILE
    }

    private FieldType type;
    private boolean required;
    private String label;
    private List<String> options;
    private String helperLeft;
    private String helperRight;
    private List<String> multipleOptions;
    private boolean multiple;

    public SurveyFieldDto() {
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getHelperLeft() {
        return helperLeft;
    }

    public void setHelperLeft(String helperLeft) {
        this.helperLeft = helperLeft;
    }

    public String getHelperRight() {
        return helperRight;
    }

    public void setHelperRight(String helperRight) {
        this.helperRight = helperRight;
    }

    public List<String> getMultipleOptions() {
        return multipleOptions;
    }

    public void setMultipleOptions(List<String> multipleOptions) {
        this.multipleOptions = multipleOptions;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    @Override
    public String toString() {
        return String.format("SurveyFieldDto{type=%s, required=%s, label='%s', options=%s, helperLeft='%s', helperRight='%s', multipleOptions=%s, multiple=%s}",
                type, required, label, options, helperLeft, helperRight, multipleOptions, multiple);
    }
}