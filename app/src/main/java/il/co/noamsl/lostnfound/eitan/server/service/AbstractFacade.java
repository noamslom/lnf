package il.co.noamsl.lostnfound.eitan.server.service;

import javax.persistence.EntityManager;

public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public abstract Integer edit(T entity);
    
    public Integer create(T entity) {
        getEntityManager().persist(entity);
        return 0;
    }

    public final T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }
}
