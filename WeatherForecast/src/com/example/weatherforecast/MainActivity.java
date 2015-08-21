package com.example.weatherforecast;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.xmlReader.XmlElement;
import com.example.xmlReader.XmlParser;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	RelativeLayout btnGetLocation;

	EditText edtLocation;

	ListView listWeather;

	GPSTracker gpsTracker;

	ArrayList<DayWeatherReport> arrReports = new ArrayList<DayWeatherReport>();
	WeatherAdapter adapter;

	String latitude, longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);

		// For hide keypad and open keypad when on edittect click
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		init();

		getCurrentLatitudeLongitude();

		btnGetLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String city = edtLocation.getText().toString();
				if (city.equals(null) || city.equals("")) {
					edtLocation.setError("Please Enter City name");
				} else {

					// For hide keypad and open keypad when on edittect click
					getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

					getLatitudeLongitudebyCity(city);
				}

			}
		});

		edtLocation
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							performSearch();
							return true;
						}
						return false;
					}

					private void performSearch() {
						String city = edtLocation.getText().toString();
						if (city.equals(null) || city.equals("")) {
							edtLocation.setError("Please Enter City name");
						} else {

							// For hide keypad and open keypad when on edittect
							// click
							getWindow()
									.setSoftInputMode(
											WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

							getLatitudeLongitudebyCity(city);
						}

					}
				});

	}

	private void init() {
		btnGetLocation = (RelativeLayout) findViewById(R.id.btnGetLocation);

		edtLocation = (EditText) findViewById(R.id.edtSearchLocation);

		listWeather=(ListView)findViewById(R.id.listDayWeathers);

		gpsTracker = new GPSTracker(MainActivity.this);

		arrReports = new ArrayList<>();

	}

	class ReadXmlWeatherData extends AsyncTask<Void, Void, Void> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(MainActivity.this, "", "Loading...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				arrReports = new ArrayList<>();

				XmlElement XMLFileElement;

				String fetchUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?lat="
						+ latitude
						+ "&lon="
						+ longitude
						+ "&cnt=14&mode=xml&unit=metrics&APPID="
						+ getResources().getString(R.string.weather_web_key);

				System.out.println("URL :"+fetchUrl);
				
				URL url = new URL(fetchUrl);
				URI uri = url.toURI();
				// Get the main tag of xml
				XMLFileElement = new XmlParser().parse(uri.toString());
				// System.out.println("File  : "+XMLFileElement.getKey());

				// get all elements into array from main tag of xml
				ArrayList<XmlElement> dayElements = XMLFileElement
						.getElements();

				XmlElement aa = dayElements.get(0);
				System.out.println("CITY : "
						+ aa.getElements().get(0).getValue());

				XmlElement forecastXmlElement = dayElements.get(4);

				// get Forecast elements into array from forecast of xml
				ArrayList<XmlElement> arrForecastElements = forecastXmlElement
						.getElements();
				// System.out.println("forecast Size : "+arrForecastElements.size());

				for (int i = 0; i < arrForecastElements.size(); i++) {

					DayWeatherReport obj = new DayWeatherReport();

					XmlElement xmlforcastMain = arrForecastElements.get(i);

					obj.reportDate = xmlforcastMain.getAttributes().get("day");

					ArrayList<XmlElement> xmlReport = xmlforcastMain
							.getElements();

					XmlElement xmlName = xmlReport.get(0);
					obj.typeName = xmlName.getAttributes().get("name");
					obj.imgNm = xmlName.getAttributes().get("var");

					XmlElement xmlWindspeed = xmlReport.get(3);
					obj.windSpeed = xmlWindspeed.getAttributes().get("mps");
					obj.windSpeedNm = xmlWindspeed.getAttributes().get("name");

					// Getting temp by converting Kelvin to celsius
					XmlElement xmlTemp = xmlReport.get(4);
					obj.tpDay = (Double.parseDouble(xmlTemp.getAttributes()
							.get("day"))) - 273;
					obj.tpMin = (Double.parseDouble(xmlTemp.getAttributes()
							.get("min"))) - 273;
					obj.tpMax = (Double.parseDouble(xmlTemp.getAttributes()
							.get("max"))) - 273;
					obj.tpNight = (Double.parseDouble(xmlTemp.getAttributes()
							.get("night"))) - 273;
					obj.tpEve = (Double.parseDouble(xmlTemp.getAttributes()
							.get("eve"))) - 273;
					obj.tpMor = (Double.parseDouble(xmlTemp.getAttributes()
							.get("morn"))) - 273;

					XmlElement xmlHumidity = xmlReport.get(6);
					obj.humidity = xmlHumidity.getAttributes().get("value");

					XmlElement xmlClouds = xmlReport.get(7);
					obj.clouds = xmlClouds.getAttributes().get("value");
					obj.cloudsAll = xmlClouds.getAttributes().get("all");

					// System.out.println("----------------------------------------------");

					arrReports.add(obj);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dialog.dismiss();

			adapter = new WeatherAdapter(MainActivity.this,
					R.layout.view_weather_list_item, arrReports);
			listWeather.setAdapter(adapter);
		}

	}

	public void getCurrentLatitudeLongitude() {
		// check if GPS enabled
		if (gpsTracker.canGetLocation()) {
			latitude = gpsTracker.getLatitude() + "";
			longitude = gpsTracker.getLongitude() + "";
			// Toast.makeText(getApplicationContext(),
			// "Your Location is - \nLat: " + latitude + "\nLong: " + longitude,
			// Toast.LENGTH_LONG).show();

			// Getting Weather Report According to current city
			new ReadXmlWeatherData().execute();

		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gpsTracker.showSettingsAlert();
		}
	}

	public void getLatitudeLongitudebyCity(String city) {

		if (Geocoder.isPresent()) {
			try {
				String location = city;
				Geocoder gc = new Geocoder(this);
				List<Address> addresses = gc.getFromLocationName(location, 5); // get
																				// the
																				// found
																				// Address
																				// Objects

				Address a = addresses.get(0);
				latitude = a.getLatitude() + "";
				longitude = a.getLongitude() + "";

				// System.out.println("URL :"+"http://api.openweathermap.org/data/2.5/forecast/daily?lat="+latitude+"&lon="+longitude+"&cnt=10&mode=xml&unit=metrics&APPID=e7b46621da7a981fe7675327771afa6d");

				// Getting Weather Report According to city
				new ReadXmlWeatherData().execute();

			} catch (Exception e) {
				// handle the exception
			}
		}

	}

}
