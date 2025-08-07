package com.lopez.surveyhandler;

import org.jboss.logging.Logger;

import com.lopez.surveyhandler.dto.AgreementDto;
import com.lopez.surveyhandler.dto.AgreementRequestDto;
import com.lopez.surveyhandler.dto.ApiResponse;
import com.lopez.surveyhandler.service.AgreementService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/survey/agreement")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AgreementResource {

    private static final Logger logger = Logger.getLogger(AgreementResource.class);

    @Inject
    AgreementService agreementService;

    @GET
    public Response fetchAgreements() {
        try {
            ApiResponse<?> result = agreementService.fetchAllAgreements();
            if (result.isSuccess()) {
                return Response.ok(result).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(result).build();
            }
        } catch (Exception e) {
            logger.error("Error fetching agreements", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error"))
                    .build();
        }
    }

    @GET
    @Path("/{version}")
    public Response fetchAgreementByTypeVersion(@PathParam("version") String version) {
        try {
            ApiResponse<?> result = agreementService.fetchAgreementByVersion(version);
            if (result.isSuccess()) {
                return Response.ok(result).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(result).build();
            }
        } catch (Exception e) {
            logger.error("Error fetching agreement by version", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error"))
                    .build();
        }
    }

    @POST
    public Response registerAgreement(AgreementRequestDto model) {
        try {
            AgreementDto agreementDto = new AgreementDto();
            agreementDto.setVersion(model.getVersion());
            agreementDto.setSummary(model.getSummary());
            agreementDto.setAgreement(model.getAgreement());
            agreementDto.setTermsConditions(model.getTermsConditions());
            
            ApiResponse<?> result = agreementService.registerAgreement(agreementDto);
            if (result.isSuccess()) {
                return Response.status(Response.Status.CREATED).entity(result).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result).build();
            }
        } catch (Exception e) {
            logger.error("Error creating agreement", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Failed to create agreement"))
                    .build();
        }
    }

    @DELETE
    public Response clearCollection() {
        try {
            ApiResponse<?> result = agreementService.clearAll();
            return Response.ok(result).build();
        } catch (Exception e) {
            logger.error("Error clearing agreements", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Failed to clear collection"))
                    .build();
        }
    }
}