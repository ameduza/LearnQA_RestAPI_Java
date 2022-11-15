package Models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonPropertyOrder({"id", "username", "email", "firstName", "lastName"})
public class UserResponseModel {
    int id;
    String username;
    String email;
    String firstName;
    String lastName;

    public UserResponseModel() {
    }

    public UserResponseModel(String id, String username, String email, String firstName, String lastName) {
        this.id = Integer.parseInt(id);
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponseModel that = (UserResponseModel) o;
        return id == that.id && username.equals(that.username) && email.equals(that.email) && firstName.equals(that.firstName) && lastName.equals(that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, firstName, lastName);
    }
}
