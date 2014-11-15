package cmsc434.doyleedwardsclock;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;


public class ClockActivity extends Activity {

    final Handler handler = new Handler();
    private ClockView view;
    private Integer[] colors = new Integer[87120];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create colors based on http://stackoverflow.com/questions/14274871/java-for-loop-iterate-and-list-colors
        SortedMap<Long, Integer> colours = new TreeMap<Long, Integer>(Collections.reverseOrder());
        for (int r = 0; r < 45; r++) {
            for (int g = 0; g < 44; g++) {
                for (int b = 0; b < 44; b++) {
                    long brightness = (r * 2 + g + b * 3) / 3;
                    long colourScore = (brightness << 24) + (r * 2 << 8) + (g << 16) + b * 3;
                    colours.put(colourScore, Color.rgb(r * 4, g, b * 2));
                }
            }
        }

        colours.values().toArray(colors);

        setContentView(R.layout.activity_clock);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        view = (ClockView) findViewById(R.id.clock_view);

        handler.post(new Runnable(){

            @Override
            public void run() {

                Time t = new Time();
                t.setToNow();

                int seconds = ((t.hour * 60) + t.minute * 60) + t.second;

                view.setPaintColor(colors[seconds]);
                Log.i("Color", colors[seconds] + "");
                view.invalidate();
                handler.postDelayed(this,1000); // set time here to refresh textView
            }

        });
    }

}
