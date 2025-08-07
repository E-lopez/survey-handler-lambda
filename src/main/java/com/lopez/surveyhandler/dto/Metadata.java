package com.lopez.surveyhandler.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@RegisterForReflection
@DynamoDbBean
public class Metadata {
    private String title;
    private float weight;
    private String instruction;

    public Metadata() {}

    public Metadata(String title, float weight, String instruction) {
        this.title = title;
        this.weight = weight;
        this.instruction = instruction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}