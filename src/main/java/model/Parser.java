package model;

import java.util.*;

public class Parser {
    private Grammar grammar;
    private HashMap<String, List<String>> firstTable;
    private HashMap<String, List<String>> followTable;
    private HashMap<Pair, String>  parseTable;
    private static String EMPTY_CELL = null;


    public Parser(Grammar grammar){
        this.grammar = grammar;
        this.firstTable = new HashMap<>();
        this.followTable = new HashMap<>();
        this.generateFirstTable();
        this.followOfIterative();
        this.initializeParseTable();
        generateParsingTable();

//        printParseTable();
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
            List<String> components = Arrays.asList(pair.second.split(" "));
            Integer index =  components.indexOf(nonTerminal);
            if(index == components.size()-1){
                if(nonTerminal == pair.first)
                    return values;
                else {
                    values.addAll(followOf(pair.first, startTerminal, false)); // inf recursion

                }
            }
            else{
                List<String> firstOfRight = firstOfSequence(components.subList(index+1, components.size()));
                if(firstOfRight.contains("ε")){
                    values.addAll(toSet(firstOfRight));
                    values.addAll(followOf(pair.first, startTerminal,false));
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
                    List<String> components = Arrays.asList(pair.second.split(" "));
                    Integer index =  components.indexOf(nonTerminal);
                    if(index == components.size()-1){
                        if(!pair.first.equals(nonTerminal))
                            currentFollow.get(nonTerminal).addAll(previousFollow.get(pair.first));
                    }
                    else{
                        List<String> firstValues = firstOfSequence(components.subList(index+1, components.size()));
                        if(firstValues.contains("ε")){
                           firstValues.remove("ε");
                           currentFollow.get(nonTerminal).addAll(previousFollow.get(pair.first));
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

    private void initializeParseTable(){
        List<String> lines = new ArrayList<>();
        lines.addAll(grammar.getNonTerminals());
        lines.addAll(grammar.getTerminals());
        lines.add("ε"); // dollar
        List<String> columns = new ArrayList<>();
        columns.addAll(grammar.getTerminals());
        columns.add("ε");
        parseTable = new HashMap<>();
        for(String l: lines)
            for(String c: columns){
                if(l.equals(c) && grammar.getTerminals().contains(l)) {
                    parseTable.put(new Pair(l, c), "pop");
                    continue;
                }
                if(l.equals(c) && l.equals("ε")){
                    parseTable.put(new Pair(l,c),"acc");
                    continue;
                }
                parseTable.put(new Pair(l,c), null);
            }

    }

    public void printParseTable(){
        for(Pair pair: parseTable.keySet())
            if(parseTable.get(pair) != null)
                System.out.println(pair+" ---> "+parseTable.get(pair));
    }

    private void generateParsingTable(){
        HashMap<Integer, Production> orderOfProductions = grammar.getOrderOfProductions();
        for(Integer i: orderOfProductions.keySet()){
            List<String> firstSet;
            if(orderOfProductions.get(i).elements.size() == 1 && orderOfProductions.get(i).elements.contains("ε")){
                firstSet = new ArrayList<>();
                firstSet.add("ε");
            }
            else {
                firstSet =firstOfSequence(orderOfProductions.get(i).elements);
            }
            if(!firstSet.contains("ε")){
                for(String elem: firstSet){
                    Pair pair = new Pair(orderOfProductions.get(i).leftHand, elem);
                    String tableElement = orderOfProductions.get(i).getElementsString()+"~"+i;
                    if(parseTable.get(pair)!=null){
                        // ASK IF WE SHOULD STOP HERE
                        System.out.println("!!!! CONFLICT !!!!!! At="+pair+" older: "+parseTable.get(pair)+"  new="+tableElement);
                    }
                    else{
                        parseTable.remove(pair);
                        parseTable.put(pair,tableElement);
                    }
                }
            }
            else{
                // if cont EPS, follow
                List<String> followOfLine = followTable.get(orderOfProductions.get(i).leftHand);
                for(String followVal: followOfLine){
                    Pair pair = new Pair(orderOfProductions.get(i).leftHand, followVal);
                    String tableElem = "ε~"+i;
                    if(parseTable.get(pair) != null){
                        System.out.println("!!!! CONFLICT !!!!!! At="+pair+" older: "+parseTable.get(pair)+"  new="+tableElem);
                    }
                    else{
                        parseTable.remove(pair);
                        parseTable.put(pair,tableElem);
                    }
                }
            }
        }
    }


    public List<Integer> parseSequence(List<String> sequence){
        //returns a list -> on the last position we have -1 (if error) or -2 (if acc)

        //initialize first stack
        Stack<String> alpha = new Stack<>();
        Stack<String> beta = new Stack<>();
        List<Integer> result = new ArrayList<>();

        alpha.push("$");
        for(int i = sequence.size()-1; i >=0;i--)
            alpha.push(sequence.get(i));

        beta.push("$");
        beta.push(this.grammar.getStartingSymbol());

        while(! (alpha.peek().equals("$") && beta.peek().equals("$")) ){
            String alphaTop = alpha.peek();
           /* System.out.println("----------------");
            System.out.println("-" + alphaTop + "-");*/
            String betaTop = beta.peek();
            //System.out.println("-" + betaTop + "-");
            if(alphaTop.equals("$"))
                alphaTop = "ε";
            if(betaTop.equals("$"))
                betaTop = "ε";
            if (this.grammar.getNonTerminals().contains(betaTop)){
                //System.out.println("push");
                String res = this.parseTable.get(new Pair(betaTop, alphaTop));
                //System.out.println(res);
                if(res == null){
                    result.add(-1);
                    return result;
                }
                String[] res_split = res.split("~");
                List<String> prod = Arrays.asList(res_split[0].split(" "));
                Integer prodNumber = Integer.parseInt(res_split[1]);

                if(prod.get(0).equals("ε")){
                    beta.pop();
                    result.add(prodNumber);
                }
                else{
                    beta.pop();
                    for(int i=prod.size()-1; i>=0; i--){
                        if(!prod.get(i).equals(""))
                            beta.push(prod.get(i));
                        //System.out.println(beta);
                    }
                    result.add(prodNumber);
                }
            }
            else if(this.grammar.getTerminals().contains(betaTop) && alphaTop.equals(betaTop)){
                // pop case
               // System.out.println("pop");
                alpha.pop();
                beta.pop();
            }
            else {
                //System.out.println("err");
                result.add(-1);
                return result;
            }

        }
        result.add(-2);
        return result;
    }

    public Grammar getGrammar(){
        return this.grammar;
    }

    public Production getProductionByOrderNumber(Integer prodNumber){
        for(Integer key: grammar.orderOfProductions.keySet())
            if(key == prodNumber)
                return grammar.orderOfProductions.get(key);
        Production production = new Production();
        production.elements=null;
        production.leftHand="-1";
        return production;
    }


}
