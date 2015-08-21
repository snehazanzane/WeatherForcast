package com.example.weatherforecast;

import java.io.Serializable;

public class DayWeatherReport implements Serializable{
	
	String typeName,reportDate,imgNm;
	
	double tpDay,tpMin,tpMax,tpNight,tpEve,tpMor;
	
	String humidity;
	
	String clouds;
	String cloudsAll;
	
	String windSpeed,windSpeedNm;
	
}
