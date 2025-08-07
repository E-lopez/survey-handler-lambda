package com.lopez.surveyhandler.dto;

import java.util.Map;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

@RegisterForReflection
@DynamoDbBean
public class AgreementDto {
    private String id;
    private String version;
    private Map<String, String> summary;
    private Map<String, String> agreement;
    private Map<String, String> termsConditions;

    public AgreementDto() {
        /* Not needed */
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "VersionIndex")
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