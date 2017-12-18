package fi.muni.cz.pa165.travelagency.dto;

/**
 * Representing user's dto object for authentication
 * @author Martin Sevcik <422365>
 */
public class UserAuthenticateDTO {

    /**
     * Representing email
     */
    private String email;

    /**
     * Representing hash of password
     */
    private String passwordHash;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
