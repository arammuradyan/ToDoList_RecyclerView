package com.example.todolist10;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;



public class Utils {


     void setBitmap(Context context, ImageView view, Uri uri,int width,int hight){
        Bitmap bitmap=null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(bitmap!=null){
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,width, hight,true);

            view.setImageBitmap(scaledBitmap);}

    }

     void onBackgroundColorSet(Task task, View view){
        if (task.getType() == 5.0f) {
            view.setBackgroundResource(R.drawable.view_5_background);
        } else if (task.getType() == 4.0f) {
            view.setBackgroundResource(R.drawable.view_4_background);
        } else if (task.getType() == 3.0f) {
            view.setBackgroundResource(R.drawable.view_3_background);
        } else if (task.getType() == 2.0f) {
            view.setBackgroundResource(R.drawable.view_2_background);
        } else if (task.getType() == 1.0f) {
            view.setBackgroundResource(R.drawable.view_1_background);
        } else{ view.setBackgroundResource(R.drawable.view_backgroound);}

    }

}
