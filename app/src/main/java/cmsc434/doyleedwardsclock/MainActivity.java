package cmsc434.doyleedwardsclock;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends Activity {

    private boolean twelveHourEnabled;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onToggleClicked(View view) {
        twelveHourEnabled = ((ToggleButton) view).isChecked();
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(getString(R.string.twelve_hour_enabled), twelveHourEnabled);
        editor.commit();
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        twelveHourEnabled = sp.getBoolean(getString(R.string.twelve_hour_enabled), false);
        ToggleButton tb = (ToggleButton)findViewById(R.id.toggleButton);
        tb.setChecked(twelveHourEnabled);

        handler.post(new Runnable(){

            @Override
            public void run() {

                Time t = new Time();
                t.setToNow();

                TextView month = (TextView)findViewById(R.id.month);
                TextView day = (TextView)findViewById(R.id.day);
                TextView year = (TextView)findViewById(R.id.year);

                month.setText(String.format("%02d", t.month + 1));
                day.setText(String.format("%02d", t.monthDay));
                year.setText((t.year % 100) + "");

                TextView hour = (TextView)findViewById(R.id.hour);
                TextView minute = (TextView)findViewById(R.id.minute);
                TextView second = (TextView)findViewById(R.id.second);

                if(!twelveHourEnabled) {
                    hour.setText(String.format("%02d", t.hour));
                } else {
                    if(t.hour == 0)
                        hour.setText(String.format("%02d", t.hour + 12));
                    else if(t.hour > 12)
                        hour.setText(String.format("%02d", t.hour - 12));
                }
                minute.setText(String.format("%02d", t.minute));
                second.setText(String.format("%02d", t.second));

                handler.postDelayed(this,1000); // set time here to refresh textView
            }

        });
    }
}
