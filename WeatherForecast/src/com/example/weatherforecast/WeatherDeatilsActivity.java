package com.example.weatherforecast;

import java.text.DecimalFormat;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherDeatilsActivity extends Activity {

	TextView txtDate, txtType, txtHumidity, txtClouds, txtWindSpeed;
	TextView txtDay, txtNight, txtMor, txtEve, txtMax, txtMin;
	ImageView img;

	DayWeatherReport obj = new DayWeatherReport();

	DisplayImageOptions options;
	ImageLoaderConfiguration config;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_weather_details);

		init();

		setDate();
	}

	private void init() {
		txtDate = (TextView) findViewById(R.id.txtDate_WeatherDetails);
		txtType = (TextView) findViewById(R.id.txtType_WeatherDetails);
		txtHumidity = (TextView) findViewById(R.id.txtHumidity_WeatherDetails);
		txtClouds = (TextView) findViewById(R.id.txtClouds_WeatherDetails);
		txtWindSpeed = (TextView) findViewById(R.id.txtWindSpeed_WeatherDetails);

		txtDay = (TextView) findViewById(R.id.txtTpDay_WeatherDetails);
		txtNight = (TextView) findViewById(R.id.txtTpNight_WeatherDetails);
		txtMor = (TextView) findViewById(R.id.txtTpMor_WeatherDetails);
		txtEve = (TextView) findViewById(R.id.txtTpEve_WeatherDetails);
		txtMax = (TextView) findViewById(R.id.txtTpMax_WeatherDetails);
		txtMin = (TextView) findViewById(R.id.txtTpMin_WeatherDetails);

		img = (ImageView) findViewById(R.id.img_WeatherDetails);

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).build();

		config = new ImageLoaderConfiguration.Builder(
				WeatherDeatilsActivity.this)
				.defaultDisplayImageOptions(options)
				.memoryCacheExtraOptions(1200, 1000).build();
		ImageLoader.getInstance().init(config);

	}

	private void setDate() {

		obj = (DayWeatherReport) getIntent().getSerializableExtra("obj");

		ImageLoader.getInstance().displayImage(
				"http://openweathermap.org/img/w/" + obj.imgNm + ".png", img);

		txtDate.setText(obj.reportDate);
		txtType.setText(obj.typeName);

		txtHumidity.setText("Humidity   : " + obj.humidity + "%");
		txtWindSpeed.setText("Wind Speed : " + obj.windSpeed);
		txtClouds.setText("Clouds     : " + obj.clouds + " " + obj.cloudsAll+ "%");
	
		txtDay.setText("Day     : " + new DecimalFormat("##.##").format(obj.tpDay) + " \u2103");
		txtNight.setText("Night   : " + new DecimalFormat("##.##").format(obj.tpNight) + " \u2103");
		txtMor.setText("Morning : " + new DecimalFormat("##.##").format(obj.tpMor) + " \u2103");
		txtEve.setText("Evening : " + new DecimalFormat("##.##").format(obj.tpEve) + " \u2103");
		txtMax.setText("Maximum : " + new DecimalFormat("##.##").format(obj.tpMax) + " \u2103");
		txtMin.setText("Minimum : " + new DecimalFormat("##.##").format(obj.tpMin) + " \u2103");

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	
		
		WeatherDeatilsActivity.this.finish();
	}

}
