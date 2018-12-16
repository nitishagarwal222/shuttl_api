package com.org.shuttl.api;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.response.Response;

public class Weather {

	public static Response getResponse(String city) {
		
		//Calling the api

		Response r = given().param("format", "json").param("q",
				"select item.forecast.code from weather.forecast where woeid in (select woeid from geo.places(1) where text=\""
						+ city + "\")")
				.when().get("https://query.yahooapis.com/v1/public/yql");
		return r;
	}

	public static boolean getRainCoindition(Response r, String city) {
		List<String> rain = new ArrayList<String>();// prepared list of codes returned when there is going be rain, we
													// configure more such list to check different weather conditions
		rain.add("5");
		rain.add("6");
		rain.add("8");
		rain.add("9");
		rain.add("10");
		rain.add("11");
		rain.add("12");
		rain.add("35");
		rain.add("39");//there is bug in yahoo documentation which showd code 40 but API is returning 39 as there is one description is repeated 2 times

		int status = r.getStatusCode();
		int count = r.jsonPath().getInt("query.count");
		if (status != 200) {
			System.out.println("API request not successful/server down");//Logging server down or bad request
		}
		else if(count == 0) {
			System.out.println(city +" is not a valid city name");//Logging incorrect name of the city
			throw new IncorrectCityException("City name "+city+" is not a valid city");
		}
		else
		{
			List<String> code = r.jsonPath().getList("query.results.channel.item.forecast.code");
			code.remove(9);//deleting entry for extra days other then 7
			code.remove(8);

			// System.out.println(r);
			code.retainAll(rain);
			if (code.size() > 0) {
				System.out.println("It will be rain in " + city + " in next 7 days");//Logging if it is going to rain
				return true;
			} else
				System.out.println("It will not be any rain in " + city + " in next 7 days");// Logging if it is not going to rain
		}

		return false;

	}
	

}
