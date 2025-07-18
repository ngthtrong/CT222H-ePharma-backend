package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.constants.ProductConstants;
import ct222h.vegeta.projectbackend.dto.request.ProductRequest;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.ProductResponse;
import ct222h.vegeta.projectbackend.model.Category;
import ct222h.vegeta.projectbackend.model.Product;
import ct222h.vegeta.projectbackend.security.AuthorizationService;
import ct222h.vegeta.projectbackend.service.CategoryService;
import ct222h.vegeta.projectbackend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    
    @Autowired
    private AuthorizationService authorizationService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // PUBLIC ENDPOINTS - No authentication required

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponse> responses = products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, ProductConstants.SUCCESS_GET_PRODUCTS, responses));
    }

    @GetMapping("/products/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(@RequestParam("q") String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        List<ProductResponse> responses = products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, ProductConstants.SUCCESS_SEARCH_PRODUCTS, responses));
    }

    @GetMapping("/products/{slug}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySlug(@PathVariable String slug) {
        Optional<Product> product = productService.getProductBySlug(slug);
        return product
                .map(p -> ResponseEntity.ok(new ApiResponse<>(true, ProductConstants.SUCCESS_GET_PRODUCT, convertToProductResponse(p))))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>(false, ProductConstants.ERROR_PRODUCT_NOT_FOUND, null)));
    }

    @GetMapping("/products/{id}/related")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getRelatedProducts(
            @PathVariable String id,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock) {
        
        List<Product> products = productService.getRelatedProducts(id);
        List<Product> filteredProducts = applyProductFilters(products, brand, minPrice, maxPrice, inStock);
        
        List<ProductResponse> responses = filteredProducts.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, ProductConstants.SUCCESS_GET_RELATED_PRODUCTS, responses));
    }

    // ADMIN ENDPOINTS - Require ADMIN role

    @GetMapping("/admin/products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProductsWithFilters(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) Boolean published,
            @RequestParam(required = false) String search) {
        
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        List<Product> products;
        
        if (search != null && !search.trim().isEmpty()) {
            // Search functionality
            products = productService.searchProducts(search.trim());
        } else {
            // Filter functionality
            products = productService.getAllProductsWithFilters(category, brand, minPrice, maxPrice, inStock, published);
        }
        
        List<ProductResponse> responses = products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());

        String message = search != null ? "Tìm kiếm sản phẩm thành công" : "Lấy danh sách sản phẩm với filter thành công";
        return ResponseEntity.ok(new ApiResponse<>(true, message, responses));
    }

    @GetMapping("/admin/products/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductByIdAdmin(@PathVariable String id) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        Optional<Product> product = productService.getProductById(id);
        return product
                .map(p -> ResponseEntity.ok(new ApiResponse<>(true, ProductConstants.SUCCESS_GET_PRODUCT, convertToProductResponse(p))))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>(false, ProductConstants.ERROR_PRODUCT_NOT_FOUND, null)));
    }

    @PostMapping("/admin/products")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        Product product = convertToProduct(request);
        Product created = productService.createProduct(product);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, ProductConstants.SUCCESS_CREATE_PRODUCT, convertToProductResponse(created)));
    }

    @PutMapping("/admin/products/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequest request) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        Product product = convertToProduct(request);
        product.setId(id);
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(new ApiResponse<>(true, ProductConstants.SUCCESS_UPDATE_PRODUCT, convertToProductResponse(updated)));
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String id) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse<>(true, ProductConstants.SUCCESS_DELETE_PRODUCT, null));
    }

    // HELPER METHODS

    private List<Product> applyProductFilters(List<Product> products, String brand, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock) {
        return products.stream()
                .filter(p -> filterByBrand(p, brand))
                .filter(p -> filterByMinPrice(p, minPrice))
                .filter(p -> filterByMaxPrice(p, maxPrice))
                .filter(p -> filterByStock(p, inStock))
                .toList();
    }

    private boolean filterByBrand(Product product, String brand) {
        return brand == null || (product.getBrand() != null && product.getBrand().equalsIgnoreCase(brand));
    }

    private boolean filterByMinPrice(Product product, BigDecimal minPrice) {
        return minPrice == null || (product.getPrice() != null && product.getPrice().compareTo(minPrice) >= 0);
    }

    private boolean filterByMaxPrice(Product product, BigDecimal maxPrice) {
        return maxPrice == null || (product.getPrice() != null && product.getPrice().compareTo(maxPrice) <= 0);
    }

    private boolean filterByStock(Product product, Boolean inStock) {
        return inStock == null || (!inStock || product.getStockQuantity() > 0);
    }

    private Product convertToProduct(ProductRequest request) {
        return new Product(
                null,
                request.getName(),
                request.getSku(),
                request.getSlug(),
                request.getDescription(),
                request.getImages(),
                request.getPrice(),
                request.getDiscountPercent(),
                request.getStockQuantity(),
                request.getCategoryId(),
                request.getBrand(),
                request.getAttributes(),
                Boolean.TRUE.equals(request.getPublished()),
                request.getRelatedProducts(),
                null, // createdAt will be set automatically
                null  // updatedAt will be set automatically
        );
    }

    private ProductResponse convertToProductResponse(Product product) {
        String categoryName = getCategoryName(product.getCategoryId());
        
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getSlug(),
                product.getDescription(),
                product.getImages(),
                product.getPrice(),
                product.getDiscountPercent(),
                product.getStockQuantity(),
                product.getCategoryId(),
                categoryName,
                product.getBrand(),
                product.getAttributes(),
                product.isPublished(),
                product.getRelatedProducts(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    private String getCategoryName(String categoryId) {
        if (categoryId == null || categoryId.trim().isEmpty()) {
            return "Chưa phân loại";
        }
        
        Optional<Category> category = categoryService.getCategoryById(categoryId);
        return category.map(Category::getName).orElse("Danh mục không tồn tại");
    }
}
