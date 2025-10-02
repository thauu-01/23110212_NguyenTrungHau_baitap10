package baitap10.controller;

import baitap10.entity.Product;
import baitap10.model.Response;
import baitap10.service.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductAPIController {

    @Autowired
    private IProductService productService;

    @GetMapping
    public ResponseEntity<Response> getAllProduct() {
        return new ResponseEntity<>(new Response(true, "Thành công", productService.findAll()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getProduct(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<>(new Response(true, "Thành công", product.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, "Thất bại", null), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Response> addProduct(@RequestBody Product product) {
        productService.save(product);
        return new ResponseEntity<>(new Response(true, "Thêm thành công", product), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Optional<Product> optProduct = productService.findById(id);
        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            product.setProductName(productDetails.getProductName());
            product.setQuantity(productDetails.getQuantity());
            product.setUnitPrice(productDetails.getUnitPrice());
            // Cập nhật các field khác tương tự
            productService.save(product);
            return new ResponseEntity<>(new Response(true, "Cập nhật thành công", product), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Product", null), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long id) {
        Optional<Product> optProduct = productService.findById(id);
        if (optProduct.isPresent()) {
            productService.deleteById(id);
            return new ResponseEntity<>(new Response(true, "Xóa thành công", null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Product", null), HttpStatus.NOT_FOUND);
        }
    }
}