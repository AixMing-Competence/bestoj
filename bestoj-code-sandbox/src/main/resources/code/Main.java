import java.util.Scanner;

/**
 * @author AixMing
 * @since 2025-03-20 15:42:57
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = Integer.parseInt(args[0]), b = args[1];
        System.out.println(a + b);
        scanner.close();
    }
}