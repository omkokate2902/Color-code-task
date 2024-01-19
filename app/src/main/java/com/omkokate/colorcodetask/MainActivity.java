package com.omkokate.colorcodetask;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String API_URL = "https://ecom.saanvigs.com/api/Banner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assuming you have an ImageView with id 'imageView' in your layout
        ImageView imageView = findViewById(R.id.imageView);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Set the span count to 3

        // Make a network request to get the API response
        getBannerFromApi(imageView);

        getBannerFromApi2(recyclerView);
    }

    private void getBannerFromApi(final ImageView imageView) {
        OkHttpClient client = new OkHttpClient();

        // Replace "Ecom-all-Home-bestquality-bannertop" with your desired Stype
        String stype = "Ecom-all-Home-bestquality-bannertop";

        // Create a JSON object with the required parameters
        JSONObject json = new JSONObject();
        try {
            json.put("Stype", stype);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Build the request
        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(MediaType.parse("application/json"), json.toString()))
                .build();

        // Execute the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    try {
                        // Parse the JSON response
                        JSONObject jsonObject = new JSONObject(responseData);
                        String imageUrl = jsonObject.getJSONArray("Data")
                                .getJSONObject(0)
                                .getString("Picture");

                        // Load image using Picasso on the UI thread
                        runOnUiThread(() -> {
                            Picasso.get()
                                    .load(imageUrl)
                                    .placeholder(R.color.white)
                                    .error(R.color.white)
                                    .into(imageView);
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                        showErrorMessage("Error parsing JSON");
                    }
                } else {
                    // Handle unsuccessful response
                    showErrorMessage("Unsuccessful response");
                }
            }
        });
    }

    private void showErrorMessage(final String message) {
        runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        });
    }

    private void getBannerFromApi2(final RecyclerView recyclerView) {
        OkHttpClient client = new OkHttpClient();

        // Replace "Ecom-all-Offer-Highlights-6" with your desired Stype
        String stype = "Ecom-all-Offer-Highlights-6";

        // Create a JSON object with the required parameters
        JSONObject json = new JSONObject();
        try {
            json.put("Stype", stype);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Build the request
        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(MediaType.parse("application/json"), json.toString()))
                .build();

        // Execute the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure, e.g., show an error message
                showErrorMessage("Network request failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    runOnUiThread(() -> {
                        try {
                            // Parse the JSON response
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray dataArray = jsonObject.getJSONArray("Data");

                            // Populate the bannerItemList with BannerItem objects
                            List<BannerItem> bannerItemList = new ArrayList<>();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject itemObject = dataArray.getJSONObject(i);
                                String imageUrl = itemObject.getString("Picture");

                                BannerItem bannerItem = new BannerItem(imageUrl);

                                Log.d("tag", imageUrl);
                                bannerItemList.add(bannerItem);
                            }

                            // Set up RecyclerView with the adapter
                            BannerAdapter bannerAdapter = new BannerAdapter(bannerItemList);
                            recyclerView.setAdapter(bannerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                            showErrorMessage("Error parsing JSON");
                        }
                    });
                } else {
                    // Handle unsuccessful response, e.g., show an error message
                    showErrorMessage("Unsuccessful response");
                }
            }
        });
    }
}
