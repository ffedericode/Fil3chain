package cs.scrs.miner.controllers;

import cs.scrs.config.KeysConfig;
import cs.scrs.miner.dao.block.Block;
import cs.scrs.miner.dao.block.BlockRepository;
import cs.scrs.miner.models.DndTree;
import cs.scrs.miner.models.Filechain;
import cs.scrs.miner.models.NodeInfo;
import cs.scrs.miner.models.WidgetModel;
import cs.scrs.service.connection.ConnectionServiceImpl;
import cs.scrs.service.ip.IPServiceImpl;
import cs.scrs.service.util.Conversions;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@RestController
public class ControllerStatistics {

    @Autowired
    private IPServiceImpl ipService;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private KeysConfig keyProperties;
    @Autowired
    private Filechain filechain;
    @Autowired
    private ConnectionServiceImpl connectionServiceImpl;



    @RequestMapping(value = "/fil3chain/statistics", method = RequestMethod.POST)
    @ResponseBody
    public String statistics(@RequestBody WidgetModel widgetModel) {

        switch (widgetModel.getName()){
            case "ips":
                    return "{\"value\":"+getNumbersConnectedIp()+"}";
            case "blocks":
                    return "{\"value\":"+blockRepository.findAll().size()+"}";
            case "fil3chain":
                return getBlockToDraw(widgetModel.getPage());
            case "mlevel":
                //Attuale chain level massimo
                return "{\"value\":"+blockRepository.findFirstByOrderByChainLevelDesc().getChainLevel()+"}";
            case "power":
                //Potenza media di calcolo dell'hash
                return "{\"value\":"+filechain.getMiningService().getAveragePowerMachine()+"}";

        }
        return "error";
    }


    private Integer getNumbersConnectedIp(){
        connectionServiceImpl.firstConnectToEntryPoint();
        return ipService.getIPList().size();
    }

    private String getBlockToDraw(Integer val){
        if(val==0 || val==null){
            val=1;
        }
        Integer inf=10*(val-1);
        Integer sup=10*(val)-1;

        String myHashKey= DigestUtils.sha256Hex(keyProperties.getPublicKey());
        List<Block> blocks=blockRepository.findByChainLevelBetweenOrderByChainLevelAsc(inf,sup);
        if(blocks.size() == 0)
            return "empty";
        //verifico se ci sono ulteriori nodi
        List<Block> succ = blockRepository.findBychainLevel(sup+1);
        String last;
        if(succ.size()>0){
            last = "false";
        }else{
            last = "true";
        }
        DndTree root;
        // se la pagina è la 1 la root è il blocco 0 altrimenti devo creare un 
        // blocco fittizio
        if(val == 1){
            root = new DndTree("0",new NodeInfo("zeroBlock",last,val+""));
        }else{
            root = new DndTree("0",new NodeInfo("fakeBlock",last,val+""));
        }
        Set<String> hashBlocks = new HashSet<>(); // contiene gli hash dei blocchi recuperati
        //popolo l'insieme
        for(Block b:blocks){
            hashBlocks.add(b.getHashBlock());
        }  
        
        HashMap<String,DndTree> addedNode= new HashMap<>();//contiene i nodi già aggiunti al dndTree
        String father;
        String hashBlock;
        for(Block b: blocks){
            if(b.getHashBlock().equals("0")){
                continue;
            }else{
                father = b.getFatherBlockContainer();
                hashBlock = b.getHashBlock();
                if(myHashKey.equals(b.getUserContainer().getPublicKeyHash())){
                    addedNode.put(hashBlock,new DndTree(hashBlock,new NodeInfo("myBlock")));
                }else{
                    addedNode.put(hashBlock,new DndTree(hashBlock,new NodeInfo("otherBlock")));
                }
                if((father.equals("0"))||(!hashBlocks.contains(father))){
                    root.getChildren().add(addedNode.get(hashBlock));
                }else{
                    addedNode.get(father).getChildren().add(addedNode.get(hashBlock));
                }
            }
        }
        return Conversions.toJson(root);
    }


}

