package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

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
        node.setHasRight(false);
        nodeNumber++;
        node.setValue(parser.getProductionByOrderNumber(productions.get(0)).leftHand);
        this.root = node;
        nodes.push(node);
        nodeList = new ArrayList<>();
        nodeList.add(this.root);
        while (prodListIndex < productions.size() && !nodes.isEmpty()){
            Node currentNode = nodes.peek(); // so that we know the father
            if(parser.getGrammar().getTerminals().contains(currentNode.getValue()) || currentNode.getValue().equals("ε")){

                while(nodes.size()>0 && !nodes.peek().getHasRight()) {
                    nodes.pop();
                }
                if(nodes.size() > 0)
                    nodes.pop();
                else
                    break;
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
                nodeList.add(child);
            }
            nodeNumber += children.size()+1;
            prodListIndex += 1;
        }
    }

    private List<Node> getChildren(Integer index){
        List<Node> children = new ArrayList<>();
        for(Node node: nodeList){
            if(node.getParent() == index)
                children.add(node);
        }
        if(children.size()>0)
             children = children.stream().sorted(Comparator.comparing(Node::getIndex)).collect(Collectors.toList());
        return children;
    }


    public List<String> generateSequence(){
        List<String> sequence = new ArrayList<>();
        Stack<Node> nodes = new Stack<>();
        nodes.push(root);
        while (!nodes.isEmpty()){
            Node currentNode = nodes.peek();
            if(parser.getGrammar().getTerminals().contains(currentNode.getValue()) || currentNode.getValue().equals("ε")){
                if(!currentNode.getValue().equals("ε"))
                   sequence.add(currentNode.getValue());
                while (!nodes.isEmpty() && !nodes.peek().getHasRight()){
                    nodes.pop();
                }
                if(!nodes.isEmpty())
                    nodes.pop();
                if(nodes.isEmpty())
                    break;
                continue;
            }
            List<Node> children = this.getChildren(currentNode.getIndex());
            for(int idx = children.size()-1; idx>=0; idx--){
                nodes.push(children.get(idx));
            }
        }
        System.out.println(sequence);
        return sequence;
    }
}
