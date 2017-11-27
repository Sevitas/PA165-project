package fi.muni.cz.pa165.travelagency.service.service;

import fi.muni.cz.pa165.travelagency.dto.CustomerDTO;
import fi.muni.cz.pa165.travelagency.entity.Customer;
import fi.muni.cz.pa165.travelagency.service.BeanMappingService;
import fi.muni.cz.pa165.travelagency.service.config.ServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Martin Sevcik
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class BeanMappingServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private BeanMappingService beanMappingService;

    private Customer customer1, customer2;
    private CustomerDTO customerDTO1, customerDTO2;
    private List<Customer> customerList;
    private List<CustomerDTO> customerDTOList;

    @BeforeMethod
    public void setup() {
        Calendar cal = Calendar.getInstance();

        customer1 = new Customer();
        customer1.setEmail("email1@email.com");
        customer1.setIdCardNumber("idCardNumber1");
        customer1.setName("Name1");
        customer1.setSurname("Surname1");
        customer1.setPhoneNumber("78945561231");
        cal.set(2017, 28, 10);
        customer1.setBirthDate(cal.getTime());

        customer2 = new Customer();
        customer2.setEmail("email2@email.com");
        customer2.setIdCardNumber("idCardNumber2");
        customer2.setName("Name2");
        customer2.setSurname("Surname2");
        customer2.setPhoneNumber("78945561232");
        cal.set(2017, 29, 10);
        customer2.setBirthDate(cal.getTime());

        customerDTO1 = new CustomerDTO();
        customerDTO1.setEmail("email1@email.com");
        customerDTO1.setIdCardNumber("idCardNumber1");
        customerDTO1.setName("Name1");
        customerDTO1.setSurname("Surname1");
        customerDTO1.setPhoneNumber("78945561231");
        cal.set(2017, 28, 10);
        customerDTO1.setBirthDate(cal.getTime());

        customerDTO2 = new CustomerDTO();
        customerDTO2.setEmail("email2@email.com");
        customerDTO2.setIdCardNumber("idCardNumber2");
        customerDTO2.setName("Name2");
        customerDTO2.setSurname("Surname2");
        customerDTO2.setPhoneNumber("78945561232");
        cal.set(2017, 29, 10);
        customerDTO2.setBirthDate(cal.getTime());

        customerList = new ArrayList<>();
        customerList.add(customer1);
        customerList.add(customer2);

        customerDTOList = new ArrayList<>();
        customerDTOList.add(customerDTO1);
        customerDTOList.add(customerDTO2);
    }

    @Test
    public void testMapping2CustomerEntityList() {
        List<Customer> customerMappedList = beanMappingService.mapTo(customerDTOList, Customer.class);
        customerEqualsTest(customerMappedList.get(0), customer1);
        customerEqualsTest(customerMappedList.get(1), customer2);
    }

    @Test
    public void testMapping2CustomerDTOEntityList() {
        List<CustomerDTO> customerMappedList = beanMappingService.mapTo(customerList, CustomerDTO.class);
        customerEqualsDTOTest(customerMappedList.get(0), customerDTO1);
        customerEqualsDTOTest(customerMappedList.get(1), customerDTO2);
    }

    @Test
    public void testMapping2CustomerEntity() {
        Customer found = beanMappingService.mapTo(customerDTO1, Customer.class);
        customerEqualsTest(found, customer1);
    }

    @Test
    public void testMapping2CustomerDTOEntity() {
        CustomerDTO foundDTO = beanMappingService.mapTo(customer1, CustomerDTO.class);
        customerEqualsDTOTest(foundDTO, customerDTO1);
    }

    public void customerEqualsTest(Customer customerNew, Customer customerExpected) {
        Assert.assertEquals(customerNew.getIdCardNumber(), customerExpected.getIdCardNumber(),"IdCardNumber Equality");
        Assert.assertEquals(customerNew.getSurname(), customerExpected.getSurname(), "Surname Equality");
        Assert.assertEquals(customerNew.getName(), customerExpected.getName(), "Name Equality");
        Assert.assertEquals(customerNew.getReservations(), customerExpected.getReservations(), "Reservations Equality");
        Assert.assertEquals(customerNew.getPhoneNumber(), customerExpected.getPhoneNumber(), "PhoneNumber Equality");
        Assert.assertEquals(customerNew.getBirthDate(), customerExpected.getBirthDate(), "BirthDate Equality");
        Assert.assertEquals(customerNew.getEmail(), customerExpected.getEmail(), "Email Equality");
    }

    public void customerEqualsDTOTest(CustomerDTO customerDTONew, CustomerDTO customerDTOExpected) {
        Assert.assertEquals(customerDTONew.getIdCardNumber(), customerDTOExpected.getIdCardNumber(),"IdCardNumber Equality");
        Assert.assertEquals(customerDTONew.getSurname(), customerDTOExpected.getSurname(), "Surname Equality");
        Assert.assertEquals(customerDTONew.getName(), customerDTOExpected.getName(), "Name Equality");
        Assert.assertEquals(customerDTONew.getReservations(), customerDTOExpected.getReservations(), "Reservations Equality");
        Assert.assertEquals(customerDTONew.getPhoneNumber(), customerDTOExpected.getPhoneNumber(), "PhoneNumber Equality");
        Assert.assertEquals(customerDTONew.getBirthDate(), customerDTOExpected.getBirthDate(), "BirthDate Equality");
        Assert.assertEquals(customerDTONew.getEmail(), customerDTOExpected.getEmail(), "Email Equality");
    }
}
