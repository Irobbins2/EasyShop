/*package org.yearup.data.mysql;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;

@Repository
public class ShoppingCartDaoImpl implements ShoppingCartDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ShoppingCart getByUserId(int userId) {
        return entityManager.find(ShoppingCart.class, userId);
    }

    @Override
    @Transactional
    public void saveOrUpdate(ShoppingCart cart) {
        entityManager.merge(cart);
    }

}
*/
