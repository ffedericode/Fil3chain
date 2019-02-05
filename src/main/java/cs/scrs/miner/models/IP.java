package cs.scrs.miner.models;

import java.util.Objects;



public class IP implements Cloneable{
    private String user_ip;

    public String getIp() {
        return user_ip;
    }

    public void setIp(String ip) {
        this.user_ip = ip;
    }

    public IP(String ip) {
        this.user_ip = ip;
    }
    
    @Override
    protected Object clone(){
        return new IP(this.toString());
    }
    
    @Override
    public String toString() {
        return "{\"user_ip\":\""+user_ip+"\"}";
    }

    @Override 
    public boolean equals(Object o){
        return user_ip.equals(((IP)o).getIp());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.user_ip);
        return hash;
    }
}
