package hftm.joshua.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private Long authorId;
}
