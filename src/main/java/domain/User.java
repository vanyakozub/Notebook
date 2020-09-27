package domain;

import java.util.Objects;

/**
 * Configures user's data.
 */
public class User {
    /**
     * String field which define user's first name.
     */
    private String firstName;
    /**
     * String field which define user's first name.
     */
    private String lastName;
    /**
     * String field which define user's Email.
     */
    private String email;
    /**
     * String field which define user's password.
     */
    private String password;

    /**
     *
     * @return user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password password to set
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     *
     * @param firstName user's first name
     * @param lastName user's last name
     * @param email user's email
     * @param password  user's password
     */
    public User(final String firstName, final String lastName, final String email, final String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    /**
     *
     * @return user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName first name to set
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName last name to set
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return user's Email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email Email to set
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        User user = (User) o;
        if (!this.email.equals(user.email)) {
            return false;
        }
        return true;
    }
}
