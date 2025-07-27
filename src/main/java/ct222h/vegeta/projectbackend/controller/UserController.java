package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.dto.request.AddressRequest;
import ct222h.vegeta.projectbackend.dto.request.UpdateUserRequest;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.UserResponse;
import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import ct222h.vegeta.projectbackend.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorizationService authorizationService;

    // USER endpoints - require authentication
    @GetMapping("/users/me")
    public ApiResponse<UserResponse> getMyProfile(Principal principal) {
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
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
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
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
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<User.Address> addresses = user.getAddresses();
        if (addresses == null) addresses = new ArrayList<>();

        return new ApiResponse<>(true, "Lấy địa chỉ thành công", addresses);
    }

    @PostMapping("/users/me/addresses")
    public ApiResponse<List<User.Address>> addAddress(Principal principal, @RequestBody AddressRequest addressRequest) {
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
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
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
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
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
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
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
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

    // ADMIN endpoints - require ADMIN role
    @GetMapping("/admin/users")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
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
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        if (!userRepository.existsById(userId)) {
            return new ApiResponse<>(false, "Người dùng không tồn tại", null);
        }

        userRepository.deleteById(userId);
        return new ApiResponse<>(true, "Xóa người dùng thành công", null);
    }
}
