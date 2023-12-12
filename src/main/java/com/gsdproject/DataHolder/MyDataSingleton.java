package com.gsdproject.DataHolder;

import com.gsdproject.Entity.GraphFeatures;
import com.gsdproject.Entity.Weight;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

public class MyDataSingleton {
    @Getter @Setter private GraphFeatures graphFeatures;
    @Getter @Setter private Weight weight;

    @Autowired
    public MyDataSingleton()  {
        try {
            this.graphFeatures = GraphFeatures.getInstance();
            this.weight = Weight.getInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void update(){

    }
}
