package com.lopez.surveyhandler;

import org.jboss.logging.Logger;

import com.lopez.surveyhandler.dto.ApiResponse;
import com.lopez.surveyhandler.dto.SurveyDto;
import com.lopez.surveyhandler.dto.SurveyRequestDto;
import com.lopez.surveyhandler.service.SurveyService;

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

@Path("/api/v1/survey")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SurveyResource {

    private static final Logger logger = Logger.getLogger(SurveyResource.class);

    @Inject
    SurveyService surveyService;

    @GET
    public Response fetchSurveys() {
        try {
            ApiResponse<?> result = surveyService.fetchSurveys();
            if (result.isSuccess()) {
                return Response.ok(result).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(result).build();
            }
        } catch (Exception e) {
            logger.error("Error fetching surveys", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error"))
                    .build();
        }
    }

    @GET
    @Path("/{typeVersion}")
    public Response fetchModelByTypeVersion(@PathParam("typeVersion") String typeVersion) {
        try {
            ApiResponse<?> result = surveyService.fetchModelByTypeVersion(typeVersion);
            if (result.isSuccess()) {
                return Response.ok(result).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(result).build();
            }
        } catch (Exception e) {
            logger.error("Error fetching survey by type version", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error"))
                    .build();
        }
    }

    @POST
    public Response registerModel(SurveyRequestDto model) {
        try {
            if (model == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Request body is required"))
                        .build();
            }

            SurveyDto persistent = new SurveyDto(model);
            ApiResponse<?> result = surveyService.registerModel(persistent);
            
            if (result.isSuccess()) {
                return Response.status(Response.Status.CREATED).entity(result).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result).build();
            }
        } catch (Exception e) {
            logger.error("Error creating survey", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Failed to create survey"))
                    .build();
        }
    }

    @DELETE
    public Response clearCollection() {
        try {
            ApiResponse<?> result = surveyService.clearAll();
            return Response.ok(result).build();
        } catch (Exception e) {
            logger.error("Error clearing surveys", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Failed to clear collection"))
                    .build();
        }
    }
}