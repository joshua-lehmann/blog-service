package hftm.joshua.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Blog extends BaseEntity {

    private String title;
    @Lob
    @Column(length = 10000)
    private String content;
    private Integer likes;
    private boolean validated = false;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;

    @OneToMany(mappedBy = "blog")
    private List<Comment> comments;

    public Blog(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
