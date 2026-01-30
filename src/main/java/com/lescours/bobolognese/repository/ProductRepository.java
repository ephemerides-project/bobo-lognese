package com.lescours.bobolognese.repository;

import com.lescours.bobolognese.model.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ProductRepository extends GenericRepository<Product, Long> {

    public ProductRepository() {
        super(Product.class);
    }

    public Product findBySku(String sku) {
        List<Product> results = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.sku = :sku", Product.class)
                .setParameter("sku", sku)
                .getResultList();

        return results.isEmpty()
                ? null
                : results.getFirst();
    }

    @Transactional
    public Product saveOrUpdate(Product product) {
        if (product.getId() == null) {
            return save(product);
        } else {
            return update(product);
        }
    }
}
