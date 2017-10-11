package be.algielen.dal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public abstract class GenericDao<T> {

    protected Class<T> persistentClass;

    @PersistenceContext(unitName = "PictureGallery")
    protected EntityManager entityManager;

    public T find(long id) {
        return entityManager.find(persistentClass, id);
    }

    public void persist(T object) {
        entityManager.persist(object);
    }
}
