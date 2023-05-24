package hftm.joshua.service;

import hftm.joshua.data.Author;
import hftm.joshua.data.Blog;
import hftm.joshua.dto.BlogRequest;
import hftm.joshua.repository.AuthorRepository;
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
    AuthorRepository authorRepository;
    @Inject
    Logger logger;

    public List<Blog> getAllBlogs() {
        logger.info("Getting all blogs");
        return blogRepository.listAll();
    }

    public void addBlog(BlogRequest blogRequest) {
        Blog blog = new Blog(blogRequest.getTitle(), blogRequest.getContent());

        if (blogRequest.getAuthorId() != null) {
            Author author = authorRepository.findById(blogRequest.getAuthorId());
            authorRepository.persist(author);
            blog.setAuthor(author);
        }
        blogRepository.persist(blog);
    }

    public Blog getBlog(Long id) {
        return blogRepository.findById(id);
    }

}
