package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.constants.ProductConstants;
import ct222h.vegeta.projectbackend.exception.ProductNotFoundException;
import ct222h.vegeta.projectbackend.model.Product;
import ct222h.vegeta.projectbackend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCaseOrSlugContainingIgnoreCase(keyword, keyword);
    }

    public List<Product> getAllProductsWithFilters(String category, String brand, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock, Boolean published) {
        List<Product> products = productRepository.findAll();
        
        return products.stream()
                .filter(p -> filterByCategory(p, category))
                .filter(p -> filterByBrand(p, brand))
                .filter(p -> filterByPriceRange(p, minPrice, maxPrice))
                .filter(p -> filterByStock(p, inStock))
                .filter(p -> filterByPublished(p, published))
                .toList();
    }

    public List<Product> getRelatedProducts(String id) {
        Product product = getProductByIdOrThrow(id);
        
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
        validateProductUniqueness(product);
        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product updatedProduct) {
        Product existing = getProductByIdOrThrow(id);
        updateProductFields(existing, updatedProduct);
        return productRepository.save(existing);
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(ProductConstants.ERROR_PRODUCT_NOT_FOUND + " với id: " + id);
        }
        productRepository.deleteById(id);
    }

    // Private helper methods
    
    private Product getProductByIdOrThrow(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(ProductConstants.ERROR_PRODUCT_NOT_FOUND + " với id: " + id));
    }

    private void validateProductUniqueness(Product product) {
        if (productRepository.existsBySlug(product.getSlug())) {
            throw new IllegalArgumentException(ProductConstants.ERROR_SLUG_EXISTS);
        }
        if (productRepository.existsBySku(product.getSku())) {
            throw new IllegalArgumentException(ProductConstants.ERROR_SKU_EXISTS);
        }
    }

    private boolean filterByCategory(Product product, String category) {
        return category == null || (product.getCategoryId() != null && product.getCategoryId().equals(category));
    }

    private boolean filterByBrand(Product product, String brand) {
        return brand == null || (product.getBrand() != null && product.getBrand().equalsIgnoreCase(brand));
    }

    private boolean filterByPriceRange(Product product, BigDecimal minPrice, BigDecimal maxPrice) {
        if (product.getPrice() == null) return false;
        boolean minCheck = minPrice == null || product.getPrice().compareTo(minPrice) >= 0;
        boolean maxCheck = maxPrice == null || product.getPrice().compareTo(maxPrice) <= 0;
        return minCheck && maxCheck;
    }

    private boolean filterByStock(Product product, Boolean inStock) {
        return inStock == null || (!inStock || product.getStockQuantity() > 0);
    }

    private boolean filterByPublished(Product product, Boolean published) {
        return published == null || product.isPublished() == published;
    }

    private void updateProductFields(Product existing, Product updated) {
        existing.setName(updated.getName());
        existing.setSku(updated.getSku());
        existing.setSlug(updated.getSlug());
        existing.setDescription(updated.getDescription());
        existing.setImages(updated.getImages());
        existing.setPrice(updated.getPrice());
        existing.setDiscountPercent(updated.getDiscountPercent());
        existing.setStockQuantity(updated.getStockQuantity());
        existing.setCategoryId(updated.getCategoryId());
        existing.setBrand(updated.getBrand());
        existing.setAttributes(updated.getAttributes());
        existing.setPublished(updated.isPublished());
        existing.setRelatedProducts(updated.getRelatedProducts());
    }
}