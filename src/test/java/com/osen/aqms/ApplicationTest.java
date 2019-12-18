package com.osen.aqms;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: PangYi
 * Date: 2019-12-10
 * Time: 10:11
 * Description:
 */
public class ApplicationTest {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(50);
        list.add(45);
        list.add(25);
        list.add(98);
        list.add(32);
        List<Integer> collect = list.stream().sorted().collect(Collectors.toList());
        System.out.println("list<Integer>元素正序：" + collect);
    }
}
