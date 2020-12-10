import model.Grammar;
import model.Parser;
import model.ParserOutput;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("aaa");
        Grammar g = new Grammar("./input/g2.in");
        Parser p = new Parser(g);
        p.printParseTable();
        List<String> seq = new ArrayList<>();
//        seq.add("inti");
//        seq.add("id");
//        seq.add(";");
//        seq.add("sayInti");
//        seq.add("(");
//        //seq.add("id");
//        seq.add(")");
//        seq.add(";");
//        seq.add("id");
//        seq.add("=");
//        seq.add("id");
//        seq.add("+");
//        seq.add("id");
//        seq.add(";");
//        seq.add("id");
//        seq.add("=");
//        seq.add("id");
//        seq.add("+");
//        seq.add("id");
//        seq.add(";");

//        seq.add("(");
//        seq.add("a");
//        seq.add("+");
//        seq.add("a");
//        seq.add("hmm");
//        seq.add("(");
//        seq.add("id");
//        seq.add(">");
//        seq.add("id");
//        seq.add(")");
//        seq.add("{");
//        seq.add("id");
//        seq.add("=");
//        seq.add("id");
//        seq.add(";");
//        seq.add("}");

        seq = p.readPIF("./input/pif.out");
        System.out.println(seq);
        List<Integer> s = p.parseSequence(seq);
        ParserOutput po = new ParserOutput(p, s);
        po.generateSequence();
        po.printNodes();
        System.out.println(po.generateSequence());
        po.writeToFile("./output/out1.txt");
        System.out.println(p.parseSequence(seq));

    }
}
