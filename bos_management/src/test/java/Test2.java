public class Test2 {
    public static void main(String[] args) {
        //  Test2.doubleNum(1237);
        System.out.println(Test2.computeAge(8));
    }

    public static void doubleNum(int n) {
        // System.out.println(n);
        if (n <= 5000)
            doubleNum(n * 2);
        System.out.println(n);
    }

    public static int computeAge(int n) {
        if (n == 1) return 10;
        return computeAge(n - 1) + 2;
    }

}
