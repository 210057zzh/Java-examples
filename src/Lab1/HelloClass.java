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
//        File dir = new File("tmp/test");
//        dir.mkdirs();
//        File tmp = new File(dir, "tmp.txt");
//        try {
//            tmp.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        OutputStream f = new FileOutputStream(tmp);
//        f.write("apple".getBytes(StandardCharsets.UTF_8));
        Random random = new Random();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            System.out.print("What would you like to do?\n" +
                    "1. Compress\n" +
                    "2. Decompress\n" +
                    "3. Exit\n" +
                    "Your choice:");
        }
    }
}
