package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.model.Category;
import ct222h.vegeta.projectbackend.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    public Optional<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getCategoryChildren(String parentId) {
        return categoryRepository.findByParentCategoryId(parentId);
    }

    public Category createCategory(Category category) {
        if (categoryRepository.existsBySlug(category.getSlug())) {
            throw new RuntimeException("Slug đã tồn tại, vui lòng chọn slug khác.");
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(String id, Category updatedCategory) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id: " + id));

        existingCategory.setName(updatedCategory.getName());
        existingCategory.setSlug(updatedCategory.getSlug());
        existingCategory.setDescription(updatedCategory.getDescription());
        existingCategory.setParentCategoryId(updatedCategory.getParentCategoryId());

        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy danh mục với id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
