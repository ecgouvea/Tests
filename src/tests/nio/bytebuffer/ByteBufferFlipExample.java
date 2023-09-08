package tests.nio.bytebuffer;

import java.nio.ByteBuffer;

public class ByteBufferFlipExample {
    public static void main(String[] args) {
        // Create a ByteBuffer with a capacity of 10 bytes
        ByteBuffer buffer = ByteBuffer.allocateDirect(10);

        // Write data into the buffer
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);

        // Flip the buffer to read mode
        buffer.flip();

//        buffer.get();
//        buffer = buffer.slice();
//        buffer.get();
//        buffer =
//        buffer.clear(); //compact().clear();

        // Read data from the buffer
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
    }
}
