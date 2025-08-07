package com.lopez.surveyhandler.dto;

import java.util.Map;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@RegisterForReflection
@DynamoDbBean
public class SurveySectionDto {
    private Metadata metadata;
    private Map<String, SurveyFieldDto> data;

    public SurveySectionDto() {}

    public SurveySectionDto(Metadata metadata, Map<String, SurveyFieldDto> data) {
        this.metadata = metadata;
        this.data = data;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Map<String, SurveyFieldDto> getData() {
        return data;
    }

    public void setData(Map<String, SurveyFieldDto> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("SurveySectionDto{metadata=%s, data=%s}", metadata, data);
    }
}