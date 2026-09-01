package com.project.e_commerce.controller;

import com.project.e_commerce.model.Product;
import com.project.e_commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> product(){
        return new ResponseEntity<>(productService.Listofproducts(),HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> product(@PathVariable Integer id){
        Product product=productService.productbyid(id);
        if(product!=null)
        return new ResponseEntity<>(product,HttpStatus.OK);

        return new ResponseEntity<>(product,HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/product", consumes = "multipart/form-data")
    public ResponseEntity<?> productsave(@RequestPart("product") Product product,
                              @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        try {
            Product pro = productService.productsave(product, imageFile);

            return new ResponseEntity<>(pro, HttpStatus.CREATED);
        }
        catch(Exception e) {

            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<byte[]> productimage(@PathVariable Integer id){
        return new ResponseEntity<>(productService.productbyid(id).getImageDate(),HttpStatus.OK);
    }


    @PutMapping("/product/{id}")
    public ResponseEntity<?> productupdate(@PathVariable Integer id,
                                           @RequestPart("product") Product product,
                                           @RequestPart("imageFile") MultipartFile imageFile) throws IOException{
        try {

            Product pro = productService.productsave(product, imageFile);
            return new ResponseEntity<>("Success",HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Soory failed",HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> productdelete(@PathVariable Integer id){
        return new ResponseEntity<>(productService.productdelete(id),HttpStatus.OK);    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> productbyname(@RequestParam String keyword){
        System.out.println(keyword);
        List<Product> product=productService.productbyname(keyword);
        if(product!=null)
            return new ResponseEntity<>(product,HttpStatus.OK);

        return new ResponseEntity<>(product,HttpStatus.NO_CONTENT);
    }


}
