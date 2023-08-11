package hftm.joshua.controller;

import hftm.joshua.data.Comment;
import hftm.joshua.dto.CommentRequest;
import hftm.joshua.service.CommentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.List;

@Path("/comment")
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource {

    @Inject
    CommentService commentService;


    @GET
    public List<Comment> getComments() {
        return commentService.getAllComments();
    }


    @POST
    @RequestBody(description = "The Comment Object to create", content = @Content(schema = @Schema(implementation = CommentRequest.class), examples = {@ExampleObject(name = "Comment with AuthorId and BlogId", value = """
                    {"blogId": 1, "content": "My very first comment for Blog 1", "authorId": 1}
            """),}))
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createComment(CommentRequest commentRequest) {
        return Response.ok().status(Response.Status.CREATED).entity(commentService.addComment(commentRequest)).build();
    }

}
