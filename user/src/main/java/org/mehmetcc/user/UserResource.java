package org.mehmetcc.user;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @GET
    public Uni<List<User>> getAllUsers() {
        return User.list("isActive", true);
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getUserById(@PathParam("id") Integer id) {
        return User.findById(id)
                .onItem()
                .ifNotNull()
                .transform(user -> Response.ok(user).build())
                .onItem()
                .ifNull()
                .continueWith(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Uni<Response> createUser(User user) {
        return Panache.withTransaction(user::persistAndFlush)
                .onItem()
                .transform(inserted -> Response.status(Response.Status.CREATED)
                        .entity(inserted)
                        .build());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> updateUser(@PathParam("id") Integer id, User user) {
        return Panache.withTransaction(() ->
                        User.<User>findById(id)
                                .onItem()
                                .ifNotNull()
                                .transformToUni(existingUser -> {
                                    existingUser.firstName = user.firstName;
                                    existingUser.lastName = user.lastName;
                                    return existingUser.persistAndFlush();
                                }))
                .onItem()
                .ifNotNull()
                .transform(updated -> Response.ok(updated).build())
                .onItem()
                .ifNull()
                .continueWith(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") Integer id) {
        return Panache.withTransaction(() ->
                        User.<User>findById(id)
                                .onItem()
                                .ifNotNull()
                                .transformToUni(existingUser -> {
                                    existingUser.isActive = false;
                                    return existingUser.persistAndFlush();
                                }))
                .onItem()
                .ifNotNull()
                .transform(updated -> Response.ok(updated).build())
                .onItem()
                .ifNull()
                .continueWith(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
