package tests.measure.jvm.memory;

public class MemoryInfo {
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();

        long totalMemory = runtime.totalMemory();
        long maxMemory = runtime.maxMemory();

        System.out.println("Total Memory (bytes): " + totalMemory);
        System.out.println("Max Memory (bytes): " + maxMemory);

        // Convert bytes to megabytes for better readability
        long totalMemoryMB = totalMemory / (1024 * 1024);
        long maxMemoryMB = maxMemory / (1024 * 1024);

        System.out.println("Total Memory (MB): " + totalMemoryMB);
        System.out.println("Max Memory (MB): " + maxMemoryMB);
    }
}
