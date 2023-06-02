package hftm.joshua.controller;

import hftm.joshua.data.Blog;
import hftm.joshua.dto.BlogRequest;
import hftm.joshua.service.BlogService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.List;

@Path("/blog")
public class BlogResource {

    @Inject
    BlogService blogService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Blog> getBlogs() {
        return blogService.getAllBlogs();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Blog getBlog(@PathParam("id") Long id) {
        return blogService.getBlog(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void createBlog(@RequestBody(description = "The Blog to create", required = true, content = @Content(schema = @Schema(implementation = BlogRequest.class))) BlogRequest blogRequest) {
        blogService.addBlog(blogRequest);
    }


}
