package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.dto.request.CategoryRequest;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.CategoryResponse;
import ct222h.vegeta.projectbackend.model.Category;
import ct222h.vegeta.projectbackend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Lấy tất cả danh mục (dạng list)
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponse> responses = categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Lấy danh sách danh mục thành công", responses)
        );
    }

    /**
     * Lấy danh mục theo slug
     */
    @GetMapping("/categories/{slug}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryBySlug(@PathVariable String slug) {
        Optional<Category> category = categoryService.getCategoryBySlug(slug);
        return category
                .map(c -> ResponseEntity.ok(
                        new ApiResponse<>(true, "Lấy danh mục thành công", convertToCategoryResponse(c))
                ))
                .orElseGet(() -> ResponseEntity.status(404).body(
                        new ApiResponse<>(false, "Không tìm thấy danh mục", null)
                ));
    }

    /**
     * Tạo danh mục mới (ADMIN)
     */
    @PostMapping("/admin/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {

        Category category = new Category(
                request.getName(),
                request.getSlug(),
                request.getDescription(),
                request.getParentCategoryId()
        );

        Category createdCategory = categoryService.createCategory(category);

        return ResponseEntity.status(201).body(
                new ApiResponse<>(true, "Tạo danh mục thành công", convertToCategoryResponse(createdCategory))
        );
    }

    /**
     * Cập nhật danh mục (ADMIN)
     */
    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody CategoryRequest request) {

        Category category = new Category(
                request.getName(),
                request.getSlug(),
                request.getDescription(),
                request.getParentCategoryId()
        );
        category.setId(id);

        Category updatedCategory = categoryService.updateCategory(id, category);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Cập nhật danh mục thành công", convertToCategoryResponse(updatedCategory))
        );
    }

    /**
     * Xóa danh mục (ADMIN)
     */
    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Xóa danh mục thành công", null)
        );
    }

    /**
     * Lấy danh mục con của 1 danh mục
     */
    @GetMapping("/categories/{id}/children")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategoryChildren(@PathVariable String id) {
        List<Category> children = categoryService.getCategoryChildren(id);
        List<CategoryResponse> responses = children.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Lấy danh sách danh mục con thành công", responses)
        );
    }

    /**
     * Helper chuyển Category -> CategoryResponse
     */
    private CategoryResponse convertToCategoryResponse(Category category) {
        String parentCategoryName = null;
        if (category.getParentCategoryId() != null) {
            Optional<Category> parent = categoryService.getCategoryById(category.getParentCategoryId());
            parentCategoryName = parent.map(Category::getName).orElse(null);
        }
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getParentCategoryId(),
                parentCategoryName
        );
    }
}
