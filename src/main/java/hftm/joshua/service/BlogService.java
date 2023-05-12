package hftm.joshua.service;

import hftm.joshua.data.Blog;
import hftm.joshua.repository.BlogRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class BlogService {

    @Inject
    BlogRepository blogRepository;
    @Inject
    Logger logger;

    public List<Blog> getAllBlogs() {
        logger.info("Getting all blogs");
        return blogRepository.getAllBlogs();
    }

}
