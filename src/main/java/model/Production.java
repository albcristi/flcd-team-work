package model;

import java.util.List;

public class Production {
    String leftHand;
    List<String> elements;

    public Production(){}
    public Production(String left, List<String> elements){
        this.leftHand = left;
        this.elements = elements;
    }

    @Override
    public String toString() {
        String s = leftHand+" -> ";
        for(String el: elements)
            s = s.concat(el);
        return s;
    }

    public String getElementsString(){
        String s =" ";
        for(String el: elements)
            s = s.concat(el+" ");
        return s;
    }
}
