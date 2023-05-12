package hftm.joshua.repository;

import hftm.joshua.data.Blog;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BlogRepository {

    private final List<Blog> blogs = new ArrayList<>();

    public BlogRepository() {
        // Simulate some data since we don't have a database yet
        blogs.add(new Blog("First Blog", "This is my first blog"));
        blogs.add(new Blog("Second Blog", "This is my second blog"));
    }

    public List<Blog> getAllBlogs() {
        return blogs;
    }
}
