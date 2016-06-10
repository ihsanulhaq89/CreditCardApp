package com.mac.creditcardapp.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mac.creditcardapp.R;
import com.mac.creditcardapp.models.Card;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MockHttpClient {
    private static final String IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH";

    public static List<Card> getCardsFromRemoteDB(Context ctx) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean isFirstLaunch = prefs.getBoolean(IS_FIRST_LAUNCH, true);
        InputStream inputStream = isFirstLaunch ? getDummyData(ctx) : getRemoteDBData(ctx);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            buffreader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new Gson().fromJson(text.toString(), new TypeToken<List<Card>>() {
        }.getType());
    }

    private static InputStream getRemoteDBData(Context ctx) {
        String filename = "remoteDb";
        File myDir = ctx.getFilesDir();
        File file = new File(myDir + "/text/", filename);
        try {
            return new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void postCardsToRemoteDB(Context ctx, String outputString) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        prefs.edit().putBoolean(IS_FIRST_LAUNCH, false).commit();

        String filename = "remoteDb";
        File myDir = ctx.getFilesDir();

        try {
            File file = new File(myDir + "/text/", filename);
            if (file.getParentFile().mkdirs()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(outputString.getBytes());
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static InputStream getDummyData(Context ctx) {
        return ctx.getResources().openRawResource(R.raw.remote_json);
    }
}
