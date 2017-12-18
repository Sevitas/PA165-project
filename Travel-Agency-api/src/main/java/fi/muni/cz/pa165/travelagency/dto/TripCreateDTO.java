package fi.muni.cz.pa165.travelagency.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @author Pavel Kotala
*/
public class TripCreateDTO {

    @NotNull
    private Date dateFrom;

    @NotNull
    private Date dateTo;

    @NotNull
    private String destination;

    @Min(0)
    private int availableSpots;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    /**
     * Creates empty TripCreateDTO
     */
    public TripCreateDTO() {
    }

    /**
     * Creates TripCreateDTO with given parameters
     * @param dateFrom start date
     * @param dateTo end date
     * @param destination destination
     * @param availableSpots available spots
     * @param name name
     * @param price price
     */
    public TripCreateDTO(Date dateFrom, Date dateTo, String destination,
                         int availableSpots, String name, BigDecimal price) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.destination = destination;
        this.availableSpots = availableSpots;
        this.name = name;
        this.price = price;
    }

    /**
     * Returns date of start of the trip.
     * @return date of start of the trip
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * Sets new date of start of the trip.
     * @param dateFrom new date of start of the trip
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * Returns date of end of the trip.
     * @return date of end of the trip
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * Sets new date of end of the trip.
     * @param dateTo new date of end of the trip
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * Returns destination of the trip.
     * @return destination of the trip
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets new destination of the trip
     * @param destination new destination of the trip
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Returns number of available spots on the trip.
     * @return number of available spots on the trip
     */
    public int getAvailableSpots() {
        return availableSpots;
    }

    /**
     * Sets new number of available spots on the trip.
     * @param availableSpots new number of available spots on the trip
     */
    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = availableSpots;
    }

    /**
     * Returns name of the trip.
     * @return name of the trip
     */
    public String getName() {
        return name;
    }

    /**
     * Sets new name of the trip.
     * @param name new name of the trip
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the price of the trip.
     * @return the price of the trip
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets new price of the trip.
     * @param price the new price of the trip
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dateTo, dateFrom);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TripCreateDTO)) {
            return false;
        }
        TripCreateDTO trip = (TripCreateDTO) object;
        return Objects.equals(this.name, trip.getName()) &&
                Objects.equals(this.dateFrom, trip.getDateFrom()) &&
                Objects.equals(this.dateTo, trip.getDateTo());
    }

    @Override
    public String toString() {
        return "TripCreateDTO{" +
                "dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", destination='" + destination + '\'' +
                ", availableSpots=" + availableSpots +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
