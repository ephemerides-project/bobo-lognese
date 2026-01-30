package com.lescours.bobolognese.bean;

import com.lescours.bobolognese.model.Product;
import com.lescours.bobolognese.service.ProductCategoryService;
import com.lescours.bobolognese.service.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Named("productBean")
@ViewScoped
public class ProductBean implements Serializable {

    @Inject
    private ProductService productService;

    @Inject
    private ProductCategoryService productCategoryService;

    @Getter
    @Setter
    private Product currentProduct;

    @Getter
    @Setter
    private Long productId;

    @PostConstruct
    public void init() {
        currentProduct = new Product();
    }

    public void loadProduct() {
        if (productId != null) {
            currentProduct = productService.getById(productId).orElse(null);

            assert currentProduct != null;
            currentProduct.setProductCategoryString(
                    currentProduct.getProductCategory().getName()
            );
        }
    }

    public List<Product> getProducts() {
        return productService.getAll();
    }

    public String saveProduct() {
        if (currentProduct.getProductCategoryString() != null) {
            currentProduct.setProductCategory(
                    productCategoryService.findOrCreate(currentProduct.getProductCategoryString())
            );
            currentProduct.setProductCategoryString(null);
        }

        Product saved = productService.save(currentProduct);

        return "product?faces-redirect=true&id=" + saved.getId();
    }

    public String editProduct(Long id) {
        return "product-form?faces-redirect=true&id=" + id;
    }

    public String deleteProduct(Long id) {
        productService.delete(id);
        return "index?faces-redirect=true";
    }

    public String viewProduct(Long id) {
        return "product?faces-redirect=true&id=" + id;
    }

    public String newProduct() {
        return "product-form?faces-redirect=true";
    }

    public String cancel() {
        return "index?faces-redirect=true";
    }

    public boolean isEditMode() {
        return currentProduct != null && currentProduct.getId() != null;
    }

    public boolean isProductFound() {
        return currentProduct != null;
    }

}
