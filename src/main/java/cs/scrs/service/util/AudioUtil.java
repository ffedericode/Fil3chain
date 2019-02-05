/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs.scrs.service.util;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.InputStream;

/**
 *
 * @author simone
 */
public class AudioUtil {
    
    public static void alert(){
        try{
            InputStream in = AudioUtil.class.getResourceAsStream("/clip/airhorn_3.au");
            //System.out.println(in.available());
            AudioStream audioStream = new AudioStream(in);
            AudioPlayer.player.start(audioStream);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("problemi con la tromba");
        }
    }
    
}
