package ct222h.vegeta.projectbackend.dto.request;

import ct222h.vegeta.projectbackend.model.User;

public class AddressRequest {
    private Boolean isDefault;
    private String recipientName;
    private String phoneNumber;
    private String street;
    private String ward;
    private String city;

    public AddressRequest() {}

    public AddressRequest(Boolean isDefault, String recipientName, String phoneNumber, String street, String ward, String city) {
        this.isDefault = isDefault;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.ward = ward;
        this.city = city;
    }

    // Convert to User.Address
    public User.Address toAddress() {
        return new User.Address(isDefault, recipientName, phoneNumber, street, ward, city);
    }

    // Getters and Setters
    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
