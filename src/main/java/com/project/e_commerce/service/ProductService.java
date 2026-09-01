package com.project.e_commerce.service;

import com.project.e_commerce.controller.ProductController;
import com.project.e_commerce.model.Product;
import com.project.e_commerce.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> Listofproducts(){
        return (List<Product>) repository.findAll();
    }

    public Product productbyid(Integer id){
        return repository.findById(id).orElse(null);
    }
               
    public String productdelete(Integer id){
        repository.deleteById(id);
        return "deleted successfully";
    }
    public String update(Product product){
        repository.save(product);
        return "successfully updated";
    }

    public Product productsave(Product product,MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageDate(image.getBytes());
        repository.save(product);
        return product;
    }


    public List<Product> productbyname(String name) {
        return repository.findByProduct(name);
    }
}
