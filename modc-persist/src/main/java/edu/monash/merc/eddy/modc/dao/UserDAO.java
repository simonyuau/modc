package edu.monash.merc.eddy.modc.dao;

import edu.monash.merc.eddy.modc.domain.User;
import edu.monash.merc.eddy.modc.repository.IUserRepository;
import org.hibernate.Criteria;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * Created by simonyu on 8/08/2014.
 */
@Scope("prototype")
@Repository
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "freqRegion")
public class UserDAO extends HibernateGenericDAO<User> implements IUserRepository {

    @Override
    public User getUserByEmail(String email) {
        Criteria userCriteria = this.session().createCriteria(this.persistClass).setCacheable(true);
        return (User) userCriteria.add(Restrictions.eq("email", email).ignoreCase()).uniqueResult();
    }

    @Override
    public User getUserByUniqueId(String uniqueId) {
        Criteria userCriteria = this.session().createCriteria(this.persistClass).setCacheable(true);
        return (User) userCriteria.add(Restrictions.eq("uniqueId", uniqueId)).uniqueResult();
    }

    @Override
    public boolean checkUserExistedName(String displayName) {
        long num = (Long) this.session().createCriteria(this.persistClass).setCacheable(true).setProjection(Projections.rowCount())
                .add(Restrictions.eq("displayName", displayName).ignoreCase()).uniqueResult();
        if (num == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkUserExistedUniqueId(String uniqueId) {
        long num = (Long) this.session().createCriteria(this.persistClass).setCacheable(true).setProjection(Projections.rowCount())
                .add(Restrictions.eq("uniqueId", uniqueId).ignoreCase()).uniqueResult();
        if (num == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkExistedEmail(String email) {
        long num = (Long) this.session().createCriteria(this.persistClass).setCacheable(true).setProjection(Projections.rowCount())
                .add(Restrictions.eq("email", email).ignoreCase()).uniqueResult();
        if (num == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User checkUserLogin(String uniqueId, String password) {
        return (User) this.session().createCriteria(this.persistClass).setCacheable(true).add(Restrictions.eq("uniqueId", uniqueId))
                .add(Restrictions.eq("password", password)).uniqueResult();
    }
}
