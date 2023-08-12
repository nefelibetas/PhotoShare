package com.fish.photoshare.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.documentfile.provider.DocumentFile;

import com.fish.photoshare.common.Api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Callback;

public class MyFileUtils {
    public static File getFileFromUri(Context context, Uri uri) {
        File file = null;
        try {
            DocumentFile documentFile = DocumentFile.fromSingleUri(context, uri);
            String displayName = documentFile.getName();
            String mimeType = context.getContentResolver().getType(uri);

            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream != null) {
                file = new File(context.getFilesDir(), displayName);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    public static void uploadAvatar(ArrayList<File> fileList, Callback callback) {
        HttpUtils.sendPostRequestForFormData(Api.UPLOAD, fileList, callback);
    }
    public static void bindUser(String imageUrl, Callback callback, Context context) {
        HashMap<String, String> params = new HashMap<>();
        ResourcesUtils resourcesUtils = new ResourcesUtils(context);
        String id = SharedPreferencesUtils.getString(context, resourcesUtils.ID, null);
        params.put("id", id);
        params.put("avatar", imageUrl);
        HttpUtils.sendPostRequest(Api.UPDATE, params, callback);
    }
    public static byte[] convertFileToBytes(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        fis.close();
        bos.close();
        return bos.toByteArray();
    }
}
