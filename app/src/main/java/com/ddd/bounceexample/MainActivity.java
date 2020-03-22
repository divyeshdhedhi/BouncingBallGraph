package com.ddd.bounceexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private TextView tv_coeff;
    private LineChart lineChart;
    private EditText et_height;
    float coeff = 0.0f;
    TextView tv_bounces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            AppCompatSeekBar seekBar = findViewById(R.id.sb_coeff);
            seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

            tv_coeff = findViewById(R.id.tv_coeff);
            tv_coeff.setText("Coefficient of Restitution : 0.0");

            et_height = findViewById(R.id.et_height);
            tv_bounces = findViewById(R.id.tv_bounces);


            {   // // Chart Style // //
                lineChart = findViewById(R.id.chart);
                lineChart.setTouchEnabled(true);
                lineChart.setPinchZoom(true);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                Description description = new Description();
                description.setText("Time");
                lineChart.setDescription(description);



                //submit button
                Button btn_submit = findViewById(R.id.btn_submit);
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (coeff >= 1 || coeff < 0) {
                            Toast.makeText(MainActivity.this, "Please select valid Coefficient of Restitution", Toast.LENGTH_SHORT).show();
                        } else {
                            if (TextUtils.isEmpty(et_height.getText().toString())) {
                                Toast.makeText(MainActivity.this, "Please enter valid height", Toast.LENGTH_SHORT).show();
                            } else {
                                float h = Float.parseFloat(et_height.getText().toString());
                                Utils.AsyncTaskBounce asyncTask = new Utils.AsyncTaskBounce(MainActivity.this, h, coeff, lineChart, tv_bounces);
                                asyncTask.execute();
                            }
                        }
                    }
                });
            }
        } catch (Exception e){
            Utils.logException(MainActivity.class, "OnCreate", e);
        }
    }

    AppCompatSeekBar.OnSeekBarChangeListener seekBarChangeListener = new AppCompatSeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tv_coeff.setText("Coefficient of Restitution: " + getConvertedValue(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    public String getConvertedValue(int intVal){
        float val;
        val = .1f * intVal;
        if(String.valueOf(val).equalsIgnoreCase("0.90000004")){
            coeff = 0.9f;
            return "0.9";
        }
        else{
            coeff = val;
            return String.valueOf(val);
        }
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOW HIGH", "low: " + lineChart.getLowestVisibleX() + ", high: " + lineChart.getHighestVisibleX());
        Log.i("MIN MAX", "xMin: " + lineChart.getXChartMin() + ", xMax: " + lineChart.getXChartMax() + ", yMin: " + lineChart.getYChartMin() + ", yMax: " + lineChart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }
}
