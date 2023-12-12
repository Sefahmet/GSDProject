package com.gsdproject.Model;

import com.gsdproject.Entity.CreatedEdge;
import com.gsdproject.Entity.Default_Edge;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EdgeCreator {
    public static HashMap<String,CreatedEdge>  isItTurnLeft(Default_Edge incoming, List<Default_Edge> outgoings,
                                    List<Default_Edge> incomings,HashMap<String,CreatedEdge> createdEdgeHashMap) throws Exception {
        // Check the points degree are 2(in non-directed case)
        if (outgoings.size() ==1 && incomings.size() == 1 && incomings.get(0).getId() == outgoings.get(0).getId()){
            // Degree is 2 in that case
            createdEdgeHashMap = creatEdge(incoming,outgoings.get(0),"No_Turns",createdEdgeHashMap);

        }

        // if there is no income, there is no turning cost
        else if (outgoings.size()>=1 & incomings.isEmpty()){
            if (outgoings.size() == 1){
                createdEdgeHashMap =creatEdge(incoming,outgoings.get(0),"No_Turns",createdEdgeHashMap);
            }
            else{
                for(Default_Edge outgoing :outgoings){
                    createdEdgeHashMap =creatEdge(incoming,outgoing,"No_Income",createdEdgeHashMap);
                }
            }
        }

        /*else if(isAllNonConsideredEdge(incoming,outgoings,incomings)){
            for (Default_Edge outgoing: outgoings) {
                createdEdgeHashMap =creatEdge(incoming,outgoing,"Not_Considered",createdEdgeHashMap);
            }
        }*/
        else{
            Map<Default_Edge, Double> outgoingOrderedAngle = incoming.getOrderedAngles(outgoings);
            Map<Default_Edge, Double> incomingOrderedAngles = incoming.getOrderedAngles(incomings,true);
            Double min_incoming_angle = Double.MAX_VALUE;
            for(Double value : incomingOrderedAngles.values()){
                if (value<min_incoming_angle){
                    min_incoming_angle = value;
                }
            }

            for(Default_Edge outgoingEdge:outgoingOrderedAngle.keySet()){
                Double angle = outgoingOrderedAngle.get(outgoingEdge);
                if (180<angle & angle<340 & angle>min_incoming_angle){
                    createdEdgeHashMap =creatEdge(incoming,outgoingEdge,"Left",createdEdgeHashMap);

                }else{
                    createdEdgeHashMap =creatEdge(incoming,outgoingEdge,"Right",createdEdgeHashMap);
                }
            }
        }
        return createdEdgeHashMap;

    }
    private static boolean isAllNonConsideredEdge(Default_Edge incoming, List<Default_Edge> outgoings, List<Default_Edge> incomings) {
        List<String> nonConsideredRoadType = Arrays.asList("service", "pedestrian","path","cycleway");
        if(!nonConsideredRoadType.contains(incoming.getRoadType())){
            return false;
        }
        for(Default_Edge edge:outgoings){
            if(!nonConsideredRoadType.contains(edge.getRoadType())){
                return false;
            }
        }
        for(Default_Edge edge:incomings){
            if(!nonConsideredRoadType.contains(edge.getRoadType())){
                return false;
            }
        }

        return true;

    }
    private static HashMap<String,CreatedEdge> creatEdge(Default_Edge incoming, Default_Edge outgoing,String tag,HashMap<String,CreatedEdge> createdEdgeHashMap) throws Exception {
        try{
            String inc = incoming.getU_id() + " " + incoming.getV_id();
            String out = outgoing.getU_id() + " " + outgoing.getV_id();
            CreatedEdge created_edge = new CreatedEdge(inc + "_" + out, incoming,
                                                        outgoing, tag);
            createdEdgeHashMap.put(inc + "_" + out,created_edge);
            return createdEdgeHashMap;

        }catch (Exception e){
            e.printStackTrace();
        }
        throw new Exception("edge couldnt create");
    }

}
