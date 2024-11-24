package com.example.CaculateFare.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
public class CalculateController {
	
	
	@GetMapping("/calculate")
	public ResponseEntity<Double> calculateFare(@RequestParam String fromLocation, 
			@RequestParam String toLocation, @RequestParam String typeOfCab){
		
		Double fare = fareCalculate(fromLocation,toLocation,typeOfCab);
		
		
		return new ResponseEntity<>(fare,HttpStatus.OK);
	}
	
	public Double fareCalculate(String fromLocation,String toLocation, String typeOfCab) {
		double rate; 
		double fare =0;
		 DecimalFormat df = new DecimalFormat("#.00");
		 
		 //rate of standard cab
		if(typeOfCab.equals("Standard")) {
			rate = 1.0;
			fare = rate * distance(fromLocation,toLocation);
		}
		// rate of luxury cab
		if(typeOfCab.equals("Luxury")) {
			rate = 1.5;
			fare = rate * distance(fromLocation,toLocation);
		}
		
		
		return Double.parseDouble(df.format(fare));//round to two decimal places
	}
	
	//transfer the angle to radius
	public double rad(double d) {
	        return d * Math.PI / 180.0;
	    }
	//calculate the distance by given longitude and latitude
	public double calDistance(double lon1, double lat1, double lon2, double lat2) {
		
		double EARTH_RADIUS = 6378137; //earth radius
		double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        //calculate distance with mile unit
        return 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2))) * EARTH_RADIUS * 0.0006214;
		
		
	}
	public double distance(String fromLocation, String toLocation) {
		
		//put LA coordinate
		Map<String,Double> LAcoordinate = new HashMap<>();
		LAcoordinate.put("latitude", 34.05);
		LAcoordinate.put("longitude", -118.24);
		
		//put SD coordinate
		Map<String,Double> SDcoordinate = new HashMap<>();
		SDcoordinate.put("latitude", 32.72);
		SDcoordinate.put("longitude", -117.16);
		
		//put SF coordinate
		Map<String,Double> SFcoordinate = new HashMap<>();
		SFcoordinate.put("latitude", 37.78);
		SFcoordinate.put("longitude", -122.43);
		
		//put NY coordinate
		Map<String,Double> NYcoordinate = new HashMap<>();
		NYcoordinate.put("latitude", 40.73);
		NYcoordinate.put("longitude", -73.94);
		
		//make all cities coordinate as a Map
		Map<String, Map<String,Double>> citycoordinate = new HashMap<>();
		citycoordinate.put("Los Angeles", LAcoordinate);
		citycoordinate.put("San Diego", SDcoordinate);
		citycoordinate.put("San Francisco", SFcoordinate);
		citycoordinate.put("New York", NYcoordinate);
		
		//get city coordinate
		Map<String, Double> fromCoords = citycoordinate.get(fromLocation);
        Map<String, Double> toCoords = citycoordinate.get(toLocation);
        
        //get both latitude and longitude of fromlocation and tolocation respectively
        double fromLatitude = fromCoords.get("latitude");
        double fromLongitude = fromCoords.get("longitude");
        double toLatitude = toCoords.get("latitude");
        double toLongitude = toCoords.get("longitude");
        
        return calDistance(fromLongitude,fromLatitude,toLongitude,toLatitude);
	}

}
