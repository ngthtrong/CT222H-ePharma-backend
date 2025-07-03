package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.dto.request.CategoryRequest;
import ct222h.vegeta.projectbackend.model.Category;
import ct222h.vegeta.projectbackend.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/categories/{slug}")
    public ResponseEntity<Category> getCategoryBySlug(@PathVariable String slug) {
        Optional<Category> category = categoryService.getCategoryBySlug(slug);
        return category.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/admin/categories")
    public Category createCategory(@RequestBody CategoryRequest request) {
        Category category = new Category(
                request.getName(),
                request.getSlug(),
                request.getParentId(),
                request.getDescription()
        );
        return categoryService.createCategory(category);
    }

    @PutMapping("/admin/categories/{id}")
    public Category updateCategory(@PathVariable String id, @RequestBody CategoryRequest request) {
        Category category = new Category(
                request.getName(),
                request.getSlug(),
                request.getParentId(),
                request.getDescription()
        );
        category.setId(id);
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
