package hftm.joshua.data;

import hftm.joshua.enums.SentimentType;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sentiment extends BaseEntity{

    private SentimentType sentimentType;
    private Integer score;

}
