package hftm.joshua.mapper;


import hftm.joshua.data.Blog;
import hftm.joshua.dto.BlogRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface BlogMapper {
    Blog fromResource(BlogRequest blogRequest);
}
