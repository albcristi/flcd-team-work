package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Grammar {
    List<String> terminals;
    List<String> nonTerminals;
    HashMap<String, List<String>> productions;
    String pathToFile;
    String startingSymbol;

    public Grammar(String pathToFile){
        terminals = new ArrayList<>();
        nonTerminals = new ArrayList<>();
        productions = new HashMap<>();
        this.pathToFile = pathToFile;
        readFromFile();
    }

    public String getStartingSymbol(){
        return this.startingSymbol;
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
            List<String> terminalsFromFile = Arrays.asList(reader.readLine().split(","));
            List<String> terminals = new ArrayList<>();
            terminals.addAll(terminalsFromFile);
            String flag = reader.readLine();
            if(flag.equals("true")){
                terminals.add(",");
            }
            this.nonTerminals = nonTerminals;
            this.terminals = terminals;
            // now we read the productions
            while (true){
                String line = reader.readLine();
                if(line==null || line.equals("")){
                    break;
                }
                List<String> splitV1 = Arrays.asList(line.strip().split("->"));
                String nonTerminal = splitV1.get(0).strip();
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
            this.startingSymbol = this.nonTerminals.get(0);
            reader.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public List<Pair> getRulesThatContainNonTerminal(String nonTerminal){
        List<Pair> result = new ArrayList<>();
        for(String  key: productions.keySet())
            for(String production: productions.get(key))
                if(production.contains(nonTerminal)){
                    Pair p = new Pair();
                    p.key=key;
                    p.rule=production;
                    result.add(p);
                }
        return result;
    }
}


