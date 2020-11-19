package model;

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
    }

    private void generateFirstTable(){
        /*
           Function that, based on the FIRST algorithm,
           will generate the first table
         */
    }

    private void generateFollowTable(){
         /*
           Function that, based on the FOLLOW algorithm,
           will generate the follow table
         */

    }

    private void generateParseTable(){
         /*
           Function that will crate the Parse Table
         */

    }
}
