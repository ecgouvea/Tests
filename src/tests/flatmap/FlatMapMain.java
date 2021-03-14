package tests.flatmap;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FlatMapMain {
    
    public static void main(String[] args) {
        Map<String, String> m1 = new HashMap<>();
        Map<String, String> m2 = new HashMap<>();
        m2.put("key", "value");
        m1.put("a", m2.toString());

        m2 = new HashMap<>();
        m2.put("key2", "value2");
        m2.put("key3", "value3");
        m1.put("b", m2.toString());

        m1.values().stream().map(v -> "Modified " + v).forEach(System.out::println);
        //m1.values().stream().flatMap(Collection::stream).forEach(System.out::println);

        List<String> l1 = Arrays.asList("a", "b", "c");
        List<String> l2 = Arrays.asList("d", "e", "f");
        List<List> l3 = Arrays.asList(l1, l2);
        l1.stream().map(v -> "Modified " + v).forEach(System.out::println);
        l3.stream().flatMap(Collection::stream).forEach(System.out::println);

        List<List<String>> list = Arrays.asList(
        Arrays.asList("a"),
        Arrays.asList("b"));
        System.out.println("Non-flat list: " + list);

        System.out.println("Flat list: " + list
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList()));
    }
}
