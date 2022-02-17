package com.gridnine.testing;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
* Utility class contains methods to filter FLights by one or more Predicates
 */
class FlightFilter {
    /**
     * Returns list based on first argument filtered by second argument
     * @param list List of flights to be filtered
     * @param flightFilter predicate to filter with
     * @return list of filtered com.gridnine.testing.Flights
     */
    public static List<Flight> simpleFilter(List<Flight> list, Predicate<Flight> flightFilter) {
        List<Flight> result;
        //if list of values is big - use parallelStream to make better performance
        if (list.size() > 10000000) {
            result = list.parallelStream()
                    .filter(Flights.isArrivalBeforeDeparture())
                    .collect(Collectors.toList());
        } else {
            result = list.stream()
                    .filter(flightFilter)
                    .collect(Collectors.toList());
        }

        return result;
    }

    /**
     * Returns List based on first arguments filtered by multiply arguments with option of matching all filters or just any of them
     * @param list  arguments to be filtered
     * @param andOr choosing if filtered output matches all filters or any of them
     * @param flightFilters  varargs of Predicates
     * @return list of filtered com.gridnine.testing.Flights
     */
    @SafeVarargs
    public static List<Flight> simpleFilter(List<Flight> list, boolean andOr, Predicate<Flight>... flightFilters) {
        //if andOr true - filtered values will have to match all the filters
        List<Flight> result;
        if (andOr) {
            if (list.size() > 1000000) {
                result = list.parallelStream()
                        .filter(Arrays.stream(flightFilters).reduce(x -> andOr, Predicate::and))
                        .collect(Collectors.toList());
            } else {
                result = list.stream()
                        .filter(Arrays.stream(flightFilters)
                                .reduce(x -> andOr, Predicate::and))
                        .collect(Collectors.toList());
            }
        } else {

            if (list.size() > 1000000) {
                result = list.parallelStream()
                        .filter(Arrays.stream(flightFilters).reduce(x -> andOr, Predicate::or))
                        .collect(Collectors.toList());
            } else {
                result = list.stream()
                        .filter(Arrays.stream(flightFilters).reduce(x -> andOr, Predicate::or))
                        .collect(Collectors.toList());
            }
        }

        return result;

    }
}
