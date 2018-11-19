package com.april.house.test;

import com.april.house.common.model.House;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author :  ZouShumin
 * @Creation :  2018-11-19 14:33
 * --------  ---------  --------------------------
 */
public class OtherTest {
    @Test
    public void testStream() {
        Set<Object> sets = Sets.newHashSet(1L, 2L, 3L, 4L);
        List<Long> ids = sets.stream().map(String::valueOf).map(Long::valueOf).collect(Collectors.toList());
        ids.forEach(System.out::println);
    }

    @Test
    public void testOrdering() {
        List<Long> order = Lists.newArrayList(5L, 7L, 3L, 8L, 2L, 1L, 9L);
//        System.out.println(order.indexOf(3L));
        Ordering<Long> ordering = Ordering.natural();
        List<Long> sorted = ordering.sortedCopy(order);
        order.forEach(o -> System.out.print(o + " "));
        System.out.println();
        sorted.forEach(s -> System.out.print(s + " "));
        System.out.println();

        List<House> houses = sorted.stream().map(s -> {
            House house = new House();
            house.setId(s);
            return house;
        }).collect(Collectors.toList());

        System.out.println(1);
        Ordering<House> houseSort = Ordering.natural().onResultOf(hs -> order.indexOf(hs.getId()));
        List<House> sortedHouses = houseSort.sortedCopy(houses);
        houses.forEach(h -> System.out.print(h.getId() + " "));
        System.out.println();
        sortedHouses.forEach(s -> System.out.print(s.getId() + " "));
        System.out.println();
    }
}
