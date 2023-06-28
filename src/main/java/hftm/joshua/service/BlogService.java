package hftm.joshua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
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


    public Blog applyPatchToBlog(JsonPatch patch, Blog targetBlog) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetBlog, JsonNode.class));
        return objectMapper.treeToValue(patched, Blog.class);
    }

}
