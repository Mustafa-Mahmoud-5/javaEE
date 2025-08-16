package org.example.services;

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
}
