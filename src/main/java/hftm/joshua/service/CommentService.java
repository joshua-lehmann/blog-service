package hftm.joshua.service;

import hftm.joshua.data.Author;
import hftm.joshua.data.Blog;
import hftm.joshua.data.Comment;
import hftm.joshua.data.Sentiment;
import hftm.joshua.dto.CommentRequest;
import hftm.joshua.repository.AuthorRepository;
import hftm.joshua.repository.BlogRepository;
import hftm.joshua.repository.CommentRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CommentService {

    @Inject
    BlogRepository blogRepository;
    @Inject
    AuthorRepository authorRepository;
    @Inject
    CommentRepository commentRepository;

    @Inject
    AiService aiService;

    @Transactional
    public Long addComment(CommentRequest commentRequest) {
        Author author = authorRepository.findById(commentRequest.authorId());
        Blog blog = blogRepository.findById(commentRequest.blogId());

        Sentiment sentiment = aiService.getCommentSentiment(commentRequest.content());

        Log.info("Sentiment for message: %s is: %s with score: %d".formatted
                (commentRequest.content(), sentiment.getSentimentType(), sentiment.getScore()));

        Comment comment = new Comment(commentRequest.content(), author, blog, sentiment);
        commentRepository.persist(comment);
        return comment.getId();
    }

    public List<Comment> getAllComments() {
        return commentRepository.listAll();
    }
}
