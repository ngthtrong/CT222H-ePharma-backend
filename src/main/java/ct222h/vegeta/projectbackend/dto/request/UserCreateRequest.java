package ct222h.vegeta.projectbackend.dto.request;

public class UserCreateRequest {
    private String name;
    private String email;
    private String password;
    private Integer age;
    private String phone;

    public UserCreateRequest() {}

    public UserCreateRequest (String name, String email, int age, String phone){
        this.name = name;
        this.email = email;
        this.age = age;
        this.phone = phone;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
