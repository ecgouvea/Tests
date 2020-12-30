package tests.set;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class MainSet {

    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(1); // It doesn't get added to the set but it doesn't throw exception either.
        System.out.println(set);

        Set<Integer> set2 = new TreeSet<>();
        set2.add(1);
        set2.add(2);
        set2.add(1); // It doesn't get added to the set but it doesn't throw exception either.
        System.out.println(set2);
    }
}
