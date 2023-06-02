package hftm.joshua.controller;

import hftm.joshua.data.Author;
import hftm.joshua.repository.AuthorRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.List;

@Path("/author")
public class AuthorResource {

    @Inject
    AuthorRepository authorRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Author> getAuthors() {
        return authorRepository.listAll();
    }

    @GET
    @Path("/{id}")
    @Parameter(name = "id", description = "The id of the author to retrieve", required = true )
    public Author getAuthor(@PathParam("id") Long id) {
        return authorRepository.findById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void createAuthor(@RequestBody(description = "The Author Object to create", content = @Content(schema = @Schema(implementation = Author.class))) Author author) {
        authorRepository.persist(author);
    }
}
