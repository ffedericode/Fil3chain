package cs.scrs.miner.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import cs.scrs.miner.models.IP;
import cs.scrs.service.ip.IPServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Christian on 24/06/2016.
 */

@Component
@RestController
public class ControllerPDEP {

    @Autowired
    private IPServiceImpl ipService;


    // Controller che intercetta la connessione di un utente
    @RequestMapping(value = "/user_connect", method = RequestMethod.POST)
    public void newUserConnection(@RequestBody String ip) {
        JsonObject jobj = new Gson().fromJson(ip, JsonObject.class);
        String ipHost = jobj.get("user_ip").getAsString();
        if(ipService.indexOf(ipHost)==-1)
            ipService.addIP(new IP(ipHost));
        System.out.println("IP connect:");
        ipService.getIPList().forEach(i ->
         System.out.println(i));
    }

    // Controller che intercetta la disconnessione di un utente
    @RequestMapping(value = "/user_disconnect", method = RequestMethod.POST)
    public void newUserDisconnection(@RequestBody String ip) {
        JsonObject jobj = new Gson().fromJson(ip, JsonObject.class);
        String ipHost = jobj.get("user_ip").getAsString();
        ipService.removeIP(new IP(ipHost));
        // IPManager.getManager().getIPList().forEach(i ->
        // System.out.println(i));
    }

    // Controller che intercetta i ping
    @RequestMapping(value = "/user_ping", method = RequestMethod.POST)
    public @ResponseBody
    String user_ping() { // response body fa si che il
        // return diventa campo dati
        // della risposta
        return "{\"response\":\"ACK\"}";
    }


}
