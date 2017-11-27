package fi.muni.cz.pa165.travelagency.dto;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Customer DTO class
 * @author Martin Sevcik <422365>
 */
public class CustomerDTO {

    /**
     * Representing id
     */
    private Long id;

    /**
     * First name of customer
     */
    private String name;

    /**
     * Surname of customer
     */
    private String surname;

    /**
     * PhoneNumber of customer
     */
    private String phoneNumber;

    /**
     * IdCard number of customer
     */
    private String idCardNumber;

    /**
     * Email of customer
     */
    private String email;

    /**
     * Birth date of customer
     */
    private Date birthDate;

    /**
     * Reservations of customer
     */
    private Set<ReservationDTO> reservations;

    /**
     * Non parametric constructor
     */
    public CustomerDTO() {
        this.reservations = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Set<ReservationDTO> getReservations() {
        return reservations;
    }

    public void setReservations(Set<ReservationDTO> reservations) {
        this.reservations = reservations;
    }

    /**
     * Overridden equals method
     * @param o object
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof CustomerDTO)) return false;

        CustomerDTO that = (CustomerDTO) o;

        return this.idCardNumber != null ?
                this.idCardNumber.equals(that.getIdCardNumber()) : that.getIdCardNumber() == null;
    }

    /**
     * Overridden hash code method
     * @return hash of object
     */
    @Override
    public int hashCode() {
        return this.idCardNumber != null ? this.idCardNumber.hashCode() : 0;
    }
}
