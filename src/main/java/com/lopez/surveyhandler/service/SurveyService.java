package com.lopez.surveyhandler.service;

import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;

import com.lopez.surveyhandler.dto.ApiResponse;
import com.lopez.surveyhandler.dto.SurveyDto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@ApplicationScoped
public class SurveyService {
    private static final String SURVEY_TABLE_NAME = "Surveys-prod";
    private static final String TYPE_VERSION_INDEX = "TypeVersionIndex";

    private static final Logger logger = Logger.getLogger(SurveyService.class);
    private final DynamoDbTable<SurveyDto> surveyTable;

    @Inject
    public SurveyService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        this.surveyTable = dynamoEnhancedClient.table(SURVEY_TABLE_NAME, TableSchema.fromClass(SurveyDto.class));
    }

    public ApiResponse<List<SurveyDto>> fetchSurveys() {
        try {
            List<SurveyDto> surveys = surveyTable.scan().items().stream().toList();
            if (surveys.isEmpty()) {
                return ApiResponse.error("No surveys found!");
            }
            return ApiResponse.success(surveys);
        } catch (DynamoDbException e) {
            logger.error("DynamoDB error fetching surveys", e);
            return ApiResponse.error("Database error retrieving surveys");
        } catch (Exception e) {
            logger.error("Unexpected error fetching surveys", e);
            return ApiResponse.error("Failed to retrieve surveys");
        }
    }

    public ApiResponse<SurveyDto> fetchModelByTypeVersion(String typeVersion) {
        try {
            DynamoDbIndex<SurveyDto> typeVersionIndex = surveyTable.index(TYPE_VERSION_INDEX);
            QueryConditional condition = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(typeVersion)
                    .build());
            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(condition)
                    .limit(1)
                    .build();
            
            List<SurveyDto> results = typeVersionIndex.query(request).stream()
                    .flatMap(page -> page.items().stream())
                    .toList();
            
            if (results.isEmpty()) {
                return ApiResponse.error("No survey found with those parameters!");
            }
            return ApiResponse.success(results.get(0));
        } catch (DynamoDbException e) {
            logger.error("DynamoDB error fetching survey by type version", e);
            return ApiResponse.error("Database error retrieving survey");
        } catch (Exception e) {
            logger.error("Unexpected error fetching survey by type version", e);
            return ApiResponse.error("Failed to retrieve survey");
        }
    }

    public ApiResponse<SurveyDto> registerModel(SurveyDto model) {
        try {
            model.setId(UUID.randomUUID().toString());
            surveyTable.putItem(model);
            return ApiResponse.success(model);
        } catch (DynamoDbException e) {
            logger.error("DynamoDB error registering survey", e);
            return ApiResponse.error("Database error creating survey");
        } catch (Exception e) {
            logger.error("Unexpected error registering survey", e);
            return ApiResponse.error("Failed to create survey");
        }
    }

    public ApiResponse<String> clearAll() {
        try {
            List<SurveyDto> surveys = surveyTable.scan().items().stream().toList();
            for (SurveyDto survey : surveys) {
                surveyTable.deleteItem(Key.builder().partitionValue(survey.getId()).build());
            }
            return ApiResponse.success("Collection cleared");
        } catch (DynamoDbException e) {
            logger.error("DynamoDB error clearing surveys", e);
            return ApiResponse.error("Database error clearing collection");
        } catch (Exception e) {
            logger.error("Unexpected error clearing surveys", e);
            return ApiResponse.error("Failed to clear collection");
        }
    }
}