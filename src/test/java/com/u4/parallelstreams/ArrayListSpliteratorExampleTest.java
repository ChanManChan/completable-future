package com.u4.parallelstreams;

import com.u4.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayListSpliteratorExampleTest {
    ArrayListSpliteratorExample arrayListSpliteratorExample = new ArrayListSpliteratorExample();
    // not much difference between sequential and parallel executions because ArrayList is an indexed collection,
    // so when parallelStream() is invoked, the spliterator can slice the data into chunks really well.

    @RepeatedTest(5) // it launches the same test repeatedly in the same context
    void multiplyEachValueSequential() {
        int size = 1000000;
        ArrayList<Integer> inputList = DataSet.generateArrayList(size);
        List<Integer> resultList = arrayListSpliteratorExample.multiplyEachValue(inputList, 2, false);

        assertEquals(size, resultList.size());
    }

    @RepeatedTest(5) // it launches the same test repeatedly in the same context
    void multiplyEachValueParallel() {
        int size = 1000000;
        ArrayList<Integer> inputList = DataSet.generateArrayList(size);
        List<Integer> resultList = arrayListSpliteratorExample.multiplyEachValue(inputList, 2, true);

        assertEquals(size, resultList.size());
    }
}