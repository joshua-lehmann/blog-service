package hftm.joshua.mapper;

import hftm.joshua.data.Author;
import hftm.joshua.dto.AuthorRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface AuthorMapper {
    AuthorRequest toResource(Author author);

    Author fromResource(AuthorRequest authorRequest0);
}
