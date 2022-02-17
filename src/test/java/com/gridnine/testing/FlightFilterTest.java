package com.gridnine.testing;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class FlightFilterTest {
    static List<Flight> list;

    @BeforeAll
    static void initializeList() {
        list = new ArrayList<>(FlightBuilder.createFlights());

    }
    @Test
    void FilterDepartureBeforeCurrentTimeShouldReturnCorrectElement() {
        Flight correctValue = list.get(2);
        List<Flight> temp = FlightFilter.simpleFilter(list,Flights.isDepartureTimeBefore(LocalDateTime.now()));
        Flight filteredValue = temp.get(0);
        Assertions.assertEquals(correctValue,filteredValue);
    }
    @Test
    void FilterDepartureBeforeArrivalShouldReturnCorrectValue() {
        Flight correctValue = list.get(3);
        List<Flight> temp = FlightFilter.simpleFilter(list, Flights.isArrivalBeforeDeparture());
        Flight filteredValue = temp.get(0);
        Assertions.assertEquals(correctValue,filteredValue);
    }

    @Test
    void FilterTimeBetweenFlightsShouldReturnCorrectValues() {
        List<Segment> correctValues = new ArrayList<>(list.get(4).getSegments());
        correctValues.addAll(list.get(5).getSegments());

        List<Flight> temp = FlightFilter.simpleFilter(list, Flights.isSummaryTimeBetweenFlightsMoreThan(2));
        List<Segment> filteredValues = temp.stream()
                .flatMap(x -> x.getSegments().stream())
                .collect(Collectors.toList());


        Assertions.assertTrue(filteredValues.size() == correctValues.size() && filteredValues.containsAll(correctValues));
    }

    @Test
    void FilterWithAnyOfMultiplePredicatesShouldReturnCorrectValues() {
        List<Segment> correctValues = list.stream().skip(2)
                .flatMap(f -> f.getSegments().stream())
                .collect(Collectors.toList());
        List<Predicate<Flight>> predicates = new ArrayList<>();
        predicates.add(Flights.isDepartureTimeBefore(LocalDateTime.now()));
        predicates.add(Flights.isSummaryTimeBetweenFlightsMoreThan(2));
        predicates.add(Flights.isArrivalBeforeDeparture());

        List<Flight> temp = FlightFilter.simpleFilter(list,false, predicates.get(0), predicates.get(1), predicates.get(2));
        List<Segment> filteredValues = temp.stream()
                .flatMap(f -> f.getSegments().stream())
                .collect(Collectors.toList());
        System.out.println(correctValues);
        System.out.println(filteredValues);
        Assertions.assertTrue(filteredValues.size() == correctValues.size() && filteredValues.containsAll(correctValues));

    }
    @Test
    void FilterWithAllMultiplePredicatesShouldReturnZeroFlights() {
        List<Flight> temp = FlightFilter.simpleFilter(list,true,Flights.isDepartureTimeBefore(LocalDateTime.now()), Flights.isArrivalBeforeDeparture(), Flights.isSummaryTimeBetweenFlightsMoreThan(2));
        Assertions.assertEquals(0, temp.size());
    }
}
