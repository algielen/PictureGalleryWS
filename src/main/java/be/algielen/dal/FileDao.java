package be.algielen.dal;

import be.algielen.entities.File;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

@RequestScoped
@Transactional(value = TxType.REQUIRED)
public class FileDao extends GenericDao<File> {

    protected Class<File> persistentClass = File.class;

    public FileDao() {
    }
}
