package nl.han.project.skilltree.domain.assessment.presentation;

import nl.han.project.skilltree.domain.assessment.data.Assessment;
import nl.han.project.skilltree.domain.assessment.presentation.dto.AssessmentRequest;
import nl.han.project.skilltree.domain.assessment.service.AssessmentService;
import nl.han.project.skilltree.domain.impl.mapper.AssessmentMapperImpl;
import nl.han.project.skilltree.domain.providers.namebindings.Secured;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/assessments")
@Secured
public class AssessmentController {

    @Inject
    AssessmentService service;

    @Inject
    AssessmentMapperImpl mapper;

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAssessment(AssessmentRequest request) {
        Assessment assessment = mapper.mapRequestToEntity(request);
        assessment = service.createAssessment(assessment);
        return Response.status(Response.Status.CREATED).entity(mapper.mapEntityToResponse(assessment)).build();
    }
}
