package fi.muni.cz.pa165.travelagency.facade;

import fi.muni.cz.pa165.travelagency.dto.CustomerDTO;
import fi.muni.cz.pa165.travelagency.dto.ReservationDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Customer Facade class
 * @author Martin Sevcik <422365>
 */
public interface CustomerFacade {

    /**
     * Create operation for CustomerDao
     * @param customerDTO CustomerDTO
     * @throws IllegalArgumentException if customerDTO is null or mapped customerDTO is null
     */
    void createCustomer(CustomerDTO customerDTO);

    /**
     * Finds all Customers
     * @return CustomerDTO's list
     */
    List<CustomerDTO> findAll();

    /**
     * Finds specified customer
     * @param id id of customer
     * @return CustomerDTO
     */
    CustomerDTO findById(Long id);

    /**
     * Removes Customer
     * @param customerDTO CustomerDTO
     * @throws IllegalArgumentException if customerDTO is null or mapped customerDTO is null
     */
    void removeCustomer(CustomerDTO customerDTO);

    /**
     * Updates Customer
     * @param customerDTO CustomerDTO
     * @throws IllegalArgumentException if customerDTO is null or mapped customerDTO is null
     */
    void updateCustomer(CustomerDTO customerDTO);

    /**
     * Finds customers based on reservation
     * @param reservationDTO ReservationDTO
     * @throws IllegalArgumentException if reservationDTO is null or mapped reservationDTO is null
     * @return CustomerDTO
     */
    CustomerDTO findByReservation(ReservationDTO reservationDTO);

    /**
     * Finds Customer by id card number
     * @param idCardNumber id card number
     * @return CustomerDTO
     */
    CustomerDTO findByIdCardNumber(String idCardNumber);

    /**
     * Finds Customer by email
     * @param email email
     * @return CustomerDTO
     */
    CustomerDTO findByEmail(String email);

    /**
     * Sums all customers expenses
     * @param customerDTO CustomerDTO
     * @throws IllegalArgumentException if customerDTO is null or mapped customerDTO is null
     * @return total expenses
     */
    BigDecimal getTotalPriceCustomersReservations(CustomerDTO customerDTO);

    /**
     * Changes customer on reservation
     * @param customerDTO CustomerDTO
     * @param reservationDTO ReservationDTO
     * @throws IllegalArgumentException if customerDTO is null or mapped customerDTO is null
     * @return Customer
     */
    CustomerDTO changeCustomerOnReservation(CustomerDTO customerDTO, ReservationDTO reservationDTO);
}
