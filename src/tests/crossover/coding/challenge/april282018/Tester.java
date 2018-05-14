package tests.crossover.coding.challenge.april282018;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.function.Consumer;
import java.util.regex.*;
import java.util.stream.Stream;

public class Tester {

    /**
     * Complete the function below.
     * DO NOT MODIFY anything outside this method.
     */
    static int[] balancedOrNot(String[] expressions, int[] maxReplacements) {
        List<Integer> list = new LinkedList<>();
        int[] result = null;

        System.out.println("------------------------------------ begin print ------------------------------------");

        for (int i = 0; i < expressions.length; i++) {
            String expr = expressions[i];
            int repl = maxReplacements[i];

            if (expr.endsWith("<")) {
                list.add(0);
            } else {

            }

        }

        System.out.println("------------------------------------ end print --------------------------------------");

        result = new int[list.size()];

        for(int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }

        return result;
    }

    /**
     * DO NOT MODIFY THIS METHOD!
     */
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        int[] res;

        int _expressions_size = 0;
        _expressions_size = Integer.parseInt(in.nextLine().trim());
        String[] _expressions = new String[_expressions_size];
        String _expressions_item;
        for (int _expressions_i = 0; _expressions_i < _expressions_size; _expressions_i++) {
            try {
                _expressions_item = in.nextLine();
            } catch (Exception e) {
                _expressions_item = null;
            }
            _expressions[_expressions_i] = _expressions_item;
        }

        int _maxReplacements_size = 0;
        _maxReplacements_size = Integer.parseInt(in.nextLine().trim());
        int[] _maxReplacements = new int[_maxReplacements_size];
        int _maxReplacements_item;
        for (int _maxReplacements_i = 0; _maxReplacements_i < _maxReplacements_size; _maxReplacements_i++) {
            _maxReplacements_item = Integer.parseInt(in.nextLine().trim());
            _maxReplacements[_maxReplacements_i] = _maxReplacements_item;
        }

        res = balancedOrNot(_expressions, _maxReplacements);
        for (int res_i = 0; res_i < res.length; res_i++) {
            System.out.println(String.valueOf(res[res_i]));
        }
    }
}