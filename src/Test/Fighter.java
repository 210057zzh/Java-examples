package Test;

import javax.websocket.OnError;
import java.util.ArrayList;

class Foo<T> {

    public static<T> void fightFoo(T[] list) {

        System.out.println("I fought " + list.length + " foos!");

    }

}

public class Fighter{

    public static void main(String[] args) {

        Double[] doubles = {1.0, 1.1, 1.2, 1.3, 1.4, 1.5}; // Call fightFoo here

    }

}