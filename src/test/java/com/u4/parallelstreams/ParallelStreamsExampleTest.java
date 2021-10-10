package com.u4.parallelstreams;

import com.u4.util.DataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.u4.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamsExampleTest {

    ParallelStreamsExample parallelStreamsExample = new ParallelStreamsExample();

    @Test
    void stringTransform() {
        List<String> inputList = DataSet.namesList();

        startTimer();
        List<String> resultList = parallelStreamsExample.stringTransform(inputList);
        timeTaken();

        assertEquals(inputList.size(), resultList.size());
        resultList.forEach(name -> {
            assertTrue(name.contains("-"));
        });
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void stringTransformSwitch(boolean isParallel) {
        List<String> inputList = DataSet.namesList();
        stopWatchReset();
        startTimer();
        List<String> resultList = parallelStreamsExample.stringTransformSwitch(inputList, isParallel);
        timeTaken();

        assertEquals(inputList.size(), resultList.size());
        resultList.forEach(name -> {
            assertTrue(name.contains("-"));
        });
    }
}