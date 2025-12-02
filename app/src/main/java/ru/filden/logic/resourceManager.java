package ru.filden.logic;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class resourceManager {
    private static final String TAG = "Scheduler : resources ";
    private static final Type StudentListType = new TypeToken<List<Student>>() {}.getType();
    private static final Gson gson = new Gson();
    private static final String resourceDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath()+"/Scheduler/";
    public resourceManager(){
        new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath()+"/Scheduler").mkdir();
    }
    public static String StudentsToJson(List<Student> students){
        return gson.toJson(students, StudentListType);
    }
    public static List<Student> getStudentsFromJson(String json){
        return gson.fromJson(json, StudentListType);
    }
    public static List<Student> getStudentsFromJson(Reader json){
        return gson.fromJson(json, StudentListType);
    }
    @NonNull
    public static Boolean SaveJsonString(String json, String fileS) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(resourceDirPath+fileS)){
            Log.i(TAG,"write file" + resourceDirPath+fileS);
            fos.write(json.getBytes(StandardCharsets.UTF_8));
            Log.i(TAG,"the file was written!");

        } catch (IOException e) {
            Log.e(TAG, "the file was not written!" +e.getMessage());
            return false;
        }
        return true;
    }

    public static String LoadJsonString(@NonNull String file){
        try(FileInputStream fis  = new FileInputStream(resourceDirPath+file)){
            Log.i(TAG, "reading file: "+file);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            Log.i(TAG, "the file was reading!");
            return new String(bytes);
        }
        catch (IOException e){
            Log.e(TAG,"the file was not reading!" +e.getMessage());
        }
        return null;
    }
}
