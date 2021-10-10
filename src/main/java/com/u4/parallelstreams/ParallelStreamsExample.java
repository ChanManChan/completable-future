package com.u4.parallelstreams;

import com.u4.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.u4.util.CommonUtil.*;
import static com.u4.util.LoggerUtil.log;

public class ParallelStreamsExample {

    public List<String> stringTransform(List<String> namesList) {
        return namesList
//                .stream()
                .parallelStream()
                .map(this::addNameLengthTransform)
                .collect(Collectors.toList());
    }

    public List<String> stringTransformSwitch(List<String> namesList, boolean isParallel) {
        Stream<String> namesStream = namesList.stream().sequential();

        if (isParallel) {
            namesStream.parallel();
        }

        return namesStream
                .map(this::addNameLengthTransform)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<String> namesList = DataSet.namesList();
        ParallelStreamsExample parallelStreamsExample = new ParallelStreamsExample();
        startTimer();
        List<String> resultList = parallelStreamsExample.stringTransform(namesList);
        timeTaken();
        log("resultList: " + resultList);
    }

    private String addNameLengthTransform(String name) {
        delay(500);
        return name.length() + " - " + name;
    }
}
