package com.gsdproject.Entity;

import lombok.Getter;
import lombok.Setter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;

import static com.gsdproject.Model.GraphCreator.getGraphFeatrues;

public class GraphFeatures {
    @Getter @Setter private Graph<Default_Edge, DefaultWeightedEdge> graph;
    @Getter @Setter private HashMap<Long, Default_Node> nodeHashMap;
    @Getter @Setter private HashMap<String, Default_Edge> edgeHashMap;
    @Getter @Setter private HashMap<String, CreatedEdge> createdEdgesHashMap;
    @Setter private static GraphFeatures instance;

    public static GraphFeatures getInstance() throws Exception {
        if (instance == null){
            System.out.println("\n\nFiles Readed\n\n");
            instance = getGraphFeatrues();
        }
        return instance;
    }

}
