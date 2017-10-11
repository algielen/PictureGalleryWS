package be.algielen.dal;

import be.algielen.entities.Picture;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@RequestScoped
@Transactional(value = TxType.REQUIRED)
public class PictureDao extends GenericDao<Picture> {

    protected Class<Picture> persistentClass = Picture.class;

    public PictureDao() {
    }

    public Optional<Picture> findByUUID(String uuid) {
        TypedQuery<Picture> query = entityManager
            .createQuery("SELECT p FROM Picture p WHERE p.uuid = ?1", persistentClass);
        query.setParameter(1, uuid);
        List<Picture> list = query.getResultList();
        if (list.size() > 0) {
            return Optional.of(list.get(0));
        } else {
            return Optional.empty();
        }
    }


}
