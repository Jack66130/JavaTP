package com.example.tpjavajack;

import android.content.Context;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.Scanner;

public class JSONFileReader {

    public static JSONObject loadJSONFromResource(Context context, int resourceId) {
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            Scanner scanner = new Scanner(inputStream);
            StringBuilder builder = new StringBuilder();

            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }

            scanner.close();
            return new JSONObject(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
