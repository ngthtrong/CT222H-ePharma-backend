package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.constants.CategoryConstants;
import ct222h.vegeta.projectbackend.dto.request.CategoryRequest;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.CategoryResponse;
import ct222h.vegeta.projectbackend.dto.response.CategoryProductCountResponse;
import ct222h.vegeta.projectbackend.exception.CategoryNotFoundException;
import ct222h.vegeta.projectbackend.model.Category;
import ct222h.vegeta.projectbackend.security.AuthorizationService;
import ct222h.vegeta.projectbackend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;
    
    @Autowired
    private AuthorizationService authorizationService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // PUBLIC ENDPOINTS - No authentication required

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponse> responses = categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, CategoryConstants.SUCCESS_GET_CATEGORIES, responses));
    }

    @GetMapping("/categories/root")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getRootCategories() {
        List<Category> categories = categoryService.getRootCategories();
        List<CategoryResponse> responses = categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, CategoryConstants.SUCCESS_GET_CATEGORIES, responses));
    }

    @GetMapping("/categories/search")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> searchCategories(@RequestParam("q") String keyword) {
        List<Category> categories = categoryService.searchCategories(keyword);
        List<CategoryResponse> responses = categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Tìm kiếm danh mục thành công", responses));
    }

    @GetMapping("/categories/{slug}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryBySlug(@PathVariable String slug) {
        Optional<Category> category = categoryService.getCategoryBySlug(slug);
        return category
                .map(c -> ResponseEntity.ok(new ApiResponse<>(true, CategoryConstants.SUCCESS_GET_CATEGORY, convertToCategoryResponse(c))))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>(false, CategoryConstants.ERROR_CATEGORY_NOT_FOUND, null)));
    }

    @GetMapping("/categories/{id}/children")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategoryChildren(@PathVariable String id) {
        List<Category> children = categoryService.getCategoryChildren(id);
        List<CategoryResponse> responses = children.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, CategoryConstants.SUCCESS_GET_CATEGORY_CHILDREN, responses));
    }

    // ADMIN ENDPOINTS - Require ADMIN role

    @GetMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryByIdAdmin(@PathVariable String id) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        Optional<Category> category = categoryService.getCategoryById(id);
        return category
                .map(c -> ResponseEntity.ok(new ApiResponse<>(true, CategoryConstants.SUCCESS_GET_CATEGORY, convertToCategoryResponse(c))))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>(false, CategoryConstants.ERROR_CATEGORY_NOT_FOUND, null)));
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            Category category = convertToCategory(request);
            Category created = categoryService.createCategory(category);
            return ResponseEntity.status(201).body(new ApiResponse<>(true, CategoryConstants.SUCCESS_CREATE_CATEGORY, convertToCategoryResponse(created)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody CategoryRequest request) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            Category category = convertToCategory(request);
            Category updated = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(new ApiResponse<>(true, CategoryConstants.SUCCESS_UPDATE_CATEGORY, convertToCategoryResponse(updated)));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String id) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse<>(true, CategoryConstants.SUCCESS_DELETE_CATEGORY, null));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/admin/categories/product-counts")
    public ResponseEntity<ApiResponse<List<CategoryProductCountResponse>>> getCategoriesWithProductCount() {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            List<CategoryProductCountResponse> categoryCounts = categoryService.getCategoriesWithProductCount();
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy số lượng sản phẩm theo danh mục thành công", categoryCounts));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lấy số lượng sản phẩm theo danh mục", null));
        }
    }

    // HELPER METHODS

    private Category convertToCategory(CategoryRequest request) {
        return new Category(
                request.getName(),
                request.getSlug(),
                request.getDescription(),
                request.getParentCategoryId()
        );
    }

    private CategoryResponse convertToCategoryResponse(Category category) {
        String parentCategoryName = getParentCategoryName(category.getParentCategoryId());
        
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getParentCategoryId(),
                parentCategoryName,
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    private String getParentCategoryName(String parentCategoryId) {
        if (parentCategoryId == null || parentCategoryId.trim().isEmpty()) {
            return CategoryConstants.DEFAULT_PARENT_CATEGORY_NAME;
        }
        
        Optional<Category> parent = categoryService.getCategoryById(parentCategoryId);
        return parent.map(Category::getName).orElse("Danh mục cha không tồn tại");
    }
}
