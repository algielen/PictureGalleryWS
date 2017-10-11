package be.algielen.services;

import be.algielen.dal.PictureDao;
import be.algielen.entities.File;
import be.algielen.entities.Picture;
import be.algielen.util.HashingService;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class PicturesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PicturesService.class);

    @Inject
    private PictureDao pictureDao;

    @Inject
    private HashingService hashingService;

    @Transactional(TxType.REQUIRES_NEW)
    public Optional<Picture> getPicture(String uuid) {
        return pictureDao.findByUUID(uuid);
    }

    @Transactional(TxType.REQUIRES_NEW)
    public UUID addPicture(String filename, UUID uuid, String type) {
        File file = new File();
        file.setFilename(filename);

        //byte[] hash = hashingService.hash(content); // TODO : async ?
        byte[] hash = "placeholder".getBytes();
        file.setHash(hash);

        Picture picture = new Picture();
        picture.setFile(file);
        picture.setUuid(uuid);
        picture.setName(filename);

        pictureDao.persist(picture);
        return uuid;
    }
}
