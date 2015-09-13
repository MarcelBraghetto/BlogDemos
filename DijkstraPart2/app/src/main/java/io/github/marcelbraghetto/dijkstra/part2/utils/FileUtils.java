package io.github.marcelbraghetto.dijkstra.part2.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.github.marcelbraghetto.dijkstra.part2.MainApp;

/**
 * Created by Marcel Braghetto on 12/09/15.
 *
 * Helper methods for loading files for the demo.
 */
public final class FileUtils {
    private static final String NEWLINE = "\n";

    private FileUtils() { }

    public static boolean saveTextFile(@NonNull String fileName, @NonNull String text) {
        FileOutputStream outputStream;

        try {
            outputStream = MainApp.getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Nullable
    public static String loadTextFile(@NonNull String fileName) {
        StringBuilder sb = new StringBuilder("");

        try {
            FileInputStream fileInputStream = MainApp.getContext().openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readString = bufferedReader.readLine();

            while(readString != null) {
                sb.append(readString).append(NEWLINE);
                readString = bufferedReader.readLine();
            }

            inputStreamReader.close();
        } catch (IOException e) {
            return null;
        }

        return sb.toString();
    }

    @NonNull
    public static String loadRawTextFile(@RawRes int resourceId) {
        StringBuilder sb = new StringBuilder("");

        try {
            InputStream inputStream = MainApp.getContext().getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readString = bufferedReader.readLine();

            while(readString != null) {
                sb.append(readString).append(NEWLINE);
                readString = bufferedReader.readLine();
            }

            inputStreamReader.close();
        } catch (IOException e) {
            return "";
        }

        return sb.toString();
    }
}