package fi.muni.cz.pa165.travelagency.service;

import fi.muni.cz.pa165.travelagency.entity.Customer;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Customer service interface
 * @author Martin Sevcik <422365>
 */
@Service
public interface CustomerService {
    /**
     * Create operation for CustomerDao
     * @param customer Customer
     */
    void createCustomer(Customer customer);

    /**
     * Finds all Customers
     * @return Customer's list
     */
    List<Customer> findAll();

    /**
     * Finds specified customer
     * @param id id of customer
     * @return Customer
     */
    Customer findById(Long id);

    /**
     * Removes Customer
     * @param customer Customer
     */
    void removeCustomer(Customer customer);

    /**
     * Updates Customer
     * @param customer Customer
     * @return customer
     */
    Customer updateCustomer(Customer customer);

    /**
     * Finds customers based on reservation
     * @param reservation Reservation
     * @return Customer
     */
    Customer findByReservation(Reservation reservation);

    /**
     * Finds Customer by id card number
     * @param idCardNumber id card number
     * @return Customer
     */
    Customer findByIdCardNumber(String idCardNumber);

    /**
     * Finds Customer by email
     * @param email email
     * @return Customer
     */
    Customer findByEmail(String email);

    /**
     * Sums all customers expenses
     * @param customer Customer
     * @return total expenses
     */
    BigDecimal getTotalPriceCustomersReservations(Customer customer);

    /**
     * Changes customer on reservation
     * @param customer Customer
     * @param reservation Reservation
     * @return Customer
     */
    Customer changeCustomerOnReservation(Customer customer, Reservation reservation);
}
