package hftm.joshua.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter

public class TextMessage {
    private String text;
    private Long sourceId;
    private Boolean isValid;

    public TextMessage(Long sourceId, String text) {
        this.text = text;
        this.sourceId = sourceId;
    }
}
