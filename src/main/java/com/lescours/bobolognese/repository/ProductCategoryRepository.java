package com.lescours.bobolognese.repository;

import com.lescours.bobolognese.model.ProductCategory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductCategoryRepository extends GenericRepository<ProductCategory, Long> {

    public ProductCategoryRepository() {
        super(ProductCategory.class);
    }

    public Optional<ProductCategory> findByName(String name) {
        List<ProductCategory> results = entityManager.createQuery(
                "SELECT c FROM ProductCategory c WHERE c.name = :name", ProductCategory.class)
                .setParameter("name", name)
                .getResultList();

        return results.isEmpty()
                ? Optional.empty()
                : Optional.of(results.getFirst());
    }

    @Transactional
    public ProductCategory saveOrUpdate(ProductCategory productCategory) {
        if (productCategory.getId() == null) {
            return save(productCategory);
        } else {
            return update(productCategory);
        }
    }
}
