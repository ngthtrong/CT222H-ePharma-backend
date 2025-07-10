package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.dto.request.AddressRequest;
import ct222h.vegeta.projectbackend.dto.request.CartRequest;
import ct222h.vegeta.projectbackend.dto.request.UpdateUserRequest;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.UserResponse;
import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users/me")
    public ApiResponse<UserResponse> getMyProfile(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getAuthProvider(),
                user.getAddresses(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
        return new ApiResponse<>(true, "Lấy thông tin thành công", response);
    }

    @PutMapping("/users/me")
    public ApiResponse<UserResponse> updateMyProfile(Principal principal, @RequestBody UpdateUserRequest request) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());

        user.setUpdatedAt(new java.util.Date());
        user = userRepository.save(user);

        UserResponse response = new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getAuthProvider(),
                user.getAddresses(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
        return new ApiResponse<>(true, "Cập nhật thông tin thành công", response);
    }

    @GetMapping("/users/me/addresses")
    public ApiResponse<List<User.Address>> getMyAddresses(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<User.Address> addresses = user.getAddresses();
        if (addresses == null) addresses = new ArrayList<>();

        return new ApiResponse<>(true, "Lấy địa chỉ thành công", addresses);
    }

    @PostMapping("/users/me/addresses")
    public ApiResponse<List<User.Address>> addAddress(Principal principal, @RequestBody AddressRequest addressRequest) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<User.Address> addresses = user.getAddresses();
        if (addresses == null) addresses = new ArrayList<>();

        if (Boolean.TRUE.equals(addressRequest.getIsDefault())) {
            addresses.forEach(addr -> addr.setIsDefault(false));
        }

        addresses.add(addressRequest.toAddress());
        user.setAddresses(addresses);
        user.setUpdatedAt(new java.util.Date());
        user = userRepository.save(user);

        return new ApiResponse<>(true, "Thêm địa chỉ thành công", user.getAddresses());
    }

    @PutMapping("/users/me/addresses/{addressId}")
    public ApiResponse<List<User.Address>> updateAddress(Principal principal, @PathVariable String addressId, @RequestBody AddressRequest addressRequest) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<User.Address> addresses = user.getAddresses();
        if (addresses == null) addresses = new ArrayList<>();

        boolean found = false;
        for (User.Address addr : addresses) {
            if (addressId.equals(addr.getId())) {
                if (addressRequest.getIsDefault() != null) addr.setIsDefault(addressRequest.getIsDefault());
                if (addressRequest.getRecipientName() != null) addr.setRecipientName(addressRequest.getRecipientName());
                if (addressRequest.getPhoneNumber() != null) addr.setPhoneNumber(addressRequest.getPhoneNumber());
                if (addressRequest.getStreet() != null) addr.setStreet(addressRequest.getStreet());
                if (addressRequest.getWard() != null) addr.setWard(addressRequest.getWard());
                if (addressRequest.getCity() != null) addr.setCity(addressRequest.getCity());
                found = true;
                break;
            }
        }

        if (!found) return new ApiResponse<>(false, "Địa chỉ không tồn tại", null);

        if (Boolean.TRUE.equals(addressRequest.getIsDefault())) {
            for (User.Address addr : addresses) {
                if (!addressId.equals(addr.getId())) {
                    addr.setIsDefault(false);
                }
            }
        }

        user.setAddresses(addresses);
        user.setUpdatedAt(new java.util.Date());
        user = userRepository.save(user);

        return new ApiResponse<>(true, "Cập nhật địa chỉ thành công", addresses);
    }

    @PatchMapping("/users/me/addresses/{addressId}/default")
    public ApiResponse<List<User.Address>> setDefaultAddress(Principal principal, @PathVariable String addressId) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<User.Address> addresses = user.getAddresses();
        if (addresses == null) addresses = new ArrayList<>();

        boolean found = false;
        for (User.Address addr : addresses) {
            if (addressId.equals(addr.getId())) {
                addr.setIsDefault(true);
                found = true;
            } else {
                addr.setIsDefault(false);
            }
        }

        if (!found) return new ApiResponse<>(false, "Địa chỉ không tồn tại", null);

        user.setAddresses(addresses);
        user.setUpdatedAt(new java.util.Date());
        user = userRepository.save(user);

        return new ApiResponse<>(true, "Đặt địa chỉ mặc định thành công", addresses);
    }

    @DeleteMapping("/users/me/addresses/{addressId}")
    public ApiResponse<List<User.Address>> deleteAddress(Principal principal, @PathVariable String addressId) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<User.Address> addresses = user.getAddresses();
        if (addresses == null) addresses = new ArrayList<>();

        boolean removed = addresses.removeIf(addr -> addressId.equals(addr.getId()));

        if (!removed) return new ApiResponse<>(false, "Địa chỉ không tồn tại", null);

        user.setAddresses(addresses);
        user.setUpdatedAt(new java.util.Date());
        user = userRepository.save(user);

        return new ApiResponse<>(true, "Xóa địa chỉ thành công", addresses);
    }

    @GetMapping("/cart")
    public ApiResponse<User.Cart> getCart(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        User.Cart cart = user.getCart();
        if (cart == null) cart = new User.Cart(new ArrayList<>(), new java.util.Date());

        return new ApiResponse<>(true, "Lấy giỏ hàng thành công", cart);
    }

    @PostMapping("/cart/items")
    public ApiResponse<User.Cart> addCartItem(Principal principal, @RequestBody CartRequest.CartItemRequest itemRequest) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        User.Cart cart = user.getCart();
        if (cart == null) cart = new User.Cart(new ArrayList<>(), new java.util.Date());

        List<User.CartItem> items = cart.getItems();
        if (items == null) items = new ArrayList<>();

        Optional<User.CartItem> existingItem = items.stream().filter(i -> i.getProductId().equals(itemRequest.getProductId())).findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + itemRequest.getQuantity());
        } else {
            items.add(new User.CartItem(itemRequest.getProductId(), itemRequest.getQuantity()));
        }

        cart.setItems(items);
        cart.setUpdatedAt(new java.util.Date());
        user.setCart(cart);
        user.setUpdatedAt(new java.util.Date());

        userRepository.save(user);

        return new ApiResponse<>(true, "Thêm sản phẩm vào giỏ hàng thành công", cart);
    }

    @PutMapping("/cart/items/{productId}")
    public ApiResponse<User.Cart> updateCartItem(Principal principal, @PathVariable String productId, @RequestBody CartRequest.CartItemQuantityRequest quantityRequest) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        User.Cart cart = user.getCart();
        if (cart == null) return new ApiResponse<>(false, "Giỏ hàng trống", null);

        List<User.CartItem> items = cart.getItems();
        if (items == null) return new ApiResponse<>(false, "Giỏ hàng trống", null);

        boolean found = false;
        for (User.CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantityRequest.getQuantity());
                found = true;
                break;
            }
        }

        if (!found) return new ApiResponse<>(false, "Sản phẩm không tồn tại trong giỏ hàng", null);

        cart.setItems(items);
        cart.setUpdatedAt(new java.util.Date());
        user.setCart(cart);
        user.setUpdatedAt(new java.util.Date());

        userRepository.save(user);

        return new ApiResponse<>(true, "Cập nhật số lượng sản phẩm thành công", cart);
    }

    @DeleteMapping("/cart/items/{productId}")
    public ApiResponse<User.Cart> deleteCartItem(Principal principal, @PathVariable String productId) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        User.Cart cart = user.getCart();
        if (cart == null) return new ApiResponse<>(false, "Giỏ hàng trống", null);

        List<User.CartItem> items = cart.getItems();
        if (items == null) return new ApiResponse<>(false, "Giỏ hàng trống", null);

        boolean removed = items.removeIf(item -> item.getProductId().equals(productId));

        if (!removed) return new ApiResponse<>(false, "Sản phẩm không tồn tại trong giỏ hàng", null);

        cart.setItems(items);
        cart.setUpdatedAt(new java.util.Date());
        user.setCart(cart);
        user.setUpdatedAt(new java.util.Date());

        userRepository.save(user);

        return new ApiResponse<>(true, "Xóa sản phẩm khỏi giỏ hàng thành công", cart);
    }

    @DeleteMapping("/cart")
    public ApiResponse<User.Cart> clearCart(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        user.setCart(null);
        user.setUpdatedAt(new java.util.Date());

        userRepository.save(user);

        return new ApiResponse<>(true, "Xóa toàn bộ giỏ hàng thành công", null);
    }

    @GetMapping("/admin/users")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponse> responses = new ArrayList<>();
        for (User user : users) {
            responses.add(new UserResponse(
                    user.getId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getRole(),
                    user.getAuthProvider(),
                    user.getAddresses(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            ));
        }

        return new ApiResponse<>(true, "Lấy danh sách người dùng thành công", responses);
    }

    @DeleteMapping("/admin/users/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable String userId) {
        if (!userRepository.existsById(userId)) {
            return new ApiResponse<>(false, "Người dùng không tồn tại", null);
        }

        userRepository.deleteById(userId);
        return new ApiResponse<>(true, "Xóa người dùng thành công", null);
    }
}
