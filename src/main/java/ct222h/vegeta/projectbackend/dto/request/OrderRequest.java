package ct222h.vegeta.projectbackend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotBlank(message = "User ID không được để trống")
    private String userId;

    // Có thể để null để sử dụng địa chỉ mặc định, hoặc chọn theo addressId, hoặc nhập mới
    private String selectedAddressId; // ID của địa chỉ đã lưu trong user
    
    @Valid
    private ShippingAddressRequest customShippingAddress; // Địa chỉ mới (tùy chọn)

    @NotBlank(message = "Phương thức thanh toán không được để trống")
    @Pattern(regexp = "COD|MOMO|BANK_TRANSFER", message = "Phương thức thanh toán không hợp lệ")
    private String paymentMethod;

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private String notes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingAddressRequest {
        @NotBlank(message = "Tên người nhận không được để trống")
        @Size(max = 100, message = "Tên người nhận không được vượt quá 100 ký tự")
        private String recipientName;

        @NotBlank(message = "Số điện thoại không được để trống")
        @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại không hợp lệ")
        private String phoneNumber;

        @NotBlank(message = "Địa chỉ đường không được để trống")
        @Size(max = 200, message = "Địa chỉ đường không được vượt quá 200 ký tự")
        private String street;

        @NotBlank(message = "Phường/Xã không được để trống")
        @Size(max = 100, message = "Phường/Xã không được vượt quá 100 ký tự")
        private String ward;

        @NotBlank(message = "Thành phố không được để trống")
        @Size(max = 100, message = "Thành phố không được vượt quá 100 ký tự")
        private String city;
        
        private Boolean saveAsNewAddress = false; // Có lưu làm địa chỉ mới không
        private Boolean setAsDefault = false; // Có đặt làm địa chỉ mặc định không
    }
}
