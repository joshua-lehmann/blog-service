package hftm.joshua.service;

import hftm.joshua.data.Blog;
import hftm.joshua.repository.BlogRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class BlogService {

    @Inject
    BlogRepository blogRepository;

    public List<Blog> getAllBlogs() {
        return blogRepository.getAllBlogs();
    }

}
