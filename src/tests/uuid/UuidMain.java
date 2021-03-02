package tests.uuid;

import java.util.UUID;

public class UuidMain {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            UUID uuid = UUID.randomUUID();
            System.out.printf("UUID: %s\n", uuid);
        }
    }
}
