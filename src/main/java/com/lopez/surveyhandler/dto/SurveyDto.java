package com.lopez.surveyhandler.dto;

import java.util.Map;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

@RegisterForReflection
@DynamoDbBean
public class SurveyDto {
    public enum SurveyType {
        SCORING
    }

    private String id;
    private String name;
    private SurveyType type;
    private String version;
    private String typeVersion;
    private Map<String, SurveySectionDto> sections;

    public SurveyDto() {
    }

    public SurveyDto(SurveyRequestDto surveyRequestDto) {
        this.name = surveyRequestDto.getName();
        this.type = surveyRequestDto.getType();
        this.version = surveyRequestDto.getVersion();
        this.typeVersion = surveyRequestDto.getType() + "_" + surveyRequestDto.getVersion();
        this.sections = surveyRequestDto.getSections();
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SurveyType getType() {
        return type;
    }

    public void setType(SurveyType type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "TypeVersionIndex")
    public String getTypeVersion() {
        return typeVersion;
    }

    public void setTypeVersion(String typeVersion) {
        this.typeVersion = typeVersion;
    }

    public Map<String, SurveySectionDto> getSections() {
        return sections;
    }

    public void setSections(Map<String, SurveySectionDto> sections) {
        this.sections = sections;
    }
}