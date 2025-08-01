package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.constants.CategoryConstants;
import ct222h.vegeta.projectbackend.exception.CategoryNotFoundException;
import ct222h.vegeta.projectbackend.model.Category;
import ct222h.vegeta.projectbackend.repository.CategoryRepository;
import ct222h.vegeta.projectbackend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * Get all categories
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Get root categories (no parent)
     */
    public List<Category> getRootCategories() {
        return categoryRepository.findByParentCategoryIdIsNull();
    }

    /**
     * Get category by slug
     */
    public Optional<Category> getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    /**
     * Get category by ID
     */
    public Optional<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    /**
     * Get category children
     */
    public List<Category> getCategoryChildren(String parentId) {
        return categoryRepository.findByParentCategoryId(parentId);
    }

    /**
     * Search categories by name
     */
    public List<Category> searchCategories(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword);
    }

    /**
     * Create new category
     */
    public Category createCategory(Category category) {
        validateCategoryCreation(category);
        return categoryRepository.save(category);
    }

    /**
     * Update existing category
     */
    public Category updateCategory(String id, Category updatedCategory) {
        Category existing = getCategoryByIdOrThrow(id);
        validateCategoryUpdate(existing, updatedCategory);
        updateCategoryFields(existing, updatedCategory);
        return categoryRepository.save(existing);
    }

    /**
     * Delete category
     */
    public void deleteCategory(String id) {
        Category category = getCategoryByIdOrThrow(id);
        validateCategoryDeletion(category);
        categoryRepository.deleteById(id);
    }

    /**
     * Get product count for each category (including products in subcategories)
     */
    public List<ct222h.vegeta.projectbackend.dto.response.CategoryProductCountResponse> getCategoriesWithProductCount() {
        List<Category> categories = categoryRepository.findAll();
        
        return categories.stream()
                .map(category -> {
                    long productCount = getTotalProductCountForCategory(category.getId());
                    return new ct222h.vegeta.projectbackend.dto.response.CategoryProductCountResponse(
                            category.getId(),
                            category.getName(),
                            category.getSlug(),
                            productCount
                    );
                })
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get total product count for a category including all subcategories
     */
    private long getTotalProductCountForCategory(String categoryId) {
        // Count products directly in this category
        long directProductCount = productRepository.countByCategoryId(categoryId);
        
        // Get all subcategories and count their products recursively
        List<Category> subcategories = categoryRepository.findByParentCategoryId(categoryId);
        long subcategoryProductCount = subcategories.stream()
                .mapToLong(subcategory -> getTotalProductCountForCategory(subcategory.getId()))
                .sum();
        
        return directProductCount + subcategoryProductCount;
    }

    // Private helper methods
    
    private Category getCategoryByIdOrThrow(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(CategoryConstants.ERROR_CATEGORY_NOT_FOUND + " với id: " + id));
    }

    private void validateCategoryCreation(Category category) {
        // Check slug uniqueness
        if (categoryRepository.existsBySlug(category.getSlug())) {
            throw new IllegalArgumentException(CategoryConstants.ERROR_SLUG_EXISTS);
        }
        
        // Validate parent category exists
        if (category.getParentCategoryId() != null && 
            !categoryRepository.existsById(category.getParentCategoryId())) {
            throw new IllegalArgumentException(CategoryConstants.ERROR_INVALID_PARENT_CATEGORY);
        }
    }

    private void validateCategoryUpdate(Category existing, Category updated) {
        // Check slug uniqueness (exclude current category)
        if (!existing.getSlug().equals(updated.getSlug()) && 
            categoryRepository.existsBySlug(updated.getSlug())) {
            throw new IllegalArgumentException(CategoryConstants.ERROR_SLUG_EXISTS);
        }
        
        // Validate parent category exists
        if (updated.getParentCategoryId() != null && 
            !categoryRepository.existsById(updated.getParentCategoryId())) {
            throw new IllegalArgumentException(CategoryConstants.ERROR_INVALID_PARENT_CATEGORY);
        }
        
        // Prevent circular reference
        if (updated.getParentCategoryId() != null && 
            updated.getParentCategoryId().equals(existing.getId())) {
            throw new IllegalArgumentException(CategoryConstants.ERROR_CIRCULAR_REFERENCE);
        }
    }

    private void validateCategoryDeletion(Category category) {
        // Check if category has children
        if (categoryRepository.existsByParentCategoryId(category.getId())) {
            throw new IllegalStateException(CategoryConstants.ERROR_CATEGORY_HAS_CHILDREN);
        }
        
        // Check if category has products
        if (productRepository.existsByCategoryId(category.getId())) {
            throw new IllegalStateException(CategoryConstants.ERROR_CATEGORY_HAS_PRODUCTS);
        }
    }

    private void updateCategoryFields(Category existing, Category updated) {
        existing.setName(updated.getName());
        existing.setSlug(updated.getSlug());
        existing.setDescription(updated.getDescription());
        existing.setParentCategoryId(updated.getParentCategoryId());
    }
}
