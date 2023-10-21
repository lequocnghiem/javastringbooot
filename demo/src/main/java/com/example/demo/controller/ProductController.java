package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;


import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.hibernate.ResourceClosedException;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.example.demo.Entity.Product;
import com.example.demo.productrepons.ProductRepository;
import com.example.demo.respone.ResourceNotFoundException;
;
@CrossOrigin(origins = "*")
@RestController

@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductRepository productRepository;
    
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestPart("imageFile") MultipartFile imageFile,
            @RequestParam("idCategory") Long idCategory,
            @RequestParam("idBrand") Long idBrand,
            @RequestParam("priceSale") double priceSale,
            @RequestParam("qty") int qty,
            @RequestParam("status") String status,
            @RequestParam("description") String description) {
        try {
           
            byte[] imageData = imageFile.getBytes();

            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setImageData(imageData); // Set mảng byte vào trường imageData
            product.setIdCategory(idCategory);
            product.setIdBrand(idBrand);
            product.setPriceSale(priceSale);
            product.setQty(qty);
            product.setStatus(status);
            product.setDescription(description);

            // Lưu sản phẩm vào cơ sở dữ liệu
            final Product savedProduct = productRepository.save(product);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    
    

    
  

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceClosedException("Product not found with id " + id));
        return ResponseEntity.ok(product);
    }

  
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
        @PathVariable Long id,
        @RequestParam("name") String name,
        @RequestParam("price") double price,
        @RequestPart("imageFile") MultipartFile imageFile,
        @RequestParam("idCategory") Long idCategory,
        @RequestParam("idBrand") Long idBrand,
        @RequestParam("priceSale") double priceSale,
        @RequestParam("qty") int qty,
        @RequestParam("status") String status,
        @RequestParam("description") String description) {
        try {
            Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

            byte[] newImageData = imageFile.getBytes();

            existingProduct.setName(name);
            existingProduct.setPrice(price);
            existingProduct.setImageData(newImageData); // Cập nhật dữ liệu hình ảnh dưới dạng mảng byte
            existingProduct.setIdCategory(idCategory);
            existingProduct.setIdBrand(idBrand);
            existingProduct.setPriceSale(priceSale);
            existingProduct.setQty(qty);
            existingProduct.setStatus(status);
            existingProduct.setDescription(description);

            // Lưu sản phẩm đã cập nhật vào cơ sở dữ liệu
            final Product updatedProduct = productRepository.save(existingProduct);

            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

            // Xóa sản phẩm từ cơ sở dữ liệu
            productRepository.delete(existingProduct);

            return ResponseEntity.ok("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    

    
}
