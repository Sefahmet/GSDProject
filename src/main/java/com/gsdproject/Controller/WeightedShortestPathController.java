package com.gsdproject.Controller;


import com.gsdproject.Entity.Default_Edge;
import com.gsdproject.Service.WeightedShortestPath;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Validated
@RestController
@CrossOrigin
@RequestMapping("/Effortless")
public class WeightedShortestPathController {
    @GetMapping("/Coordinates")

    public ResponseEntity<List<Coordinate>> shortestPathGetter(@Valid @RequestParam double lat1,
                                                               @Valid @RequestParam double lon1,
                                                               @Valid @RequestParam double lat2,
                                                               @Valid @RequestParam double lon2,
                                                               @Valid @RequestParam double wLength,
                                                               @Valid @RequestParam double wSlope,
                                                               @Valid @RequestParam double wMaxSpeed,
                                                               @Valid @RequestParam double wTurnLeft,
                                                               @Valid @RequestParam double wGreenary)  {
        Coordinate p1 = new Coordinate(lat1, lon1);
        Coordinate p2 = new Coordinate(lat2, lon2);
        List<Coordinate> coordinates = WeightedShortestPath.shortestPathService(lat1, lon1, lat2, lon2,
                wLength, wSlope, wMaxSpeed, wTurnLeft,wGreenary,false);


        coordinates.add(0,p1);
        coordinates.add(p2);



        if (coordinates!=null){
            return new ResponseEntity(coordinates, HttpStatus.OK);
        }else{
            return new ResponseEntity("CoordinatesCouldntFind", HttpStatus.BAD_REQUEST);
        }

        }



    @GetMapping("/CoordinatesGreeneryFromSat")
    public ResponseEntity<List<Coordinate>> shortestPathGetterGreeneryFromSat(@Valid @RequestParam double lat1,
                                                               @Valid @RequestParam double lon1,
                                                               @Valid @RequestParam double lat2,
                                                               @Valid @RequestParam double lon2,
                                                               @Valid @RequestParam double wLength,
                                                               @Valid @RequestParam double wSlope,
                                                               @Valid @RequestParam double wMaxSpeed,
                                                               @Valid @RequestParam double wTurnLeft,
                                                               @Valid @RequestParam double wGreenary)  {
        Coordinate p1 = new Coordinate(lat1, lon1);
        Coordinate p2 = new Coordinate(lat2, lon2);
        List<Coordinate> coordinates = WeightedShortestPath.shortestPathService(lat1, lon1, lat2, lon2,
                wLength, wSlope, wMaxSpeed, wTurnLeft,wGreenary,true);


        coordinates.add(0,p1);
        coordinates.add(p2);



        if (coordinates!=null){
            return new ResponseEntity(coordinates, HttpStatus.OK);
        }else{
            return new ResponseEntity("CoordinatesCouldntFind", HttpStatus.BAD_REQUEST);
        }

    }

    }
