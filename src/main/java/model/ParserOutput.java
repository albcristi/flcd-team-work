package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ParserOutput {
    private Parser parser;
    private static Integer nodeNumber = 1;
    private Node root;
    private List<Integer> productions;
    private List<Node> nodeList;

    public ParserOutput(Parser parser, List<Integer> productions){
        this.parser = parser;
        productions.remove(productions.size()-1);
        this.productions = productions;
        generateTree();
    }

    private void generateTree(){
        Stack<Node> nodes = new Stack<>();
        Integer prodListIndex = 0;
        Node node = new Node();
        node.setParent(-1);
        node.setSibling(-1);
        node.setIndex(nodeNumber);
        nodeNumber++;
        node.setValue(parser.getProductionByOrderNumber(productions.get(0)).leftHand);
        this.root = node;
        nodes.push(node);
        nodeList = new ArrayList<>();
        nodeList.add(this.root);
        while (prodListIndex < productions.size() && !nodes.isEmpty()){
            Node currentNode = nodes.peek(); // so that we know the father
            if(parser.getGrammar().getTerminals().contains(currentNode.getValue())){
                nodes.pop();
                while(!nodes.isEmpty() && !nodes.peek().getHasRight())
                    nodes.pop();
                continue;
            }
            Production prod = parser.getProductionByOrderNumber(productions.get(prodListIndex));
            if(prod.leftHand.equals("-1"))
                break;
            List<String> children = prod.elements;
            nodeNumber += children.size() -1;
            for(Integer pos = prod.elements.size()-1; pos>=0; pos--){
                Node child = new Node();
                child.setParent(currentNode.getIndex());
                child.setIndex(nodeNumber);
                nodeNumber--;
                if(pos<prod.elements.size()-1){
                    child.setHasRight(true);
                }
                else{
                    child.setHasRight(false);
                }
                if(pos>0){
                    child.setSibling(nodeNumber);
                }
                else{
                    child.setSibling(-1);
                }
                child.setValue(children.get(pos));
                nodes.push(child);
                System.out.println("----");
                System.out.println(currentNode);
                System.out.println(child);
                nodeList.add(child);
            }
            nodeNumber += children.size()+1;
            prodListIndex += 1;
        }
        System.out.println("fvgdehwqjdewfhvbjewdfehbfbnjkdewfdhvfjdsksfdjskmsjvfjkdcsmbvbfcnjk");
        System.out.println(nodeList);

    }

}
