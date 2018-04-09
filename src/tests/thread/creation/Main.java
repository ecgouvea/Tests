package tests.thread.creation;

public class Main {

    public static void main(String[] args) {
        System.out.println("hello!");
        MyTask t1 = new Main().new MyTask();

        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(t1);
            thread.start();
        }

        System.out.println("bye!");
    }

    class MyTask implements Runnable {

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println("Thread created: " + threadName);

            int waitTime = (int) (Math.random() * 10000);

            System.out.println(threadName + " will wait " + waitTime + " milliseconds");

            try {
                Thread.sleep(waitTime);
                System.out.println(threadName + " left wait period after " + waitTime + " milliseconds");
            } catch (InterruptedException e) {
                System.out.println(threadName + " got interrupted");
                e.printStackTrace();
            }
        }
    }
}
