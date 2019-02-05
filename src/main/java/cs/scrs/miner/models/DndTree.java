package cs.scrs.miner.models;

import java.util.ArrayList;

/**
 * Created by Christian on 11/07/2016.
 */
public class DndTree {
    String name;
    NodeInfo info;
    ArrayList<DndTree> children=new ArrayList<>();

    public DndTree() {
    }

    public DndTree(String name, NodeInfo info, ArrayList<DndTree> children) {
        this.name = name;
        this.info = info;
        this.children = children;
    }

    public DndTree(String name, NodeInfo info) {
        this.name = name;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeInfo getInfo() {
        return info;
    }

    public void setInfo(NodeInfo info) {
        this.info = info;
    }

    public ArrayList<DndTree> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<DndTree> children) {
        this.children = children;
    }
}
