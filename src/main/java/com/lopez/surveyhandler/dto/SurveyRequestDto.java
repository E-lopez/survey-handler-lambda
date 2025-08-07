package com.lopez.surveyhandler.dto;

import java.util.Map;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class SurveyRequestDto {
    private String name;
    private SurveyDto.SurveyType type;
    private String version;
    private Map<String, SurveySectionDto> sections;

    public SurveyRequestDto() {}

    public SurveyRequestDto(String name, SurveyDto.SurveyType type, String version, Map<String, SurveySectionDto> sections) {
        this.name = name;
        this.type = type;
        this.version = version;
        this.sections = sections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SurveyDto.SurveyType getType() {
        return type;
    }

    public void setType(SurveyDto.SurveyType type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, SurveySectionDto> getSections() {
        return sections;
    }

    public void setSections(Map<String, SurveySectionDto> sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return String.format("SurveyRequestDto{name='%s', type=%s, version='%s', sections=%s}",
                name, type, version, sections);
    }
}