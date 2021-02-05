package tests.optional;

import java.util.Optional;

public class OptionalDtoMain {
    
    public static void main(String[] args) {
        Dto initialValue = null;
        Optional<Dto> dto = null;

        System.out.println("=================================");
        System.out.println("First example");
        System.out.println("=================================");
        try {
            System.out.println("Call get().getName()");
            dto = Optional.ofNullable(initialValue);
    
            // Results in: Exception in thread "main" java.util.NoSuchElementException: No value present
            System.out.println(dto.get().getName());
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }


        System.out.println("=================================");
        System.out.println("Second example");
        System.out.println("=================================");
        try {
            System.out.println("Call ifPresent with lambda");
            dto = Optional.ofNullable(initialValue);
    
            // Results in: Exception in thread "main" java.util.NoSuchElementException: No value present
            dto.ifPresentOrElse(o -> System.out.println(o.getName()), () -> System.out.println("o.getName() returns null"));
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }


        initialValue = null; //new OptionalDtoMain().new Dto("My Name");

        System.out.println("=================================");
        System.out.println("Third example");
        System.out.println("=================================");
        System.out.println("Call get().getName()");
        Dto dto2 = Optional.ofNullable(initialValue).orElse(new OptionalDtoMain().new Dto());

        // Results in: Exception in thread "main" java.util.NoSuchElementException: No value present
        System.out.println(dto2.getName());
    }

    private class Dto {
        private String name;

        public Dto() {
        }
        
        public Dto(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }
}
