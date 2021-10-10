package com.u4.parallelstreams;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.u4.util.DataSet.generateArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamPerformanceTest {
    ParallelStreamPerformance intStreamExample = new ParallelStreamPerformance();

    @Test
    void sum_using_intstream() {
        //given

        //when
        int sum = intStreamExample.sumUsingIntStream(1000000, false);
        System.out.println("sum_using_intstream : " + sum);

        //then
        assertEquals(1784293664, sum);
    }

    @Test
    void sum_using_intstream_parallel() {
        //given

        //when
        int sum = intStreamExample.sumUsingIntStream(1000000, true);
        System.out.println("sum_using_intstream_parallel : " + sum);

        //then
        assertEquals(1784293664, sum);
    }

    @Test
    void sum_using_iterate() {
        //given

        //when
        int sum = intStreamExample.sumUsingIterate(1000000, false);
        System.out.println("sum_using_iterate : " + sum);

        //then
        assertEquals(1784293664, sum);
    }

    @Test
    void sum_using_iterate_parallel() {
        //given

        //when
        int sum = intStreamExample.sumUsingIterate(1000000, true);
        System.out.println("sum_using_iterate_parallel : " + sum);

        //then
        assertEquals(1784293664, sum);
    }

    @Test
    void sum_using_list() {
        //given
        int size = 1000000;
        ArrayList<Integer> inputList = generateArrayList(size);
        //when
        int sum = intStreamExample.sumUsingList(inputList, false);
        System.out.println("sum_using_list : " + sum);

        //then
        assertEquals(1784293664, sum);
    }

    @Test
    void sum_using_list_parallel() {
        //given
        int size = 1000000;
        ArrayList<Integer> inputList = generateArrayList(size);
        //when
        int sum = intStreamExample.sumUsingList(inputList, true);
        System.out.println("sum_using_list_parallel : " + sum);

        //then
        assertEquals(1784293664, sum);
    }

}