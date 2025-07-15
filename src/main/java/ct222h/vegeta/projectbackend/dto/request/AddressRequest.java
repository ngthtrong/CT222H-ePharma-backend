package ct222h.vegeta.projectbackend.dto.request;

import ct222h.vegeta.projectbackend.model.User;

public class AddressRequest {
    private Boolean isDefault;
    private String recipientName;
    private String phoneNumber;
    private String street;
    private String ward;
    private String city;

    // Getter và Setter chuẩn cho isDefault
    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    // Các getter setter còn lại giữ nguyên
    public String getRecipientName() {
        return recipientName;
    }
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getWard() {
        return ward;
    }
    public void setWard(String ward) {
        this.ward = ward;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public User.Address toAddress() {
        User.Address addr = new User.Address();
        addr.setIsDefault(this.isDefault != null && this.isDefault);
        addr.setRecipientName(this.recipientName);
        addr.setPhoneNumber(this.phoneNumber);
        addr.setStreet(this.street);
        addr.setWard(this.ward);
        addr.setCity(this.city);
        // TODO: set id nếu dùng id cho Address
        return addr;
    }
}
