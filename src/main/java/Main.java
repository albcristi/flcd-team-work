import model.Grammar;
import model.Parser;

public class Main {

    public static void main(String[] args) {
        System.out.println("aaa");
        Grammar g = new Grammar("./input/g2.in");
        System.out.println("Grammar non-terminals");
        System.out.println(g.getNonTerminals());
        System.out.println("Grammar terminals");
        System.out.println(g.getTerminals());
        System.out.println("Grammar productions");
        System.out.println(g.getProductions());
        System.out.println("Productions for terminal S");
        System.out.println(g.getProductionsForNonTerminal("S"));
        System.out.println("Productions for terminal A");
        System.out.println(g.getProductionsForNonTerminal("A"));

        Parser p = new Parser(g);



    }
}
