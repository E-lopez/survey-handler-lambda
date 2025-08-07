package com.lopez.surveyhandler.service;

import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;

import com.lopez.surveyhandler.dto.AgreementDto;
import com.lopez.surveyhandler.dto.ApiResponse;

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
public class AgreementService {
    private static final String AGREEMENT_TABLE_NAME = "Agreements-dev";
    private static final String VERSION_INDEX = "VersionIndex";

    private static final Logger logger = Logger.getLogger(AgreementService.class);
    private final DynamoDbTable<AgreementDto> agreementTable;

    @Inject
    public AgreementService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        this.agreementTable = dynamoEnhancedClient.table(AGREEMENT_TABLE_NAME, TableSchema.fromClass(AgreementDto.class));
    }

    public ApiResponse<List<AgreementDto>> fetchAllAgreements() {
        try {
            List<AgreementDto> agreements = agreementTable.scan().items().stream().toList();
            if (agreements.isEmpty()) {
                return ApiResponse.error("No agreements found!");
            }
            return ApiResponse.success(agreements);
        } catch (DynamoDbException e) {
            logger.error("DynamoDB error fetching agreements", e);
            return ApiResponse.error("Database error retrieving agreements");
        } catch (Exception e) {
            logger.error("Unexpected error fetching agreements", e);
            return ApiResponse.error("Failed to retrieve agreements");
        }
    }

    public ApiResponse<AgreementDto> fetchAgreementByVersion(String version) {
        try {
            DynamoDbIndex<AgreementDto> versionIndex = agreementTable.index(VERSION_INDEX);
            QueryConditional condition = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(version)
                    .build());
            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(condition)
                    .limit(1)
                    .build();
            
            List<AgreementDto> results = versionIndex.query(request).stream()
                    .flatMap(page -> page.items().stream())
                    .toList();
            
            if (results.isEmpty()) {
                return ApiResponse.error("No agreement found with that version!");
            }
            return ApiResponse.success(results.get(0));
        } catch (DynamoDbException e) {
            logger.error("DynamoDB error fetching agreement by version", e);
            return ApiResponse.error("Database error retrieving agreement");
        } catch (Exception e) {
            logger.error("Unexpected error fetching agreement by version", e);
            return ApiResponse.error("Failed to retrieve agreement");
        }
    }

    public ApiResponse<AgreementDto> registerAgreement(AgreementDto agreementDto) {
        try {
            agreementDto.setId(UUID.randomUUID().toString());
            agreementTable.putItem(agreementDto);
            return ApiResponse.success(agreementDto);
        } catch (DynamoDbException e) {
            logger.error("DynamoDB error registering agreement", e);
            return ApiResponse.error("Database error creating agreement");
        } catch (Exception e) {
            logger.error("Unexpected error registering agreement", e);
            return ApiResponse.error("Failed to create agreement");
        }
    }

    public ApiResponse<String> clearAll() {
        try {
            List<AgreementDto> agreements = agreementTable.scan().items().stream().toList();
            for (AgreementDto agreement : agreements) {
                agreementTable.deleteItem(Key.builder().partitionValue(agreement.getId()).build());
            }
            return ApiResponse.success("Collection cleared");
        } catch (DynamoDbException e) {
            logger.error("DynamoDB error clearing agreements", e);
            return ApiResponse.error("Database error clearing collection");
        } catch (Exception e) {
            logger.error("Unexpected error clearing agreements", e);
            return ApiResponse.error("Failed to clear collection");
        }
    }
}