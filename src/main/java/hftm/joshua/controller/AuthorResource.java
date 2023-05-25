package hftm.joshua.controller;

import hftm.joshua.data.Author;
import hftm.joshua.dto.AuthorRequest;
import hftm.joshua.mapper.AuthorMapper;
import hftm.joshua.repository.AuthorRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.reactive.RestPath;

import java.util.List;

@Path("/author")
public class AuthorResource {

    @Inject
    AuthorRepository authorRepository;

    @Inject
    AuthorMapper authorMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Author> getAuthors() {
        return authorRepository.listAll();
    }

    @GET
    @Path("/{id}")
    @Parameter(name = "id", description = "The id of the author to retrieve", required = true, example = "1")
    public Author getAuthor(@RestPath("id") Long id) {
        return authorRepository.findById(id);
    }

    @POST
    @RequestBody(description = "The Author Object to create", content = @Content(schema = @Schema(implementation = AuthorRequest.class),
            examples = {@ExampleObject(name = "Simple Example", value = "{\"firstName\": \"Joshua\", \"lastName\": \"Lehmann\"}")}
    ))
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void createAuthor(AuthorRequest author) {
        authorRepository.persist(authorMapper.fromResource(author));
    }
}
