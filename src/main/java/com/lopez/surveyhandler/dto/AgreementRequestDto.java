package com.lopez.surveyhandler.dto;

import java.util.Map;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AgreementRequestDto {
    private String version;
    private Map<String, String> summary;
    private Map<String, String> agreement;
    private Map<String, String> termsConditions;

    public AgreementRequestDto() {}

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getSummary() {
        return summary;
    }

    public void setSummary(Map<String, String> summary) {
        this.summary = summary;
    }

    public Map<String, String> getAgreement() {
        return agreement;
    }

    public void setAgreement(Map<String, String> agreement) {
        this.agreement = agreement;
    }

    public Map<String, String> getTermsConditions() {
        return termsConditions;
    }

    public void setTermsConditions(Map<String, String> termsConditions) {
        this.termsConditions = termsConditions;
    }
}