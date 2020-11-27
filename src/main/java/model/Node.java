package model;

import java.util.List;

public class Node {
    private Integer parent;
    private String value;
    private Integer index;
    private Integer sibling;
    private Boolean hasRight;

    public void setHasRight(Boolean hasRight){
        this.hasRight = hasRight;
    }

    public Boolean getHasRight(){
        return this.hasRight;
    }


    public Integer getParent() {
        return parent;
    }

    public void setIndex(Integer index){
        this.index = index;
    }

    public Integer getSibling() {
        return sibling;
    }

    public void setSibling(Integer sibling) {
        this.sibling = sibling;
    }

    public Integer getIndex(){
       return this.index;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Node{" +
                "parent=" + parent +
                ", value='" + value + '\'' +
                ", index=" + index +
                ", sibling=" + sibling +
                '}';
    }
}
