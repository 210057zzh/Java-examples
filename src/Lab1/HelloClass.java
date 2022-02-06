package Lab1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

public class HelloClass {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Integer i = 0;
        while (i != 5) {
            i = scanner.nextInt();
        }
        scanner.close();
        Scanner scanner1 = new Scanner(System.in);
        i = 0;
        while (i != 5) {
            i = scanner1.nextInt();
        }
    }
}
