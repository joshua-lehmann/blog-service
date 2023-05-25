package hftm.joshua.controller;

import hftm.joshua.data.Blog;
import hftm.joshua.dto.AuthorRequest;
import hftm.joshua.dto.BlogRequest;
import hftm.joshua.service.BlogService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
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
    @RequestBody(description = "The Author Object to create", content = @Content(schema = @Schema(implementation = AuthorRequest.class),
            examples = {
                    @ExampleObject(name = "Blog with no Author", value = "{\"title\": \"My new Article\", \"content\": \"Some really great content\"}"),
                    @ExampleObject(name = "Blog with Author", value = "{\"title\": \"The Authors Article\", \"content\": \"Some really great content from the Author\", \"authorId\": \"1\"}")
            }
    ))
    @Consumes(MediaType.APPLICATION_JSON)
    public void createBlog(BlogRequest blogRequest) {
        blogService.addBlog(blogRequest);
    }


}
