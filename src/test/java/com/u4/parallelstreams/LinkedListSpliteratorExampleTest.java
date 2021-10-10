package com.u4.parallelstreams;

import com.u4.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListSpliteratorExampleTest {
    // parallel stream works worse in this example
    LinkedListSpliteratorExample linkedListSpliteratorExample = new LinkedListSpliteratorExample();

    @RepeatedTest(5)
    void multiplyEachValueSequential() {
        System.out.println("SEQUENTIAL--------");
        int size = 1000000;
        LinkedList<Integer> inputList = DataSet.generateIntegerLinkedList(size);
        List<Integer> resultList = linkedListSpliteratorExample.multiplyEachValue(inputList, 2, false);

        assertEquals(size, resultList.size());
    }

    @RepeatedTest(5)
    void multiplyEachValueParallel() {
        System.out.println("PARALLEL--------");
        int size = 1000000;
        LinkedList<Integer> inputList = DataSet.generateIntegerLinkedList(size);
        List<Integer> resultList = linkedListSpliteratorExample.multiplyEachValue(inputList, 2, true);

        assertEquals(size, resultList.size());
    }
}