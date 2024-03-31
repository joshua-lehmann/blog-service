package hftm.joshua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import hftm.joshua.data.Author;
import hftm.joshua.data.Blog;
import hftm.joshua.dto.BlogRequest;
import hftm.joshua.dto.TextMessage;
import hftm.joshua.mapper.BlogMapper;
import hftm.joshua.repository.AuthorRepository;
import hftm.joshua.repository.BlogRepository;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class BlogService {

    @Inject
    BlogRepository blogRepository;
    @Inject
    AuthorRepository authorRepository;
    @Inject
    BlogMapper blogMapper;

    @Inject
    AiService aiService;

    @Inject
    @Channel("text-validation")
    Emitter<TextMessage> validationEmitter;


    public List<Blog> getAllBlogs() {
        return blogRepository.listAll();
    }


    public Long addBlog(BlogRequest blogRequest) {
        Blog blog = blogMapper.fromResource(blogRequest);

        if (blogRequest.getAuthorId() != null) {
            Author author = authorRepository.findById(blogRequest.getAuthorId());
            blog.setAuthor(author);
        }

        var savedBlog = persistBlog(blog);
        Log.info("Sending validation for blog with id:" + savedBlog.getId() + " and text:" + savedBlog.getContent());
        validationEmitter.send(new TextMessage(savedBlog.getId(), savedBlog.getContent()));

        return savedBlog.getId();
    }

    @Transactional
    public Blog persistBlog(Blog blog) {
        blogRepository.persist(blog);
        return blog;
    }

    public Blog getBlog(Long id) {
        return blogRepository.findById(id);
    }

    @Incoming("text-validation-response")
    @Blocking
    @Transactional
    public CompletionStage<Void> setBlogValidation(Message<TextMessage> message) {
        var payload = message.getPayload();

        Log.debug("ID is:" + payload.getSourceId());
        Log.info("Text is:" + payload.getText());
        Blog blog = blogRepository.findById(payload.getSourceId());
        blog.setContent(payload.getText());
        blog.setValidated(payload.getIsValid());
        persistBlog(blog);
        return null;
    }


    public Blog applyPatchToBlog(JsonPatch patch, Blog targetBlog) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetBlog, JsonNode.class));
        return objectMapper.treeToValue(patched, Blog.class);
    }

    @Transactional
    public boolean deleteBlog(Long id) {
        return blogRepository.deleteById(id);
    }

    public Blog translateBlog(Long id, String language) {
        Blog blog = blogRepository.findById(id);
        String translatedContent = aiService.translateBlogContent(blog.getContent(), language);
        blog.setContent(translatedContent);
        return blog;
    }

}
