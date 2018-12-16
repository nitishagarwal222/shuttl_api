package com.org.shuttl.api;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class APITest {

	//This data provider gives the city name and boolean value to tell if it is going to rain or not. True for rain and false for not going to rain
	@DataProvider(name = "weather1")
	public static Object[][] init1() {

		return new Object[][] { { "Delhi", true }, { "Srinagar", false },{"Paris, FR",true},{"Dallas, TX, US",true} };
	}
	
	//This is used to test wrong city name given to the api. Expected result is throw IncorrectCityException
	@DataProvider(name = "weather2")
	public static Object[][] init2() {

		return new Object[][] { { "nag"}, { "dsjf, jsfdj" },{"Sri Lanka"},{"US"},{"TX"} };
	}

	//Extracting data from api and verifying with canned source of truth
	@Test(dataProvider = "weather1")
	public void test1(String city, boolean cannedData) {

		Response r = Weather.getResponse(city); //Taking response from api

		boolean result = Weather.getRainCoindition(r, city);// Taking result if it is going to rain

		Assert.assertEquals(cannedData, result);
	}
	
	//IncorrectCityException is expected for this data provider
	@Test(dataProvider = "weather2",expectedExceptions=IncorrectCityException.class)
	public void test2(String city) {

		Response r = Weather.getResponse(city);

		Weather.getRainCoindition(r, city);

	}
}
