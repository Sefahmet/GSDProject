package com.gsdproject.DataHolder;

import com.gsdproject.Test;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.index.strtree.STRtree;

@AllArgsConstructor
public class GreenaryData {
    @Getter @Setter private STRtree buildings;
    @Getter @Setter private STRtree tree;
    private static GreenaryData instance;

    public static GreenaryData getInstance() throws Exception {
        if (instance == null){
            instance = Test.getGreenaryData();
        }
        return instance;
    }

}
