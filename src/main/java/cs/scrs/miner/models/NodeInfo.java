/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs.scrs.miner.models;

public class NodeInfo {
    private String style;
    private String last;
    private String page;
    
    public NodeInfo(String s,String l, String p){
        style=s;
        last = l;
        page=p;
    }
    
    public NodeInfo(String s){
        style=s;
    }
}
