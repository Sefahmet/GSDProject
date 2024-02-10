package com.gsdproject.Entity;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
public class Weight {
    /*This weight are representing users selections. These are not edge weight.*/

    @Getter @Setter private  Double length_weight;
    @Getter @Setter private  Double slope_weight;
    @Getter @Setter private  Double max_speed_weight;
    @Getter @Setter private  Double turning_cost_weight;
    @Getter @Setter private  Double greenary_weight;
    @Getter @Setter private  Boolean isGreeneryFromSat;
   @Setter private static Weight instance;
    public static Weight getInstance() {
        if (instance == null){
            instance = new Weight(0.2,0.2,0.2,0.2,0.2,false);
        }
        return instance;
    }

    public Weight(Double length_weight, Double slope_weight, Double max_speed_weight, Double turning_cost_weight,Double greenary_weight,boolean isGreeneryFromSat) {
        double sumWeights = length_weight + slope_weight + max_speed_weight + turning_cost_weight + greenary_weight;
        if(length_weight + slope_weight + max_speed_weight + greenary_weight == 0 && turning_cost_weight>0){
            this.length_weight = 0.0;
            this.slope_weight = 0.0;
            this.max_speed_weight = 0.0;
            this.turning_cost_weight = 0.99;
            this.greenary_weight = 0.01;
            this.isGreeneryFromSat = isGreeneryFromSat;
        }
        else if(sumWeights!= 0){
            this.length_weight = length_weight/sumWeights;
            this.slope_weight = slope_weight/sumWeights;
            this.max_speed_weight = max_speed_weight/sumWeights;
            this.turning_cost_weight = turning_cost_weight/sumWeights;
            this.greenary_weight =greenary_weight/sumWeights;
            this.isGreeneryFromSat = isGreeneryFromSat;
        }else{
            this.length_weight = 0.2;
            this.slope_weight = 0.2;
            this.max_speed_weight = 0.2;
            this.turning_cost_weight = 0.2;
            this.greenary_weight = 0.2;
            this.isGreeneryFromSat = isGreeneryFromSat;

        }

    }
    public boolean isSameWeight(Weight w){
        if(!this.turning_cost_weight.equals(w.getTurning_cost_weight())){

            return false;
        }else if(!this.greenary_weight.equals(w.getGreenary_weight())){
            return false;
        }else if(!this.length_weight.equals(w.getLength_weight())){
            return false;
        }else if(!this.slope_weight.equals(w.getSlope_weight())){
            return false;
        }else if(!this.max_speed_weight.equals(w.getMax_speed_weight())){

            return false;
        }else if(!((this.isGreeneryFromSat & w.getIsGreeneryFromSat())|(!this.isGreeneryFromSat & !w.getIsGreeneryFromSat()))){

            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public String toString() {
        return "Weight{" +
                "length_weight=" + length_weight +
                ", slope_weight=" + slope_weight +
                ", max_speed_weight=" + max_speed_weight +
                ", turning_cost_weight=" + turning_cost_weight +
                ", greenary_weight=" + greenary_weight +
                ", isGreenreyFromSat=" + isGreeneryFromSat +
                '}';
    }
}
