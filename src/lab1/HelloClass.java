package lab1;

class C0 {
    public void meth3() {
        System.out.println("3");
    }
}

class C1 extends C0 {
    public void meth3() {
        System.out.println("4");
    }
}

public class HelloClass {
    public static void meth(C0 c) {
        c.meth3();
    }

    public static void main(String[] args) {
        C0 c0 = new C1();
        c0.meth3();

        meth(c0);

        C1 c1 = new C1();
        c1.meth3();
        meth(c1);
    }
}
