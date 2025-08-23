package org.example.repositories;

import org.example.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository {
    private final List<Product> products = new ArrayList<>();
    private int nextID = 1;



    public ProductRepository() {
        products.add(new Product(0, "Chair", 400, 2));
    }


    public List<Product> getAll() {
        return products;
    }


    public Product getById (int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }


    public void save(Product product) {
        product.setId(nextID++);
        products.add(product);
    }


    public boolean update(Product product) {

        for(int i = 0; i < products.size(); i++) {
            if(products.get(i).getId() == product.getId()) {
                products.set(i, product);
                return true;
            }
        }
        return false;
    }




    public boolean delete (int id) {
        return products.removeIf(product -> product.getId() == id);
    }

}
