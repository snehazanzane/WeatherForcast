package com.example.weatherforecast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherAdapter extends ArrayAdapter<DayWeatherReport> {

	private Context context;
	private int resource;
	private LayoutInflater inflater;

	ArrayList<DayWeatherReport> arr;

	// ImageLoader imageLoader;
	DisplayImageOptions options;
	ImageLoaderConfiguration config;

	class WeatherViewHolder {
		ImageView imgWeather;
		TextView txtDate, txtType, txtMinTemp, txtMaxTemp, txtcloud;
		//TextView txtDate, txtType, txtMinTemp, txtMaxTemp, txtWindSpeed;
	}

	public WeatherAdapter(Context context, int resource,
			ArrayList<DayWeatherReport> objects) {
		super(context, resource, objects);
		this.context = context;
		this.resource = resource;
		arr = objects;

		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).build();

		config = new ImageLoaderConfiguration.Builder(getContext())
				.defaultDisplayImageOptions(options)
				.memoryCacheExtraOptions(1200, 1000).build();
		ImageLoader.getInstance().init(config);

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;

		WeatherViewHolder holder = new WeatherViewHolder();

		if (convertView == null) {

			view = inflater.inflate(resource, null);

			holder.imgWeather = (ImageView) view
					.findViewById(R.id.imgWeather_WeatherListItem);

			holder.txtDate = (TextView) view
					.findViewById(R.id.txtDate_WeatherListItem);
			holder.txtType = (TextView) view
					.findViewById(R.id.txtCondition_WeatherListItem);

			holder.txtMaxTemp = (TextView) view
					.findViewById(R.id.txtMaxTemp_WeatherListItem);
			holder.txtMinTemp = (TextView) view
					.findViewById(R.id.txtMinTemp_WeatherListItem);

			holder.txtcloud = (TextView) view
					.findViewById(R.id.txtClouds_WeatherListItem);

			view.setTag(holder);
		} else {
			holder = (WeatherViewHolder) convertView.getTag();
		}

		// Setting data to holder
		holder.txtDate.setText(arr.get(position).reportDate);
		holder.txtType.setText(arr.get(position).typeName);

		holder.txtMaxTemp.setText(new DecimalFormat("##.##").format(arr.get(position).tpMax) + " \u2103");
		holder.txtMinTemp.setText(new DecimalFormat("##.##").format(arr.get(position).tpMin) + " \u2103");

		holder.txtcloud.setText(arr.get(position).clouds+" "+arr.get(position).cloudsAll+"%");

		String imgUrl = "http://openweathermap.org/img/w/"
				+ arr.get(position).imgNm + ".png";

		ImageLoader.getInstance().displayImage(imgUrl, holder.imgWeather);

		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						WeatherDeatilsActivity.class);
				intent.putExtra("obj", arr.get(position));
				context.startActivity(intent);
			}
		});

		return view;
	}

}
