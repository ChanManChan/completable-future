package com.u4.apiclient;

import com.u4.domain.cars.Car;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarsClientTest {
    WebClient webclient = WebClient.create("https://myfakeapi.com");
    CarsClient carsClient = new CarsClient(webclient);

    @Test
    void invokeCarsAPIWithYear() {
        List<Car> cars = carsClient.invokeCarsAPIWithYear(1950);

        assertNotNull(cars);
        assertTrue(cars.size() > 0);
        cars.forEach(Assertions::assertNotNull);
    }

    @Test
    void invokeCarsAPIUsingMultipleYears() {
        List<Integer> yearList = List.of(1950, 1960, 1970, 1980, 1990, 2000, 2001, 2002, 2003, 2004, 2005);
        List<Car> cars = carsClient.invokeCarsAPIUsingMultipleYears(yearList);

        assertNotNull(cars);
        assertTrue(cars.size() > 0);
        cars.forEach(Assertions::assertNotNull);
    }

    @Test
    void invokeCarsAPIUsingMultipleYearsAsync() {
        List<Integer> yearList = List.of(1950, 1960, 1970, 1980, 1990, 2000, 2001, 2002, 2003, 2004, 2005);
        List<Car> cars = carsClient.invokeCarsAPIUsingMultipleYearsAsync(yearList);

        assertNotNull(cars);
        assertTrue(cars.size() > 0);
        cars.forEach(Assertions::assertNotNull);
    }

    @Test
    void invokeCarsAPIUsingMultipleYearsAsyncApproach2() {
        List<Integer> yearList = List.of(1950, 1960, 1970, 1980, 1990, 2000, 2001, 2002, 2003, 2004, 2005);
        List<Car> cars = carsClient.invokeCarsAPIUsingMultipleYearsAsyncApproach2(yearList);

        assertNotNull(cars);
        assertTrue(cars.size() > 0);
        cars.forEach(Assertions::assertNotNull);
    }
}