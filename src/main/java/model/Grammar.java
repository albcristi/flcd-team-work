package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Grammar {
    List<String> terminals;
    List<String> nonTerminals;
    HashMap<String, List<String>> productions;
    String pathToFile;

    public Grammar(String pathToFile){
        terminals = new ArrayList<>();
        nonTerminals = new ArrayList<>();
        productions = new HashMap<>();
        this.pathToFile = pathToFile;
        readFromFile();
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }


    public HashMap<String, List<String>> getProductions() {
        return productions;
    }

    public List<String> getProductionsForNonTerminal(String nonTerminal){
        return this.productions.get(nonTerminal);
    }


    private void readFromFile(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.pathToFile));
            String lineOfNonTerminals = reader.readLine();
            List<String> nonTerminals = Arrays.asList(lineOfNonTerminals.split(","));
            List<String> terminals = Arrays.asList(reader.readLine().split(","));
            this.nonTerminals = nonTerminals;
            this.terminals = terminals;
            // now we read the productions
            while (true){
                String line = reader.readLine();
                if(line==null || line.equals("")){
                    break;
                }
                List<String> splitV1 = Arrays.asList(line.strip().split("->"));
                String nonTerminal = splitV1.get(0);
                // split based on "|"
                List<String> rightSide = Arrays.asList(splitV1.get(1).strip().split("\\|"));
                for(String element: rightSide){
                    String ele = element.strip();
                    List<String> lst = productions.get(nonTerminal);
                    if(lst == null){
                        lst = new ArrayList<>();
                        lst.add(ele);
                        productions.put(nonTerminal, lst);
                    }
                    else{
                        lst.add(ele);
                    }

                }
            }
            reader.close();
            System.out.println(productions);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


    }
}


