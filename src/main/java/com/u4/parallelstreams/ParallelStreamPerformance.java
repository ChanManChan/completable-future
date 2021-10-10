package com.u4.parallelstreams;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.u4.util.CommonUtil.*;

public class ParallelStreamPerformance {
    public int sumUsingIntStream(int count, boolean isParallel) {
        stopWatchReset();
        startTimer();
        IntStream intStream = IntStream.rangeClosed(0, count);

        if (isParallel) {
            intStream.parallel();
        }

        int sum = intStream.sum();
        timeTaken();
        return sum;
    }

    public int sumUsingList(List<Integer> inputList, boolean isParallel) {
        stopWatchReset();
        startTimer();
        Stream<Integer> inputStream = inputList.stream();

        if (isParallel) {
            inputStream.parallel();
        }

        int sum = inputStream
                .mapToInt(Integer::intValue) //unboxing; Integer -> primitive (doesn't perform well if run in parallel)
                .sum();
        timeTaken();
        return sum;
    }

    public int sumUsingIterate(int n, boolean isParallel) {
        stopWatchReset();
        startTimer();
        Stream<Integer> integerStream = Stream.iterate(0, integer -> integer + 1);

        if (isParallel) {
            integerStream.parallel();
        }

        int sum = integerStream
                .limit(n + 1) // includes the ent value too
                .reduce(0, Integer::sum);
        timeTaken();
        return sum;
    }
}
