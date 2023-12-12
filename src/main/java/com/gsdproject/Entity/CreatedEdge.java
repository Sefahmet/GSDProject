package com.gsdproject.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class CreatedEdge {

    @Getter @Setter private String id;
    @Getter @Setter private Default_Edge incomingEdge;
    @Getter @Setter private Default_Edge outgoingEdge;
    @Getter @Setter private String turningCost;

    public CreatedEdge(Default_Edge incomingEdge,Default_Edge outgoingEdge){
        this.id = incomingEdge.getId() +" _ "+ outgoingEdge.getId();
        this.incomingEdge = incomingEdge;
        this.outgoingEdge = outgoingEdge;
        this.turningCost = "No_Turns";
    }

    @Override
    public String toString() {
            return id + " "+incomingEdge.getOsmid()+" "+ outgoingEdge.getOsmid()+ " "+turningCost;
            }
            }
