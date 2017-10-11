package be.algielen.rest;

import be.algielen.entities.Picture;
import be.algielen.services.FileStore;
import be.algielen.services.PicturesService;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("pictures")
public class PicturesRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PicturesRest.class);

    @Context
    private UriInfo context;

    @Inject
    private PicturesService picturesService;

    @Inject
    private FileStore fileStore;

    @GET
    @Produces("image/*")
    @Path("picture/{uuid}")
    public Response getPicture(@PathParam("uuid") String uuid) {
        Response response;
        Optional<Picture> optional = picturesService.getPicture(uuid);
        if (optional.isPresent()) {
            Picture picture = optional.get();
            try {
                byte[] content = fileStore.getFile(picture.getUuid().toString());
                response = Response.ok(content).type(picture.getType()).build();
            } catch (IOException e) {
                LOGGER.error("Image not found for " + uuid, e);
                response = Response.serverError().build();
            }
            return response;
        } else {
            response = Response.status(404).build();
        }
        return response;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("picture")
    public Response addPicture(@QueryParam("filename") String filename,
                               @QueryParam("type") String type,
                               @Multipart(value = "picture", type = "image/*") InputStream inputStream) {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        UUID uuid = UUID.randomUUID();
        Response response;
        try {
            fileStore.addFile(uuid.toString(), bufferedInputStream);
            picturesService.addPicture(filename, uuid, type);
            response = Response.ok(uuid.toString()).build();
        } catch (IOException e) {
            LOGGER.error("Failed to add picture", e);
            response = Response.serverError().build();
        }
        return response;
    }
}
