import model.Grammar;
import model.Parser;
import model.ParserOutput;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("aaa");
        Grammar g = new Grammar("./input/g1.in");
        Parser p = new Parser(g);
        p.printParseTable();
        List<String> seq = new ArrayList<>();
//        seq.add("inti");
//        seq.add("id");
//        seq.add(";");
//        seq.add("sayInti");
//        seq.add("(");
//        seq.add("id");
//        seq.add(")");
//        seq.add(";");

//        seq.add("(");
        seq.add("a");
        seq.add("+");
        seq.add("a");
//        seq.add(")");
//        seq.add("+");
//        seq.add("a");

        ParserOutput po = new ParserOutput(p, p.parseSequence(seq));
        po.generateSequence();
        po.printNodes();
        //System.out.println(p.parseSequence(seq));

    }
}
