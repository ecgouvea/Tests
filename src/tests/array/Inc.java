package tests.array;

public class Inc {

    public static void main1(String[] args) {
        int[] i = {1};
        Inc in = new Inc();
        in.increment(i);

        System.out.println(i[i.length-1]);
    }

    void increment(int[] i) {
        i[i.length-1]++;
    }

    public synchronized void methodA() {

    }

    public void add(int a) {
        loop: for(int i = 1; i < 3; i++) {
            for(int j = 1; j < 3; j++) {
                if (a == 5) {
                    break loop;
                }
                System.out.println(i * j);
            }
        }
    }

    public static void main(String[] args) {
        Inc in = new Inc();
        in.add(5);
    }
}
