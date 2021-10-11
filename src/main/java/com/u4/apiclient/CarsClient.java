package com.u4.apiclient;

import com.u4.domain.cars.Car;
import com.u4.domain.cars.CarResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.u4.util.CommonUtil.*;
import static com.u4.util.LoggerUtil.log;

public class CarsClient {

    private final WebClient webClient;

    public CarsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Car> invokeCarsAPIWithYear(int year) {
        String uri = UriComponentsBuilder
                .fromUriString("/api/cars/year/{year}")
                .queryParam("q", "lt")
                .buildAndExpand(year)
                .toUriString();
        log("outgoing uri: " + uri);
        return Objects.requireNonNull(webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(CarResponseBody.class)
                        .block())
                .getCars();
    }

    public List<Car> invokeCarsAPIUsingMultipleYears(List<Integer> years) {
        stopWatchReset();
        startTimer();
        List<Car> cars = years.stream()
                .map(this::invokeCarsAPIWithYear) // blocking call
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        timeTaken();
        return cars;
    }

    public List<Car> invokeCarsAPIUsingMultipleYearsAsync(List<Integer> years) {
        stopWatchReset();
        startTimer();
        List<CompletableFuture<List<Car>>> cars = years.stream()
                .map(year -> CompletableFuture.supplyAsync(() -> invokeCarsAPIWithYear(year)))
                .collect(Collectors.toList()); // list of completable futures, and when each completable future is .join() we get list of cars in its position
        timeTaken();
        return cars.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<Car> invokeCarsAPIUsingMultipleYearsAsyncApproach2(List<Integer> years) {
        stopWatchReset();
        startTimer();
        List<CompletableFuture<List<Car>>> cars = years.stream()
                .map(year -> CompletableFuture.supplyAsync(() -> invokeCarsAPIWithYear(year)))
                .collect(Collectors.toList()); // list of completable futures, and when each completable future is .join() we get list of cars in its position

        CompletableFuture<Void> completableFutureAllOf = CompletableFuture.allOf(cars.toArray(new CompletableFuture[cars.size()]));

        List<Car> carResult = completableFutureAllOf.thenApply(v -> cars.stream() // .thenApply() will only get invoked after all the above completable futures are completed
                        .map(CompletableFuture::join)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()))
                .join();

        timeTaken();
        return carResult;
    }
}
