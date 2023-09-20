package hftm.joshua.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import hftm.joshua.data.Blog;
import hftm.joshua.dto.BlogRequest;
import hftm.joshua.service.BlogService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Path("/blog")
@Produces(MediaType.APPLICATION_JSON)
public class BlogResource {

    @Inject
    BlogService blogService;


    @GET
    public List<Blog> getBlogs() {
        return blogService.getAllBlogs();
    }

    @GET
    @Path("/{id}")
    public Blog getBlog(@PathParam("id") Long id) {
        return blogService.getBlog(id);
    }

    @POST
    @Authenticated
    @RequestBody(description = "The Blog Object to create", content = @Content(schema = @Schema(implementation = BlogRequest.class),
            examples = {
                    @ExampleObject(name = "Blog with no Author", value = """
                            {"title": "My new Article", "content": "Some really great content"}
                            """),
                    @ExampleObject(name = "Blog with Author", value = """
                            {"title": "The Authors Article", "content": "Some really great content from the Author", "authorId": "1"}
                            """)
            }
    ))
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBlog(BlogRequest blogRequest) {
        return Response.ok().status(Response.Status.CREATED).entity(blogService.addBlog(blogRequest)).build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes("application/json-patch+json")
    @RequestBody(description = "Update Blog with with Patch, fields which are provided are updated, non provided are left as is", content = @Content(schema = @Schema(implementation = BlogRequest.class),
            examples = {
                    @ExampleObject(name = "Update Like Count", value = """
                            [
                                {
                                    "op": "replace",
                                    "path": "/likes",
                                    "value": 5
                                }
                            ]"""),
            }
    ))
    @Transactional
    public Response patchBlog(JsonPatch blogPatch, @PathParam("id") Long id) {
        try {
            Blog blog = blogService.getBlog(id);
            Blog patchedBlog = blogService.applyPatchToBlog(blogPatch, blog);
            BeanUtils.copyProperties(blog, patchedBlog);
            return Response.ok(blog).build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBlog(@PathParam("id") Long id) {
        return blogService.deleteBlog(id) ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

}
