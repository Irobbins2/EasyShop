/*package org.yearup.data.mysql;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

@Repository
public class ProfileDaoImpl implements ProfileDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Profile getProfileById(int userId) {
        return entityManager.find(Profile.class, userId);
    }

    @Override
    @Transactional
    public void saveOrUpdate(Profile profile) {
        entityManager.merge(profile);
    }

    @Override
    @Transactional
    public void delete(Profile profile) {
        entityManager.remove(entityManager.contains(profile) ? profile : entityManager.merge(profile));
    }

    @Override
    @Transactional
    public void create(Profile profile) {
        entityManager.persist(profile);
    }

}
*/
