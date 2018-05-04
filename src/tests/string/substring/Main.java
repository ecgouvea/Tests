package tests.string.substring;

public class Main {

    public static void main(String[] args) {
        // Will throw StringIndexOutOfBoundsException
        System.out.println("1234567890".substring(0, 11));
    }
}
