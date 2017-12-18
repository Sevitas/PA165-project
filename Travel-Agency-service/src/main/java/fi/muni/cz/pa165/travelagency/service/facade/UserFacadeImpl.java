package fi.muni.cz.pa165.travelagency.service.facade;

import fi.muni.cz.pa165.travelagency.dto.UserAuthenticateDTO;
import fi.muni.cz.pa165.travelagency.dto.UserDTO;
import fi.muni.cz.pa165.travelagency.dto.ReservationDTO;
import fi.muni.cz.pa165.travelagency.entity.User;
import fi.muni.cz.pa165.travelagency.entity.Reservation;
import fi.muni.cz.pa165.travelagency.facade.UserFacade;
import fi.muni.cz.pa165.travelagency.service.BeanMappingService;
import fi.muni.cz.pa165.travelagency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * User Facade impl class
 * @author Martin Sevcik <422365>
 */
@Service
@Transactional
public class UserFacadeImpl implements UserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private BeanMappingService beanMappingService;

    @Override
    public void registerUser(UserDTO userDTO, String password) {
        if (userDTO == null){
            throw new IllegalArgumentException("userDTO is null");
        }

        User user = beanMappingService.mapTo(userDTO, User.class);

        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }

        userService.registerUser(user, password);
    }

    @Override
    public List<UserDTO> findAll() {
        return beanMappingService.mapTo(userService.findAll(), UserDTO.class);
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userService.findById(id);
        return user == null ? null : beanMappingService.mapTo(user, UserDTO.class);
    }

    @Override
    public void removeUser(UserDTO userDTO) {
        if (userDTO == null){
            throw new IllegalArgumentException("userDTO is null");
        }

        User user = beanMappingService.mapTo(userDTO, User.class);

        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }

        userService.removeUser(user);
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        if (userDTO == null){
            throw new IllegalArgumentException("userDTO is null");
        }

        User user = beanMappingService.mapTo(userDTO, User.class);

        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }

        userService.updateUser(user);
    }

    @Override
    public UserDTO findByReservation(ReservationDTO reservationDTO) {
        if (reservationDTO == null) {
            throw new IllegalArgumentException("reservationDTO is null");
        }

        Reservation reservation = beanMappingService.mapTo(reservationDTO, Reservation.class);
        if (reservation == null) {
            throw new IllegalArgumentException("reservation is null");
        }

        User user = userService.findByReservation(reservation);
        return user == null ? null : beanMappingService.mapTo(user, UserDTO.class);
    }

    @Override
    public UserDTO findByIdCardNumber(String idCardNumber) {
        User user = userService.findByIdCardNumber(idCardNumber);
        return user == null ? null : beanMappingService.mapTo(user, UserDTO.class);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userService.findByEmail(email);
        return user == null ? null : beanMappingService.mapTo(user, UserDTO.class);
    }

    @Override
    public BigDecimal getTotalPriceUsersReservations(UserDTO userDTO) {
        if (userDTO == null){
            throw new IllegalArgumentException("userDTO is null");
        }

        User user = beanMappingService.mapTo(userDTO, User.class);
        if (user == null){
            throw new IllegalArgumentException("user is null");
        }

        return userService.getTotalPriceUsersReservations(user);
    }

    @Override
    public UserDTO changeUserOnReservation(UserDTO userDTO, ReservationDTO reservationDTO) {
        if (userDTO == null){
            throw new IllegalArgumentException("userDTO is null");
        }

        if (reservationDTO == null) {
            throw new IllegalArgumentException("reservationDTO is null");
        }

        User user = beanMappingService.mapTo(userDTO, User.class);
        if (user == null){
            throw new IllegalArgumentException("user is null");
        }

        Reservation reservation = beanMappingService.mapTo(reservationDTO, Reservation.class);
        if (reservation == null) {
            throw new IllegalArgumentException("reservation is null");
        }

        User userToReturn = userService.changeUserOnReservation(user, reservation);
        return userToReturn == null ? null : beanMappingService.mapTo(userToReturn, UserDTO.class);
    }

    @Override
    public UserDTO authenticate(UserAuthenticateDTO userAuth) {
        if (userAuth == null){
            throw new IllegalArgumentException("user is null");
        }

        User user = userService.findByEmail(userAuth.getEmail());
        if (user == null) {
            return null;
        }

        if (!userService.authenticate(user, userAuth.getPasswordHash())) {
            return null;
        }

        return beanMappingService.mapTo(user, UserDTO.class);
    }

    @Override
    public Boolean isAdmin(UserDTO userDTO) {
        if (userDTO == null){
            throw new IllegalArgumentException("userDTO is null");
        }

        User user = beanMappingService.mapTo(userDTO, User.class);
        if (user == null){
            throw new IllegalArgumentException("user is null");
        }

        return userService.isAdmin(user);
    }
}
