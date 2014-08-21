package edu.monash.merc.eddy.modc.dao;

import edu.monash.merc.eddy.modc.repository.IRepository;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by simonyu on 4/08/2014.
 */

@Scope("prototype")
@Repository
public class HibernateGenericDAO<T> implements IRepository<T> {

    protected Class<T> persistClass;

    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }



    @SuppressWarnings("unchecked")
    public HibernateGenericDAO() {
        //get first actual type arguments -- T an entity object class.
        this.persistClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    @SuppressWarnings("unchecked")
    public HibernateGenericDAO(SessionFactory sessionFactory) {
        //call default constructor.
        //this();
        this.sessionFactory = sessionFactory;
        this.persistClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Session session() {
        return this.sessionFactory.getCurrentSession();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(long id) {
        return (T) this.session().get(this.persistClass, id);
    }

    @Override
    public void add(T entity) {
        this.session().save(entity);
    }

    @Override
    public void remove(T entity) {
        this.session().delete(entity);
    }

    @Override
    public void delete(long id) {
        String delHQL = "DELETE FROM " + this.persistClass.getSimpleName() + " AS ent WHERE ent.id = :id";
        Query delQuery = this.session().createQuery(delHQL);
        delQuery.setLong("id", id);
        delQuery.executeUpdate();
    }

    @Override
    public void update(T entity) {
        this.session().update(entity);
    }

    @Override
    public void merge(T entity) {
        this.session().merge(entity);
    }

    @Override
    public int saveAll(List<T> entities) {
        int insertedCount = 0;
        for (int i = 0; i < entities.size(); i++) {
            add(entities.get(i));
            if (++insertedCount % 20 == 0) {
                flushAndClear();
            }
        }
        flushAndClear();
        return insertedCount;
    }

    protected void flushAndClear() {
        if (this.session().isDirty()) {
            this.session().flush();
            this.session().clear();
        }
    }
}
