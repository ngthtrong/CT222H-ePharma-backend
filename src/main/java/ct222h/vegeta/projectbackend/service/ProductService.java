package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.model.Product;
import ct222h.vegeta.projectbackend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCaseOrSlugContainingIgnoreCase(keyword, keyword);
    }

    public List<Product> getRelatedProducts(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + id));
        if (product.getRelatedProducts() == null || product.getRelatedProducts().isEmpty()) {
            return List.of();
        }
        return productRepository.findAllById(product.getRelatedProducts());
    }

    public Optional<Product> getProductBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        if (productRepository.existsBySlug(product.getSlug())) {
            throw new RuntimeException("Slug đã tồn tại, vui lòng chọn slug khác.");
        }
        if (productRepository.existsBySku(product.getSku())) {
            throw new RuntimeException("SKU đã tồn tại, vui lòng chọn SKU khác.");
        }
        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product updatedProduct) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + id));

        existing.setName(updatedProduct.getName());
        existing.setSku(updatedProduct.getSku());
        existing.setSlug(updatedProduct.getSlug());
        existing.setDescription(updatedProduct.getDescription());
        existing.setImages(updatedProduct.getImages());
        existing.setPrice(updatedProduct.getPrice());
        existing.setDiscountPercent(updatedProduct.getDiscountPercent());
        existing.setStockQuantity(updatedProduct.getStockQuantity());
        existing.setCategoryId(updatedProduct.getCategoryId());
        existing.setBrand(updatedProduct.getBrand());
        existing.setAttributes(updatedProduct.getAttributes());
        existing.setPublished(updatedProduct.isPublished());
        existing.setRelatedProducts(updatedProduct.getRelatedProducts());

        return productRepository.save(existing);
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy sản phẩm với id: " + id);
        }
        productRepository.deleteById(id);
    }
}