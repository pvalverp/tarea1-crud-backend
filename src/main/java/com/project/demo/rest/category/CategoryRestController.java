package com.project.demo.rest.category;

import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.category.CategoryService;
import com.project.demo.logic.entity.category.dto.CategoryRequest;
import com.project.demo.logic.entity.category.dto.CategoryResponse;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
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
@RequestMapping("/categories")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Category> categoriesPage = categoryService.findAll(pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(categoriesPage.getTotalPages());
        meta.setTotalElements(categoriesPage.getTotalElements());
        meta.setPageNumber(categoriesPage.getNumber() + 1);
        meta.setPageSize(categoriesPage.getSize());

        return new GlobalResponseHandler().handleResponse("Categorías obtenidas exitosamente",
                categoriesPage.getContent().stream().map(CategoryResponse::fromEntity).toList(),
                HttpStatus.OK, meta);
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getById(@PathVariable Long categoryId, HttpServletRequest request) {
        Category category = categoryService.findById(categoryId);
        return new GlobalResponseHandler().handleResponse("Categoría obtenida exitosamente",
                CategoryResponse.fromEntity(category), HttpStatus.OK, request);
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> create(@Valid @RequestBody CategoryRequest categoryRequest, HttpServletRequest request) {
        Category savedCategory = categoryService.create(categoryRequest);
        return new GlobalResponseHandler().handleResponse("Categoría creada exitosamente",
                CategoryResponse.fromEntity(savedCategory), HttpStatus.CREATED, request);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> update(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest categoryRequest,
            HttpServletRequest request) {
        Category updatedCategory = categoryService.update(categoryId, categoryRequest);
        return new GlobalResponseHandler().handleResponse("Categoría actualizada exitosamente",
                CategoryResponse.fromEntity(updatedCategory), HttpStatus.OK, request);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<?> delete(@PathVariable Long categoryId, HttpServletRequest request) {
        categoryService.delete(categoryId);
        return new GlobalResponseHandler().handleResponse("Categoría eliminada exitosamente",
                HttpStatus.OK, request);
    }
}
