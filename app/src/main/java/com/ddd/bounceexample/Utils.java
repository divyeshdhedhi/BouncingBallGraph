package com.ddd.bounceexample;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class Utils {

    private static ArrayList<Entry> dataValues = new ArrayList<>();

    public static void plotBounces(Context context, float height, float coefficient, final LineChart lineChart, TextView tv_bounces){
        dataValues.clear();
        lineChart.invalidate();

        int loopCount = 0;
        int bounces = 0;
        float y = 0.0f;

        dataValues.add(new Entry(0, height));
        float time = (float) Math.sqrt((2 * height) / 9.8);
        dataValues.add(new Entry(time, y));

        Log.d("ddddd", "count: " + loopCount + " bounces: " + bounces + " Height: " + height + " time: " + time);

        int i = 2;
        while(height > 0.000001){
            if (i % 2 == 0){
                bounces = bounces + 1;
                height = height * (coefficient * coefficient);
            }

            time = time + (float) Math.sqrt((2 * height) / 9.8);

            if(i % 2 == 0){
                y = height;
            } else y = 0.0f;

            dataValues.add(new Entry(time, y));

            i++;

            Log.d("ddddd", "count: " + loopCount + " bounces: " + bounces + " Height: " + height + " time: " + time);
        }

        LineDataSet lineDataSet = new LineDataSet(dataValues, "");
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setColor(context.getResources().getColor(R.color.primaryText));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lineChart.animateX(5000, Easing.EaseOutBounce);
            }
        });
        lineChart.invalidate();

        tv_bounces.setText("Number of bounces = " + bounces + "\nPlease pinch to zoom to view smaller bounces");
    }

    public static class AsyncTaskBounce extends AsyncTask<String, String, String> {

        Context context;
        float height;
        float coefficient;
        LineChart lineChart;
        TextView tv_bounces;

        AsyncTaskBounce(Context context, float height, float coefficient, LineChart lineChart, TextView tv_bounces){
            this.context = context;
            this.height = height;
            this.coefficient = coefficient;
            this.lineChart = lineChart;
            this.tv_bounces = tv_bounces;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(context, "Please wait", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                plotBounces(context, height, coefficient, lineChart, tv_bounces);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    static void logException(Class className, String method, Exception e) {
        Log.e(className.getSimpleName() + "->" + method + " Exception: ", e.getMessage());
    }

}
