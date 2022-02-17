package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Utility interface with predicates for FlightFilter method
 */
interface Flights {
    /**
     * Returns preficate for filter with arrival time before departure time
     * @return Predicate for filter method
     */
    static Predicate<Flight> isArrivalBeforeDeparture() {
        return new Predicate<Flight>() {

            @Override
            public boolean test(Flight flight) {
                boolean temp = false;
                for (Segment s : flight.getSegments()) {
                    if (!temp) temp = s.getArrivalDate().isBefore(s.getDepartureDate());
                }
                return temp;
            }
        };
    }

    /**
     * Returns predicate to filter with departure time before argument time
     * @param time LocalDateTime argument
     * @return Predicate for filter method
     */
    static Predicate<Flight> isDepartureTimeBefore(LocalDateTime time) {
        return new Predicate<Flight>() {
            @Override
            public boolean test(Flight flight) {
                boolean temp = false;
                for (Segment s : flight.getSegments()) {
                    if (!temp) temp = s.getDepartureDate().isBefore(time);
                }
                return temp;
            }
        };
    }

    /**
     * Returns predicate to filter flights with time between flights more than N hours
     * @param hours  long value for hours
     * @return Predicate<Flight> for filter method
     */
    static Predicate<Flight> isSummaryTimeBetweenFlightsMoreThan(long hours) {
        return  new Predicate<Flight>() {
            public boolean test(Flight flight) {
                long diff = 0;
                LocalDateTime arrTime = null;
                for (Segment s : flight.getSegments()) {
                    if (arrTime != null) {
                        if (diff < hours*60*60*1000) {
                            diff += Duration.between(arrTime, s.getDepartureDate()).toMillis();
                        }
                    } else {
                        arrTime = s.getArrivalDate();
                    }
                }
                return diff > hours*60*60*1000;
            }
        };
    }
}
