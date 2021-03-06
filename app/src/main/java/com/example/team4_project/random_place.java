package com.example.team4_project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class random_place extends AppCompatActivity {

    String PLACE_SEARCH_ENDPOINT = "https://maps.googleapis.com/maps/api/place/textsearch/json";
    String API_KEY = "AIzaSyB2Ul315SoD2GRH-xCMvpYUQ3sOlsx7u-Q";
    String apiCharset = StandardCharsets.UTF_8.name();
    Gson gson = new Gson();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_place);

        this.setTitle("Rando");
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setCustomView(R.layout.action_bar);

        Button getRandomButton = (Button) findViewById(R.id.random_button);

        getRandomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAsyncActivity();
            }
        });

        ImageView userDataButton = (ImageView) findViewById(R.id.image_button_user2);
        ImageView btnOpenRandom = (ImageView) findViewById(R.id.image_button_random2);
        ImageView btnOpenHome = (ImageView) findViewById(R.id.image_button_home2);

        userDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserData();
            }
        });

        btnOpenHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });
    }

    public void startAsyncActivity() {
        GetRandomAsyncTask task = new GetRandomAsyncTask();
        task.execute();
        return;
    }

    private class GetRandomAsyncTask extends AsyncTask<Void, Void, Restaurant> {

        @Override
        protected Restaurant doInBackground(Void... voids) {
            Restaurant randomRestaurant = new Restaurant();
            //fall back option
            randomRestaurant.setRestaurantId("ChIJf83U3auQTYcRcsZgTfnaQOg");
            randomRestaurant.setRestaurantAddress("445 Freedom Blvd 200 W, Provo, UT 84601, USA");
            randomRestaurant.setRestaurantName("Example Restaurant");
            randomRestaurant.setRestaurantRating(5.0);
            randomRestaurant.setRestaurantWebsiteUri(Uri.parse("google.com"));

            try {

                String result = getRandomPlacesJSON();
                JsonObject jsonObject = gson.fromJson( result, JsonObject.class);
                JsonArray places = jsonObject.get("results").getAsJsonArray();
                Iterator<JsonElement> iterator = places.iterator();
                Integer i = 0;
                Random random = new Random();
                Integer luckyNumber = random.nextInt(20);
                while(iterator.hasNext()) {
                    JsonObject place = iterator.next().getAsJsonObject();


                    if (i == luckyNumber) {
                        randomRestaurant.setRestaurantName(place.get("name").toString().replaceAll("\"", ""));
                        randomRestaurant.setRestaurantId(place.get("place_id").toString().replaceAll("\"", ""));
                        return randomRestaurant;
                    }
                    i++;
                }
                return randomRestaurant;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return randomRestaurant;
        }

        @Override
        protected void onPostExecute(Restaurant r) {
            super.onPostExecute(r);
            //Toast.makeText(random_place.this, s, Toast.LENGTH_LONG).show();
            TextView resultView = (TextView) findViewById(R.id.result_view);
            CardView resultCard = (CardView) findViewById(R.id.result_view_card);
            TextView randomPlaceId = (TextView) findViewById(R.id.result_place_id);
            resultView.setText(r.restaurantName);
            randomPlaceId.setText(r.restaurantId);
            resultCard.setVisibility(View.VISIBLE);
        }

        private String getHttpResults(String url) throws IOException {
            // Make a connection to the provided URL
            URLConnection connection = new URL(url).openConnection();

            // Open the response stream and get a reader for it.
            InputStream responseStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));

            // If the API was written well, there will be only one line,
            // but just in case, I will go through each line in the response.

            // Because this could happen multiple times, a StringBuilder is much more efficient.
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }

        public String getRandomPlacesJSON() throws IOException {
            EditText editText = (EditText) findViewById(R.id.editTextQuery);
            String q = editText.getText().toString();
            String url = String.format("%s?query=%s&key=%s&type=restaurant",
                    PLACE_SEARCH_ENDPOINT,
                    URLEncoder.encode(q, apiCharset),
                    URLEncoder.encode(API_KEY, apiCharset));
            Log.d("RandomPlaceActivity", getHttpResults(url));
            return getHttpResults(url);
        }

    }

    public void openUserData() {
        Intent intent = new Intent(this, UserInfo.class);
        startActivity(intent);
    }
    public void openHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void loadRandomRestaurant() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        Handler mainHandler = new Handler(Looper.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    TextView randomPlaceId = (TextView) findViewById(R.id.result_place_id);
                    MainActivity m = MainActivity.getInstance();
                    m.loadRestaurant(randomPlaceId.getText().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } // This is your code
        };
        mainHandler.post(myRunnable);

    }
}

