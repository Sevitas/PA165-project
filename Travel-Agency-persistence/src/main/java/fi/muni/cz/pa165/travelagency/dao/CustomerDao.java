package fi.muni.cz.pa165.travelagency.dao;

import fi.muni.cz.pa165.travelagency.entity.Customer;
import fi.muni.cz.pa165.travelagency.entity.Reservation;

import java.util.List;

/**
 * Created by martin on 22.10.2017.7
 * @author Martin Sevcik <422365>
 */
public interface CustomerDao {

    /**
     * Create operation for CustomerDao
     * @param customer Customer
     */
    void create(Customer customer);

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
    void remove(Customer customer);

    /**
     * Updates Customer
     * @param customer Customer
     * @return customer
     */
    Customer update(Customer customer);

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
}
