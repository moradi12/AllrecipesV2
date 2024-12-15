package Allrecipes.Recipesdemo.DTOs;

public class CustomerDto {
    private Long id;
    private String username;
    private String email;

    public CustomerDto() {
        this.password = password;
    }

    private String password;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
