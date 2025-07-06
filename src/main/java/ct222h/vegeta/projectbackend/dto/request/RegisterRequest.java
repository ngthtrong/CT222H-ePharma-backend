package ct222h.vegeta.projectbackend.dto.request;

import ct222h.vegeta.projectbackend.model.User;
import java.util.List;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private List<User.Address> addresses;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public List<User.Address> getAddresses() { return addresses; }
    public void setAddresses(List<User.Address> addresses) { this.addresses = addresses; }
}
