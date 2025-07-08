package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.dto.request.CategoryRequest;
import ct222h.vegeta.projectbackend.dto.response.CategoryResponse;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.model.Category;
import ct222h.vegeta.projectbackend.service.CategoryService;
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

    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponse> responses = categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Lấy danh sách danh mục thành công", responses);
    }

    @GetMapping("/categories/{slug}")
    public ApiResponse<CategoryResponse> getCategoryBySlug(@PathVariable String slug) {
        Optional<Category> category = categoryService.getCategoryBySlug(slug);
        if (category.isPresent()) {
            CategoryResponse response = convertToCategoryResponse(category.get());
            return new ApiResponse<>(true, "Lấy danh mục thành công", response);
        } else {
            return new ApiResponse<>(false, "Không tìm thấy danh mục", null);
        }
    }

    @PostMapping("/admin/categories")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        Category category = new Category(
                request.getName(),
                request.getSlug(),
                request.getDescription(),
                request.getParentCategoryId()
        );
        Category createdCategory = categoryService.createCategory(category);
        CategoryResponse response = convertToCategoryResponse(createdCategory);
        return new ApiResponse<>(true, "Tạo danh mục thành công", response);
    }

    @PutMapping("/admin/categories/{id}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable String id, @RequestBody CategoryRequest request) {
        Category category = new Category(
                request.getName(),
                request.getSlug(),
                request.getDescription(),
                request.getParentCategoryId()
        );
        category.setId(id);
        Category updatedCategory = categoryService.updateCategory(id, category);
        CategoryResponse response = convertToCategoryResponse(updatedCategory);
        return new ApiResponse<>(true, "Cập nhật danh mục thành công", response);
    }

    @DeleteMapping("/admin/categories/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return new ApiResponse<>(true, "Xóa danh mục thành công", null);
    }

    @GetMapping("/categories/{id}/children")
    public ApiResponse<List<CategoryResponse>> getCategoryChildren(@PathVariable String id) {
        List<Category> children = categoryService.getCategoryChildren(id);
        List<CategoryResponse> responses = children.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Lấy danh mục con thành công", responses);
    }

    // Helper method to convert Category to CategoryResponse
    private CategoryResponse convertToCategoryResponse(Category category) {
        String parentCategoryName = null;
        if (category.getParentCategoryId() != null) {
            Optional<Category> parentCategory = categoryService.getCategoryById(category.getParentCategoryId());
            if (parentCategory.isPresent()) {
                parentCategoryName = parentCategory.get().getName();
            }
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
