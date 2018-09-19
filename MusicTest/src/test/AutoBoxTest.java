package test;

import java.io.PrintStream;

public class AutoBoxTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Long g = 3L;
        PrintStream out=System.out;
        out.println(c == d);
        out.println(c == (a + b) );
        out.println(c.equals(a + b) );
        out.println(g == (a + b) );
        out.println(g.equals(a+b));
    }

}
