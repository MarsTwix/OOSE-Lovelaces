package nl.han.project.skilltree.domain.skill.presentation;

import nl.han.project.skilltree.domain.impl.mapper.SkillCouplingMapperImpl;
import nl.han.project.skilltree.domain.impl.mapper.SkillMapperImpl;
import nl.han.project.skilltree.domain.providers.namebindings.Secured;
import nl.han.project.skilltree.domain.skill.data.Skill;
import nl.han.project.skilltree.domain.skill.data.SkillCoupling;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillCouplingRequest;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillCouplingResponse;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillRequest;
import nl.han.project.skilltree.domain.skill.presentation.dto.SkillResponse;
import nl.han.project.skilltree.domain.skill.service.SkillService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/skilltrees/{id}/skills")
@Secured
public class SkillController {

    @Inject
    private SkillService service;

    @Inject
    private SkillMapperImpl skillMapper;

    @Inject
    private SkillCouplingMapperImpl skillCouplingMapper;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSkill(@PathParam("id") int id, SkillRequest req) {

        req.setSkillTreeId(id);
        Skill skill = skillMapper.mapRequestToEntity(req);
        skill = service.createSkill(skill);
        SkillResponse res = skillMapper.mapEntityToResponse(skill);

        return Response.status(Response.Status.CREATED).entity(res).build();
    }

    @PUT
    @Path("/{skillId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSkill(@PathParam("skillId") int id, SkillRequest req) {
        Skill skill = skillMapper.mapRequestToEntity(req, id);
        service.updateSkill(skill);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @Path("/{skillId}/reposition")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSkillPosition(@PathParam("skillId") int id, SkillRequest req) {
        Skill skill = skillMapper.mapRequestToEntity(req, id);
        service.updateSkillPosition(skill);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @Path("/{skillId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response archiveSkill(@PathParam("skillId") int id) {
        service.archiveSkill(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/archived")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArchivedSkills(@PathParam("id") int id) {
        List<Skill> skills = service.getSkillsForSkillTree(id, true);
        List<SkillResponse> res = new ArrayList<>();
        for (Skill skill : skills) {
            res.add(skillMapper.mapEntityToResponse(skill));
        }
        return Response.ok().entity(res).build();
    }

    @PATCH
    @Path("/{skillId}/restore")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response restoreSkill(@PathParam("skillId") int id) {
        service.restoreSkill(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{skillId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSkill(@PathParam("skillId") int id) {
        service.deleteSkill(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/attach")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSkillCoupling(@PathParam("id") int id, SkillCouplingRequest req) {
        req.setSkillTreeId(id);
        SkillCoupling skillCoupling = skillCouplingMapper.mapRequestToEntity(req);
        skillCoupling = service.createSkillCoupling(skillCoupling);
        SkillCouplingResponse res = skillCouplingMapper.mapEntityToResponse(skillCoupling);
        return Response.ok().entity(res).build();
    }

    @DELETE
    @Path("/{skillCouplingId}/detach")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSkillCoupling(@PathParam("id") int id, @PathParam("skillCouplingId") int skillCouplingId) {
        service.deleteSkillCoupling(id, skillCouplingId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
