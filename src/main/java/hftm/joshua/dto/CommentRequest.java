package hftm.joshua.dto;

import jakarta.validation.constraints.NotNull;

public record CommentRequest(@NotNull Long blogId, @NotNull Long authorId, String content) {
}
