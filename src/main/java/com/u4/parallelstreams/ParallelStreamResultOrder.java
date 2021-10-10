package com.u4.parallelstreams;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.u4.util.DataSet.generateIntegerList;
import static com.u4.util.DataSet.generateIntegerSet;
import static com.u4.util.LoggerUtil.log;

public class ParallelStreamResultOrder {

    public static List<Integer> listOrder(List<Integer> inputList) {
        return inputList
                .parallelStream()
                .map(integer -> integer * 2)
                .collect(Collectors.toList());
    }

    public static Set<Integer> setOrder(Set<Integer> inputList) {
        return inputList
                .parallelStream()
                .map(integer -> integer * 2)
                .collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        // order of the result is maintained in this case
        log("ORDERED----------");
        List<Integer> inputList = generateIntegerList(8);
        log("inputList: " + inputList);
        List<Integer> resultList = listOrder(inputList);
        log("resultList: " + resultList);

        log("UNORDERED----------");
        Set<Integer> inputSet = generateIntegerSet(8);
        log("inputSet: " + inputSet);
        Set<Integer> resultSet = setOrder(inputSet);
        log("resultSet: " + resultSet);
    }
}
