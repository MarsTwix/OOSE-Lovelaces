package nl.han.project.skilltree.domain.user.presentation;

import nl.han.project.skilltree.domain.global.AuthenticationHelper;
import nl.han.project.skilltree.domain.impl.mapper.SkillCouplingMapperImpl;
import nl.han.project.skilltree.domain.impl.mapper.UserMapperImpl;
import nl.han.project.skilltree.domain.providers.namebindings.Secured;
import nl.han.project.skilltree.domain.skill.data.SkillCoupling;
import nl.han.project.skilltree.domain.skill.data.UserSkill;
import nl.han.project.skilltree.domain.skill.presentation.dto.UserSkillResponse;
import nl.han.project.skilltree.domain.skill.service.SkillService;
import nl.han.project.skilltree.domain.skilltree.data.UserSkillTree;
import nl.han.project.skilltree.domain.skilltree.presentation.dto.UserSkillTreeResponse;
import nl.han.project.skilltree.domain.skilltree.service.SkillTreeService;
import nl.han.project.skilltree.domain.user.data.Feedback;
import nl.han.project.skilltree.domain.user.data.User;
import nl.han.project.skilltree.domain.user.presentation.dto.FeedbackRequest;
import nl.han.project.skilltree.domain.user.presentation.dto.UserRequest;
import nl.han.project.skilltree.domain.user.presentation.dto.UserResponse;
import nl.han.project.skilltree.domain.user.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/users")
public class UserController {

    @Inject
    private UserService userService;

    @Inject
    private UserMapperImpl mapper;

    @Inject
    private SkillCouplingMapperImpl skillCouplingMapper;

    @Inject
    private SkillTreeService skillTreeService;

    @Inject
    private SkillService skillService;

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserRequest req) {
        User user = mapper.mapRequestToEntity(req);
        user = userService.getUserByEmail(user);
        UserResponse res = mapper.mapEntityToResponse(user);
        return Response.ok(res).build();
    }

    @Path("/logout")
    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization) {
        String token = AuthenticationHelper.getBearerFromHeader(authorization);

        userService.logout(token);
        return Response.ok().build();
    }

    @Path("{studentId}/skilltree/{id}")
    @GET
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkillTree(@PathParam("id") int id, @PathParam("studentId") int studentId) {
        UserSkillTreeResponse res = mapEntityToResponse(skillTreeService.getSkillTreeForUser(id, studentId));
        return Response.status(Response.Status.OK).entity(res).build();
    }

    @Path("")
    @GET
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<UserResponse> res = new ArrayList<>();
        for(User user : userService.getAll()) {
            res.add(mapper.mapEntityToResponse(user));
        }
        return Response.status(Response.Status.OK).entity(res).build();
    }

    @Path("/feedback")
    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response giveFeedback(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, FeedbackRequest req) {
        String token = AuthenticationHelper.getBearerFromHeader(authorization);

        User fromUser = userService.getUserByToken(token);
        Feedback feedback = mapFeedbackToEntity(req);

        userService.insertFeedback(fromUser.getId(), feedback);
        return Response.ok().build();
    }

    private UserSkillTreeResponse mapEntityToResponse(UserSkillTree skillTree) {
        UserSkillTreeResponse response = new UserSkillTreeResponse();
        response.setId(skillTree.getId());
        response.setName(skillTree.getName());
        response.setStudentId(skillTree.getStudentId());
        response.setUserId(skillTree.getUserId());
        response.setUserSkills(new ArrayList<>());
        response.setEdges(new ArrayList<>());
        if(skillTree.getUserSkills() != null) {
            for(UserSkill userSkill : skillTree.getUserSkills()) {
                response.getUserSkills().add(mapEntityToResponse(userSkill));
            }
        }
        if(skillTree.getEdges() != null) {
            for (SkillCoupling skillCoupling : skillTree.getEdges()) {
                response.getEdges().add(skillCouplingMapper.mapEntityToResponse(skillCoupling));
            }
        }
        return response;
    }

    private UserSkillResponse mapEntityToResponse(UserSkill skill) {
        UserSkillResponse response = new UserSkillResponse();
        response.setId(skill.getId());
        response.setName(skill.getName());
        response.setDescription(skill.getDescription());
        response.setAssessmentCriteria(skill.getAssessmentCriteria());
        response.setEvidenceRequired(skill.isEvidenceRequired());
        response.setGrade(skill.getGrade());
        response.setSkillTreeId(skill.getSkillTreeId());
        response.setX(skill.getX());
        response.setY(skill.getY());
        return response;
    }

    private Feedback mapFeedbackToEntity(FeedbackRequest feedback) {
        Feedback fb = new Feedback();
        fb.setSkillId(feedback.getSkillId());
        fb.setForUserId(feedback.getForUserId());
        fb.setDescription(feedback.getDescription());
        return fb;
    }
}
