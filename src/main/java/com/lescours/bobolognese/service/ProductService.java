package com.lescours.bobolognese.service;

import com.lescours.bobolognese.model.Product;
import com.lescours.bobolognese.repository.ProductRepository;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ProductService implements Serializable {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductCategoryService productCategoryService;

    public Product create(String name,
                          String category,
                          BigDecimal price,
                          Integer stock,
                          String sku,
                          String color,
                          String description) {
        var product = Product.builder()
                .name(name)
                .productCategory(productCategoryService.findOrCreate(category))
                .price(price)
                .stock(stock)
                .sku(sku)
                .color(color)
                .description(description)
                .build();

        return save(product);
    }

    public Product save(Product product) {
        return productRepository.saveOrUpdate(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    public Product getBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

}
