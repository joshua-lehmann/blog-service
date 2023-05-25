package hftm.joshua.service;

import hftm.joshua.data.Author;
import hftm.joshua.data.Blog;
import hftm.joshua.dto.BlogRequest;
import hftm.joshua.mapper.BlogMapper;
import hftm.joshua.repository.AuthorRepository;
import hftm.joshua.repository.BlogRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class BlogService {

    @Inject
    BlogRepository blogRepository;
    @Inject
    AuthorRepository authorRepository;
    @Inject
    BlogMapper blogMapper;

    public List<Blog> getAllBlogs() {
        return blogRepository.listAll();
    }

    @Transactional
    public void addBlog(BlogRequest blogRequest) {
        Blog blog = blogMapper.fromResource(blogRequest);

        if (blogRequest.getAuthorId() != null) {
            Author author = authorRepository.findById(blogRequest.getAuthorId());
            blog.setAuthor(author);
        }
        blogRepository.persist(blog);
    }

    public Blog getBlog(Long id) {
        return blogRepository.findById(id);
    }

}
