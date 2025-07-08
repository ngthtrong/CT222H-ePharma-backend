package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.dto.request.ProductRequest;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.ProductResponse;
import ct222h.vegeta.projectbackend.model.Category;
import ct222h.vegeta.projectbackend.model.Product;
import ct222h.vegeta.projectbackend.service.CategoryService;
import ct222h.vegeta.projectbackend.service.ProductService;
import jakarta.validation.Valid;
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

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponse> responses = products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách sản phẩm thành công", responses));
    }

    @GetMapping("/products/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(@RequestParam("q") String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        List<ProductResponse> responses = products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Tìm kiếm sản phẩm thành công", responses));
    }

    @GetMapping("/products/{slug}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySlug(@PathVariable String slug) {
        Optional<Product> product = productService.getProductBySlug(slug);
        return product
                .map(p -> ResponseEntity.ok(new ApiResponse<>(true, "Lấy sản phẩm thành công", convertToProductResponse(p))))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>(false, "Không tìm thấy sản phẩm", null)));
    }

    @GetMapping("/products/{id}/related")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getRelatedProducts(
            @PathVariable String id,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock
    ) {
        List<Product> products = productService.getRelatedProducts(id);

        List<Product> filtered = products.stream()
                .filter(p -> brand == null || (p.getBrand() != null && p.getBrand().equalsIgnoreCase(brand)))
                .filter(p -> minPrice == null || (p.getPrice() != null && p.getPrice().compareTo(minPrice) >= 0))
                .filter(p -> maxPrice == null || (p.getPrice() != null && p.getPrice().compareTo(maxPrice) <= 0))
                .filter(p -> inStock == null || (inStock ? p.getStockQuantity() > 0 : true))
                .toList();

        List<ProductResponse> responses = filtered.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách sản phẩm liên quan thành công", responses));
    }

    @PostMapping("/admin/products")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        Product product = convertToProduct(request);
        Product created = productService.createProduct(product);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Tạo sản phẩm thành công", convertToProductResponse(created)));
    }

    @PutMapping("/admin/products/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequest request) {
        Product product = convertToProduct(request);
        product.setId(id);
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật sản phẩm thành công", convertToProductResponse(updated)));
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Xóa sản phẩm thành công", null));
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
                request.getIsPublished() != null ? request.getIsPublished() : true,
                request.getRelatedProducts(),
                null,
                null
        );
    }

    private ProductResponse convertToProductResponse(Product product) {
        String categoryName = null;
        if (product.getCategoryId() != null) {
            Optional<Category> category = categoryService.getCategoryById(product.getCategoryId());
            categoryName = category.map(Category::getName).orElse(null);
        }
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
}
