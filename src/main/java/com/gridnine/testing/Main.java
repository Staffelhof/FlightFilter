package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Flight> flightList = new ArrayList<>(FlightBuilder.createFlights());

        List<Flight> resultList = FlightFilter.simpleFilter(flightList, Flights.isDepartureTimeBefore(currentTime));
        List<Flight> resultList1 = FlightFilter.simpleFilter(flightList, Flights.isArrivalBeforeDeparture());
        List<Flight> resultList2 = FlightFilter.simpleFilter(flightList, Flights.isSummaryTimeBetweenFlightsMoreThan(2));

        System.out.println("Array of Flights with Departure time before Current time");
        resultList.forEach(System.out::println);

        System.out.println("\n\nArray of Flights with segments where Departure time before Arrival time");
        resultList1.forEach(System.out::println);

        System.out.println("\n\nArray of Flights with summary time between flights is more than 2 hours");
        resultList2.forEach(System.out::println);










    }
}
