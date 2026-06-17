package com.project.demo.rest.product;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductService;
import com.project.demo.logic.entity.product.dto.ProductRequest;
import com.project.demo.logic.entity.product.dto.ProductResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productsPage = productService.findAll(pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(productsPage.getTotalPages());
        meta.setTotalElements(productsPage.getTotalElements());
        meta.setPageNumber(productsPage.getNumber() + 1);
        meta.setPageSize(productsPage.getSize());

        return new GlobalResponseHandler().handleResponse("Productos obtenidos exitosamente",
                productsPage.getContent().stream().map(ProductResponse::fromEntity).toList(),
                HttpStatus.OK, meta);
    }

    @GetMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getById(@PathVariable Long productId, HttpServletRequest request) {
        Product product = productService.findById(productId);
        return new GlobalResponseHandler().handleResponse("Producto obtenido exitosamente",
                ProductResponse.fromEntity(product), HttpStatus.OK, request);
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> create(@Valid @RequestBody ProductRequest productRequest, HttpServletRequest request) {
        Product savedProduct = productService.create(productRequest);
        return new GlobalResponseHandler().handleResponse("Producto creado exitosamente",
                ProductResponse.fromEntity(savedProduct), HttpStatus.CREATED, request);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> update(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest productRequest,
            HttpServletRequest request) {
        Product updatedProduct = productService.update(productId, productRequest);
        return new GlobalResponseHandler().handleResponse("Producto actualizado exitosamente",
                ProductResponse.fromEntity(updatedProduct), HttpStatus.OK, request);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> delete(@PathVariable Long productId, HttpServletRequest request) {
        productService.delete(productId);
        return new GlobalResponseHandler().handleResponse("Producto eliminado exitosamente",
                HttpStatus.OK, request);
    }
}
