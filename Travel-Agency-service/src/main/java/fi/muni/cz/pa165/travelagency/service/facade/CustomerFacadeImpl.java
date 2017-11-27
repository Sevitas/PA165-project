package fi.muni.cz.pa165.travelagency.service.facade;

import fi.muni.cz.pa165.travelagency.dto.CustomerDTO;
import fi.muni.cz.pa165.travelagency.dto.ReservationDTO;
import fi.muni.cz.pa165.travelagency.entity.Customer;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.facade.CustomerFacade;
import fi.muni.cz.pa165.travelagency.service.BeanMappingService;
import fi.muni.cz.pa165.travelagency.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Customer Facade impl class
 * @author Martin Sevcik <422365>
 */
@Service
@Transactional
public class CustomerFacadeImpl implements CustomerFacade {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BeanMappingService beanMappingService;

    @Override
    public void createCustomer(CustomerDTO customerDTO) {
        if (customerDTO == null){
            throw new IllegalArgumentException("customerDTO is null");
        }

        Customer customer = beanMappingService.mapTo(customerDTO, Customer.class);

        if (customer == null) {
            throw new IllegalArgumentException("customer is null");
        }

        customerService.createCustomer(customer);
    }

    @Override
    public List<CustomerDTO> findAll() {
        return beanMappingService.mapTo(customerService.findAll(), CustomerDTO.class);
    }

    @Override
    public CustomerDTO findById(Long id) {
        Customer customer = customerService.findById(id);
        return customer == null ? null : beanMappingService.mapTo(customer, CustomerDTO.class);
    }

    @Override
    public void removeCustomer(CustomerDTO customerDTO) {
        if (customerDTO == null){
            throw new IllegalArgumentException("customerDTO is null");
        }

        Customer customer = beanMappingService.mapTo(customerDTO, Customer.class);

        if (customer == null) {
            throw new IllegalArgumentException("customer is null");
        }

        customerService.removeCustomer(customer);
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        if (customerDTO == null){
            throw new IllegalArgumentException("customerDTO is null");
        }

        Customer customer = beanMappingService.mapTo(customerDTO, Customer.class);

        if (customer == null) {
            throw new IllegalArgumentException("customer is null");
        }

        customerService.updateCustomer(customer);
    }

    @Override
    public CustomerDTO findByReservation(ReservationDTO reservationDTO) {
        if (reservationDTO == null) {
            throw new IllegalArgumentException("reservationDTO is null");
        }

        Reservation reservation = beanMappingService.mapTo(reservationDTO, Reservation.class);
        if (reservation == null) {
            throw new IllegalArgumentException("reservation is null");
        }

        Customer customer = customerService.findByReservation(reservation);
        return customer == null ? null : beanMappingService.mapTo(customer, CustomerDTO.class);
    }

    @Override
    public CustomerDTO findByIdCardNumber(String idCardNumber) {
        Customer customer = customerService.findByIdCardNumber(idCardNumber);
        return customer == null ? null : beanMappingService.mapTo(customer, CustomerDTO.class);
    }

    @Override
    public CustomerDTO findByEmail(String email) {
        Customer customer = customerService.findByEmail(email);
        return customer == null ? null : beanMappingService.mapTo(customer, CustomerDTO.class);
    }

    @Override
    public BigDecimal getTotalPriceCustomersReservations(CustomerDTO customerDTO) {
        if (customerDTO == null){
            throw new IllegalArgumentException("customerDTO is null");
        }

        Customer customer = beanMappingService.mapTo(customerDTO, Customer.class);
        if (customer == null){
            throw new IllegalArgumentException("customer is null");
        }

        return customerService.getTotalPriceCustomersReservations(customer);
    }

    @Override
    public CustomerDTO changeCustomerOnReservation(CustomerDTO customerDTO, ReservationDTO reservationDTO) {
        if (customerDTO == null){
            throw new IllegalArgumentException("customerDTO is null");
        }

        if (reservationDTO == null) {
            throw new IllegalArgumentException("reservationDTO is null");
        }

        Customer customer = beanMappingService.mapTo(customerDTO, Customer.class);
        if (customer == null){
            throw new IllegalArgumentException("customer is null");
        }

        Reservation reservation = beanMappingService.mapTo(reservationDTO, Reservation.class);
        if (reservation == null) {
            throw new IllegalArgumentException("reservation is null");
        }

        Customer customerToReturn = customerService.changeCustomerOnReservation(customer, reservation);
        return customerToReturn == null ? null : beanMappingService.mapTo(customerToReturn, CustomerDTO.class);
    }
}
