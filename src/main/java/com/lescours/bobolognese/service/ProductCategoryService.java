package com.lescours.bobolognese.service;

import com.lescours.bobolognese.model.ProductCategory;
import com.lescours.bobolognese.repository.ProductCategoryRepository;
import jakarta.inject.Inject;

import java.io.Serializable;

public class ProductCategoryService implements Serializable {

    @Inject
    private ProductCategoryRepository productCategoryRepository;

    public ProductCategory findOrCreate(String name) {
        var category = productCategoryRepository.findByName(name);

        return category.orElseGet(() ->
                productCategoryRepository.saveOrUpdate(new ProductCategory(name)));
    }

}
