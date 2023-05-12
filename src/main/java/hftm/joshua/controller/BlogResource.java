package hftm.joshua.controller;

import hftm.joshua.data.Blog;
import hftm.joshua.service.BlogService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/blog")
public class BlogResource {

     @Inject
     BlogService blogService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Blog> getAllBlogs() {
        return blogService.getAllBlogs();
    }

}
