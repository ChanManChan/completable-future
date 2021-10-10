package com.u4.parallelstreams;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.u4.util.CommonUtil.*;

public class LinkedListSpliteratorExample {
    public List<Integer> multiplyEachValue(List<Integer> inputList, int multiplyValue, boolean isParallel) {
        stopWatchReset();
        startTimer();
        Stream<Integer> integerStream = inputList.stream(); //sequential

        if (isParallel) {
            integerStream.parallel();
        }

        List<Integer> resultList = integerStream
                .map(integer -> integer * multiplyValue)
                .collect(Collectors.toList());

        timeTaken();
        return resultList;
    }
}
