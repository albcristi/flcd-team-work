package UI;

import model.Grammar;
import model.Parser;
import model.ParserOutput;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class ConsoleUserInterface {
    private Grammar g1, g2;

    public ConsoleUserInterface(){
        g1 = new Grammar("./input/g1.in");
        g2 = new Grammar("./input/g2.in");
    }

    private void printCommands(){
        System.out.println("1 - Run G1");
        System.out.println("2 - Run G2");
        System.out.println("X - Exit");
    }

    private void runCommand1(){
        Parser parser = new Parser(g1);
        List<Integer> sequence = parser.parseSequence(parser.readSequenceTXT("./input/seq.txt"));
        ParserOutput output = new ParserOutput(parser, sequence);
        output.printNodes();
        output.writeToFile("./output/out1.txt");
    }

    private void runCommand2(){
        Parser parser = new Parser(g2);
        List<Integer> sequence = parser.parseSequence(parser.readPIF("./input/pif2.out"));
        ParserOutput output = new ParserOutput(parser, sequence);
        output.printNodes();
        output.writeToFile("./output/out2.txt");
    }

    public void run(){
        while (true){
            try {
                printCommands();
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Enter command:");
                String command = reader.readLine();
                switch (command){
                    case "1":{
                        runCommand1();
                        break;
                    }
                    case "2":{
                        runCommand2();
                        break;
                    }
                    case "x":{
                        return;
                    }
                    default:{
                        throw new RuntimeException("Invalid Command!");
                    }
                }

            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
