/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose Tools | Templates and open the template in the editor.
 */
package cs.scrs.miner.dao.citations;


import javax.persistence.*;
import java.io.Serializable;



@Entity
@Table(name = "citation")
public class Citation {

    
    // Columns
    @EmbeddedId()
    private Key key;
    
    public Citation(){
        super();
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    
    
    @Embeddable
    public static class Key implements Serializable{
        
        @Column(name = "hashCiting")  
        private String hashCiting; //hash di chi cita
    
        @Column(name = "hashCited")
        private String hashCited; //hash di chi viene citato
        
        protected Key(){}
        
        public String getHashCiting() {
            return hashCiting;
        }

        public void setHashCiting(String hashCiting) {
            this.hashCiting = hashCiting;
        }

        public String getHashCited() {
            return hashCited;
        }

        public void setHashCited(String hashCited) {
            this.hashCited = hashCited;
        }

        @Override
        public String toString() {
            return "{\"hashCiting\":\"" + hashCiting + "\",\"hashCited\":\"" + hashCited + "\"}";
        }
    }

    @Override
    public String toString() {
        return "{\"key\":" + key +"}";
    }
}