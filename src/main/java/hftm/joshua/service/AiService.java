package hftm.joshua.service;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import hftm.joshua.data.Sentiment;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService()
public interface AiService {

    @SystemMessage("""
            You are working for a blogging website, processing and analyzing posts and comments from users.
            """)
    @UserMessage("""
            Your Task is to process a blog comment and determine the sentiment of the comment. The comment is always delimited with ---
            Apply a sentiment analysis to the comment to determine if it is positive or negative or neutral, considering various languages.
            Also assign a score between 1-10 to the sentiment, which corresponds to the strength of the sentiment that you think the comment has.
            For example:
                - "I like the post, it was interesting!" is a 'POSITIVE' review
                - "J'adore votre blog" is a 'POSITIVE' review
                - "Not sure what to think" is a 'NEUTRAL' review
                - "Okay I did not know about that" is a 'NEUTRAL' review
                - "Da bin ich überhaupt nicht gleicher Meinung!" is a 'NEGATIVE' review
                - "This is boring and I disagree!" is a 'NEGATIVE' review
                
                Respond with a JSON document containing:
                    - the 'sentimentType' key set to 'POSITIVE' if the comment is positive, is set to 'NEGATIVE' if comment is negative, and to 'NEUTRAL' if the comment is neutral.
                    - the 'score' key set to a value between 1-10 indicating the strength of the sentiment.
                    
                    ---
                    {comment}
                    ---
            """)
    Sentiment getCommentSentiment(String comment);


    @UserMessage("""
            Your Task is to translate the content of a blog post to a specific language.
            Please translate the content to the specified language.
            Translate the content to language: {language}
            The content to translate:
            {content}
            """)
    String translateBlogContent(String content, String language);


}
