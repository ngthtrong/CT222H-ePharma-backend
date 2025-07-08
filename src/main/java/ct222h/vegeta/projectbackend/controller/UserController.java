package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.dto.request.AddressRequest;
import ct222h.vegeta.projectbackend.dto.request.CartRequest;
import ct222h.vegeta.projectbackend.dto.response.UserResponse;
import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMyProfile(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

    @PutMapping("/me")
    public ApiResponse<UserResponse> updateMyProfile(Principal principal, @RequestBody UpdateUserRequest request) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update user fields
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddresses() != null) {
            user.setAddresses(request.getAddresses());
        }
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

    @GetMapping("/me/cart")
    public ApiResponse<User.Cart> getMyCart(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ApiResponse<>(true, "Lấy giỏ hàng thành công", user.getCart());
    }

    @PutMapping("/me/cart")
    public ApiResponse<User.Cart> updateMyCart(Principal principal, @RequestBody CartRequest cartRequest) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User.Cart cart = cartRequest.toCart();
        user.setCart(cart);
        user.setUpdatedAt(new java.util.Date());
        user = userRepository.save(user);

        return new ApiResponse<>(true, "Cập nhật giỏ hàng thành công", user.getCart());
    }

    @PostMapping("/me/addresses")
    public ApiResponse<List<User.Address>> addAddress(Principal principal, @RequestBody AddressRequest addressRequest) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User.Address> addresses = user.getAddresses();
        if (addresses == null) {
            addresses = new java.util.ArrayList<>();
        }

        // If this is set as default, unset all others
        if (addressRequest.getIsDefault() != null && addressRequest.getIsDefault()) {
            addresses.forEach(addr -> addr.setIsDefault(false));
        }

        addresses.add(addressRequest.toAddress());
        user.setAddresses(addresses);
        user.setUpdatedAt(new java.util.Date());
        user = userRepository.save(user);

        return new ApiResponse<>(true, "Thêm địa chỉ thành công", user.getAddresses());
    }

    @DeleteMapping("/me/addresses/{index}")
    public ApiResponse<List<User.Address>> deleteAddress(Principal principal, @PathVariable int index) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User.Address> addresses = user.getAddresses();
        if (addresses != null && index >= 0 && index < addresses.size()) {
            addresses.remove(index);
            user.setAddresses(addresses);
            user.setUpdatedAt(new java.util.Date());
            user = userRepository.save(user);
        }

        return new ApiResponse<>(true, "Xóa địa chỉ thành công", user.getAddresses());
    }

    // DTO class for update request
    public static class UpdateUserRequest {
        private String fullName;
        private String phoneNumber;
        private List<User.Address> addresses;

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

        public List<User.Address> getAddresses() { return addresses; }
        public void setAddresses(List<User.Address> addresses) { this.addresses = addresses; }
    }
}