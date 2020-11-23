package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Parser {
    private Grammar grammar;
    private HashMap<String, List<String>> firstTable;
    private HashMap<String, List<String>> followTable;

    public Parser(Grammar grammar){
        this.grammar = grammar;
        this.firstTable = new HashMap<>();
        this.followTable = new HashMap<>();
        this.generateFirstTable();
        this.followOfIterative();
        System.out.println(followTable);
        //System.out.println(this.followTable);
    }

    private void generateFirstTable(){
        /*
           Function that, based on the FIRST algorithm,
           will generate the first table
         */
        for (String nonTerminal : grammar.getNonTerminals()) {
            this.firstTable.put(nonTerminal, this.toSet(this.firstOf(nonTerminal)));
        }
    }

    private List<String> firstOf(String nonTerminal) {
        /*
        For every non terminal, compute its First
         */
        if (firstTable.containsKey(nonTerminal))
            return firstTable.get(nonTerminal);
        List<String> result = new ArrayList<>();
        List<String> terminals = grammar.getTerminals();
        System.out.println(nonTerminal);
        System.out.println(grammar.getProductionsForNonTerminal(nonTerminal));
        for (String production : grammar.getProductionsForNonTerminal(nonTerminal)) {
            List<String> productionRules = Arrays.asList(production.split(" "));
            String firstSymbol = productionRules.get(0).strip();
            if(productionRules.size()==1) {
                if (productionRules.get(0).equals("ε")) {
                    result.add("ε");
                    continue;
                }
            }
            if(terminals.contains(firstSymbol)){
                result.add(firstSymbol);
                continue;
            }
            // first symbol is a non terminal (ex: A B a)
            int index = -1;
            for(String symbol: productionRules){
                index ++;
                if(terminals.contains(symbol))
                    result.add(symbol);
                else {
                    List<String> firstOfCurrentSymbolNonTerminal = firstOf(symbol);
                    if(firstOfCurrentSymbolNonTerminal.contains("ε") && index < this.getPosOfTheLastNonTerminal(productionRules)) {
                        // S -> A B a b c
                        // B -> .... eps
                        //first of the current symbol which is a non terminal does not contain epsilon
                        // and the current symbol is not the last non terminal from the current production
                        firstOfCurrentSymbolNonTerminal.remove("ε");
                        result.addAll(firstOfCurrentSymbolNonTerminal);
                    }
                    else{
                        result.addAll(firstOfCurrentSymbolNonTerminal);
                        if( ! firstOfCurrentSymbolNonTerminal.contains("ε"))
                            break;
                    }

                }
            }
        }
        return result;
    }

    private int getPosOfTheLastNonTerminal(List<String> productionRules){
        // get the index of the last non terminal from a list of rules of a production
        for(int i=productionRules.size()-1; i>=0; i--){
            if(grammar.getNonTerminals().contains(productionRules.get(i)))
                return i;
        }
        return -1;
    }

    private List<String> toSet(List<String> lst){
        /*
        Gets the set version of a list
         */

        List<String> set = new ArrayList<>();
        for(String element: lst)
            if(!set.contains(element))
                set.add(element);
        return set;
    }
    private void generateFollowTable(){
         /*
           Function that, based on the FOLLOW algorithm,
           will generate the follow table
         */
         for(String nonTerminal: this.grammar.getNonTerminals())
             this.followTable.put(nonTerminal, toSet(this.followOf(nonTerminal, nonTerminal, false)));

    }

    private List<String> followOf(String nonTerminal, String startTerminal, Boolean flag){
        List<String> values = new ArrayList<>();
        if(nonTerminal.equals(this.grammar.getStartingSymbol()))
            values.add("ε");

        for(Pair pair: this.grammar.getRulesThatContainNonTerminal(nonTerminal)){
            List<String> components = Arrays.asList(pair.rule.split(" "));
            Integer index =  components.indexOf(nonTerminal);
            if(index == components.size()-1){
                if(nonTerminal == pair.key)
                    return values;
                else {
                    values.addAll(followOf(pair.key, startTerminal, false)); // inf recursion

                }
            }
            else{
                List<String> firstOfRight = firstOfSequence(components.subList(index+1, components.size()));
                if(firstOfRight.contains("ε")){
                    values.addAll(toSet(firstOfRight));
                    values.addAll(followOf(pair.key, startTerminal,false));
                    values = toSet(values);

                }

                values.addAll(toSet(firstOfRight));
                values = toSet(values);
            }
        }
        return toSet(values);
    }

    public List<String> firstOfSequence(List<String> sequence) {
        List<String> result = new ArrayList<>();
        result.add("ε");
        boolean ok = false;
        for (String element : sequence) {
            List<String> firstOf = new ArrayList<>();
            if (grammar.getTerminals().contains(element)) {
                firstOf.add(element);
                ok = true;
            }
            else {
                firstOf = toSet(this.firstTable.get(element));
            }
            result = plusWithCircle(result, firstOf);

            if(ok || !firstOf.contains("ε"))
                return toSet(result);
        }
        return toSet(result);
    }

    public List<String> plusWithCircle(List<String> l1, List<String> l2){
        List<String> result = new ArrayList<>();
        if(l1.contains("ε")){
            result.addAll(l1);
            result.remove("ε");
            result.addAll(l2);
            return toSet(result);
        }
        return toSet(l1);
    }


    private void followOfIterative(){
        HashMap<String, List<String>> previousFollow = new HashMap<>();
        for(String nonTerminal: grammar.getNonTerminals()){
            // initialization step
            List<String> values = new ArrayList<>();
            if(nonTerminal.equals(grammar.startingSymbol)){
                values.add("ε");
            }
            previousFollow.put(nonTerminal, values);
        }

        while (true){
            HashMap<String, List<String>> currentFollow = initHashMap();
            for(String nonTerminal: grammar.getNonTerminals()){
                currentFollow.get(nonTerminal).addAll(toSet(previousFollow.get(nonTerminal)));
                for(Pair pair: this.grammar.getRulesThatContainNonTerminal(nonTerminal)){
                    List<String> components = Arrays.asList(pair.rule.split(" "));
                    Integer index =  components.indexOf(nonTerminal);
                    if(index == components.size()-1){
                        if(!pair.key.equals(nonTerminal))
                            currentFollow.get(nonTerminal).addAll(previousFollow.get(pair.key));
                    }
                    else{
                        List<String> firstValues = firstOfSequence(components.subList(index+1, components.size()));
                        if(firstValues.contains("ε")){
                           firstValues.remove("ε");
                           currentFollow.get(nonTerminal).addAll(previousFollow.get(pair.key));
                        }
                        currentFollow.get(nonTerminal).addAll(firstValues);
                    }
                }
            }
            for(String key: currentFollow.keySet()){
                currentFollow.put(key,toSet(currentFollow.get(key)));
            }
            if(stopFollowAlgorithm(previousFollow, currentFollow)){
                this.followTable = currentFollow;
                return;
            }
            previousFollow = currentFollow;
        }
    }

    private HashMap<String, List<String>> initHashMap(){
        HashMap<String, List<String>> init = new HashMap<>();
        for(String key: grammar.getNonTerminals())
            init.put(key, new ArrayList<>());
        return init;
    }

    private Boolean stopFollowAlgorithm(HashMap<String, List<String>> f1, HashMap<String, List<String>> f2){
        try {
            for (String nonTerminal : f1.keySet()) {
                if (f1.get(nonTerminal).size() != f2.get(nonTerminal).size())
                    return false;
                for(String el: f1.get(nonTerminal))
                    if(!f2.get(nonTerminal).contains(el))
                        return false;
            }
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    private void generateParseTable(){
         /*
           Function that will crate the Parse Table
         */

    }
}
