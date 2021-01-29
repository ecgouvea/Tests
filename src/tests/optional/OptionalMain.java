package tests.optional;

import java.util.Optional;

public class OptionalMain {
    
    public static void main(String[] args) {
        String initialValue = "not null";

        System.out.println("Call orElse");
        String name = Optional.ofNullable(initialValue).orElse(getDefault());
        System.out.println(name);
        
        System.out.println("\nCall orElseGet");
        name = Optional.ofNullable(initialValue).orElseGet(OptionalMain::getDefault);
        System.out.println(name);
        
        System.out.println("\nCall orElseGet with lambda expression");
        name = Optional.ofNullable(initialValue).orElseGet(() -> getDefault());
        System.out.println(name);
    }

    private static String getDefault() {
        System.out.println("getDefault called");
        return "default";
    }
}
