package com.kk.bus.android.demo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gemalto.tests.demo.R;
import com.kk.bus.Subscribe;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private Button mButtonPost;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mButtonPost = (Button) rootView.findViewById(R.id.buttonPost);
            mButtonPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonStartClicked();
                }
            });
            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();
            EventBus.register(this);
        }

        @Override
        public void onStop() {
            EventBus.unregister(this);
            super.onStop();
        }

        private void buttonStartClicked() {
            busPost();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Bus test
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        private static class Event {

            private String mMessage;

            private Event(String message) {
                mMessage = message;
            }

            @Override
            public String toString() {
                return mMessage;
            }
        }


        private static class EventSub extends Event {

            private EventSub(String message) {
                super(message);
            }
        }


        private int mGreetingsCounter = 0;

        private void busPost() {
            EventBus.post(new Event("Hello World! " + mGreetingsCounter++));
        }

        @Subscribe
        public void onEvent(Event event) {
            Logger.d("Received: " + event);
            TextView textView = (TextView) getActivity().findViewById(R.id.textView);
            textView.setText(event.toString());
        }
    }
}
