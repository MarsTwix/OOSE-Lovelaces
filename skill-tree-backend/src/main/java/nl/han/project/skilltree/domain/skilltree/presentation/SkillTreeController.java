package nl.han.project.skilltree.domain.skilltree.presentation;


import nl.han.project.skilltree.domain.global.AuthenticationHelper;
import nl.han.project.skilltree.domain.impl.mapper.SkillTreeMapperImpl;
import nl.han.project.skilltree.domain.providers.namebindings.Secured;
import nl.han.project.skilltree.domain.skilltree.data.SkillTree;
import nl.han.project.skilltree.domain.skilltree.presentation.dto.SkillTreeRequest;
import nl.han.project.skilltree.domain.skilltree.presentation.dto.SkillTreeResponse;
import nl.han.project.skilltree.domain.skilltree.service.SkillTreeService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/skilltrees")
@Secured
public class SkillTreeController {

    @Inject
    private SkillTreeService service;

    @Inject
    private SkillTreeMapperImpl mapper;


    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkillTrees() {
        List<SkillTreeResponse> res = new ArrayList<>();
        for (SkillTree skillTree : service.getSkillTrees(false)) {
            res.add(mapper.mapEntityToResponse(skillTree, null, null));
        }
        return Response.status(Response.Status.OK).entity(res).build();
    }

    @GET
    @Path("/archived")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArchivedSkillTrees() {
        List<SkillTreeResponse> res = new ArrayList<>();
        for (SkillTree skillTree : service.getSkillTrees(true)) {
            res.add(mapper.mapEntityToResponse(skillTree, null, null));
        }
        return Response.status(Response.Status.OK).entity(res).build();
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkillTree(@PathParam("id") int id) {

        SkillTree skillTree = service.getSkillTree(id);
        SkillTreeResponse res = mapper.mapEntityToResponse(skillTree);

        return Response.status(Response.Status.OK).entity(res).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSkillTree(@PathParam("id") int id, SkillTreeRequest req) {
        SkillTree skillTree = mapper.mapRequestToEntity(req, id);
        service.updateSkillTree(skillTree);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSkillTree(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, SkillTreeRequest req) {
        String token = AuthenticationHelper.getBearerFromHeader(authorization);
        SkillTree skillTree = mapper.mapRequestToEntity(req);
        skillTree = service.createSkillTree(skillTree, token);
        SkillTreeResponse res = mapper.mapEntityToResponse(skillTree, null, null);
        return Response.status(Response.Status.OK).entity(res).build();
    }

    @PATCH
    @Path("/{id}/archive")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response archiveSkillTree(@PathParam("id") int id) {
        service.archiveSkillTree(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSkillTree(@PathParam("id") int id) {
        service.deleteSkillTree(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @Path("/{id}/restore")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response restoreSkillTree(@PathParam("id") int id) {
        service.restoreSkillTree(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
