package hftm.joshua.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogRequest {
    @NotNull
    @NotBlank
    private String title;
    @NotNull(message = "Content cannot be null")
    private String content;
    private Long authorId;
}
