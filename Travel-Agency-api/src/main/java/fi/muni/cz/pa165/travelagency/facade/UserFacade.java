package fi.muni.cz.pa165.travelagency.facade;

import fi.muni.cz.pa165.travelagency.dto.UserAuthenticateDTO;
import fi.muni.cz.pa165.travelagency.dto.UserDTO;
import fi.muni.cz.pa165.travelagency.dto.ReservationDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * user Facade class
 * @author Martin Sevcik <422365>
 */
public interface UserFacade {

    /**
     * Registers new User and create hash of his password
     * @param userDTO UserDTO
     * @param password unencrypted password
     * @throws IllegalArgumentException if userDTO is null or mapped userDTO is null
     */
    void registerUser(UserDTO userDTO, String password);

    /**
     * Finds all users
     * @return UserDTO's list
     */
    List<UserDTO> findAll();

    /**
     * Finds specified user
     * @param id id of user
     * @return UserDTO
     */
    UserDTO findById(Long id);

    /**
     * Removes user
     * @param userDTO UserDTO
     * @throws IllegalArgumentException if userDTO is null or mapped userDTO is null
     */
    void removeUser(UserDTO userDTO);

    /**
     * Updates user
     * @param userDTO UserDTO
     * @throws IllegalArgumentException if userDTO is null or mapped userDTO is null
     */
    void updateUser(UserDTO userDTO);

    /**
     * Finds users based on reservation
     * @param reservationDTO ReservationDTO
     * @throws IllegalArgumentException if reservationDTO is null or mapped reservationDTO is null
     * @return UserDTO
     */
    UserDTO findByReservation(ReservationDTO reservationDTO);

    /**
     * Finds user by id card number
     * @param idCardNumber id card number
     * @return UserDTO
     */
    UserDTO findByIdCardNumber(String idCardNumber);

    /**
     * Finds user by email
     * @param email email
     * @return UserDTO
     */
    UserDTO findByEmail(String email);

    /**
     * Sums all users expenses
     * @param userDTO UserDTO
     * @throws IllegalArgumentException if userDTO is null or mapped userDTO is null
     * @return total expenses
     */
    BigDecimal getTotalPriceUsersReservations(UserDTO userDTO);

    /**
     * Changes user on reservation
     * @param userDTO UserDTO
     * @param reservationDTO ReservationDTO
     * @throws IllegalArgumentException if userDTO is null or mapped userDTO is null
     * @return user
     */
    UserDTO changeUserOnReservation(UserDTO userDTO, ReservationDTO reservationDTO);

    /**
     * Tries to authenticate user
     * @param user user to authenticate
     * @throws IllegalArgumentException if user is null or mapped userDTO is null
     * @return User if authenticated, null otherwise
     */
    UserDTO authenticate(UserAuthenticateDTO user);

    /**
     * Checks whether user is an admin
     * @param userDTO user
     * @throws IllegalArgumentException if userDTO is null or mapped userDTO is null
     * @return true if user is admin, false otherwise
     */
    Boolean isAdmin(UserDTO userDTO);
}
