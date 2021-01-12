package com.yczuoxin.others.api.third;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.Map;

/**
 * {@link BidiMap}
 */
public class BidiMapDemo {
    public static void main(String[] args) {
        DualHashBidiMap<Integer, String> hashBidiMap = new DualHashBidiMap<>();
        hashBidiMap.put(1, "a");
        hashBidiMap.put(2, "b");
        hashBidiMap.put(3, "c");

        BidiMap<String, Integer> inverseBidiMap = hashBidiMap.inverseBidiMap();
        for (Map.Entry<String, Integer> entry : inverseBidiMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
