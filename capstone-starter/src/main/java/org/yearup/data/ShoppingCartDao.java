package org.yearup.data;

import org.springframework.transaction.annotation.Transactional;
import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao {
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    void saveOrUpdate(ShoppingCart cart);
}
