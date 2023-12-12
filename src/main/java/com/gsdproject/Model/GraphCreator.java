package com.gsdproject.Model;

import com.gsdproject.Entity.GraphFeatures;
import com.gsdproject.Service.Reader;

public class GraphCreator {
    public static GraphFeatures getGraphFeatrues() throws Exception {
        GraphFeatures graphFeatures = Reader.readTheDefaultFiles();


        return graphFeatures;
    }


}
