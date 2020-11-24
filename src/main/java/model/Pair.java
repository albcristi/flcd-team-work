package model;

public class Pair {
    public String first;
    public String second;

    public Pair(String f, String s){
        this.first = f;
        this.second = s;
    }

    public Pair(){}
    @Override
    public String toString() {
        return "(" +
                first +
                "," + second +
                ')';
    }
}
