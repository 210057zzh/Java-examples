package Test;

import javax.websocket.OnError;
import java.util.ArrayList;

class Foo<T> {

    public static <T> void fightFoo(T[] list) {

        System.out.println("I fought " + list.length + " foos!");

    }

}

public class Fighter {

    public static void main(String[] args) {

        String a = "600", b = "9";
        System.out.println(a.compareTo(b));
    }

}