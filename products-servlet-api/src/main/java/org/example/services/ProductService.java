package org.example.services;

import jakarta.servlet.http.HttpServletResponse;
import org.example.errors.ApiException;
import org.example.models.Product;
import org.example.repositories.ProductRepository;
import java.util.List;

public class ProductService {
    private ProductRepository repo = new ProductRepository();

    public List<Product> getAllProducts() {
        return repo.getAll();
    }

    public Product getProductById(int id) {
        Product p =  repo.getById(id);
        if(p == null) {
            throw new ApiException(404, "Product with given id is not found");
        }
        return p;
    }


    public void addProduct(Product product) {
        product.validate();
        repo.save(product);
    }

    public void deleteProduct(int id) {
        boolean isDeleted = repo.delete(id);
        if(!isDeleted) {
            throw new ApiException(404, "Product with given id is not found");
        }
    }

    public Product updateProduct(int id, Product updatedProduct) {
        Product product = repo.getById(id);
        if(product == null) {
            throw new ApiException(HttpServletResponse.SC_NOT_FOUND, "Product with given id is not found");
        }

        updatedProduct.validate();

        product.setId(id);
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setQuantity(updatedProduct.getQuantity());

        repo.update(product);

        return product;
    }
}
