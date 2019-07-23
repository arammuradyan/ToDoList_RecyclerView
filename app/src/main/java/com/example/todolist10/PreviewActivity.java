package com.example.todolist10;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.Permission;


public class PreviewActivity extends AppCompatActivity {



public ImageView img_preview;
public Button btn_add_preview;
public RatingBar rb_preview;
public EditText et_name_preview;
public EditText et_discription_preview;
public Button btn_chose_photo_preview;
public Button btn_delete;
public View view_preview;
public final int IMAGE_URI_REQUEST_CODE=23;
public String imgUri="";
private final int STORAGE_READ_REQUEST_CODE=654;
public Uri uri=null;
static int  hight=0;
static int width=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        viewInitialization();

        Intent state = getIntent();
        int index = state.getIntExtra("index", -100);

      if (index >= 0) {
          Task task=MainActivity.taskAdapter.getTaskList().get(index);
          onBackgroundColorSet(task,view_preview);

          btn_delete.setVisibility(Button.VISIBLE);
          btn_add_preview.setText(R.string.save_btn);

          rb_preview.setRating(task.getType());                                     // Bundle taskBundle=state.getBundleExtra("TaskToPreview");Task taskToPreview=(Task) taskBundle.get("TaskToPreview");
          et_name_preview.setText(task.getName());
          et_discription_preview.setText(task.getDescription());

          imgUri=task.getImgUri();
          Uri uri=Uri.parse(task.getImgUri());


          if( ActivityCompat.checkSelfPermission(PreviewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.
                  PERMISSION_GRANTED){
              ActivityCompat.requestPermissions(PreviewActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                      STORAGE_READ_REQUEST_CODE);

          }else{ setBitmap(PreviewActivity.this,img_preview,uri);}

      }

      btn_chose_photo_preview.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent uriIntent=new Intent(Intent.ACTION_PICK);
            uriIntent.setType("image/*");
            startActivityForResult(uriIntent,IMAGE_URI_REQUEST_CODE);
        }
    });

    btn_add_preview.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent addIntent=getIntent();
            int index=addIntent.getIntExtra("index",-100);
            if (index<0) {
                Task task = new Task(et_name_preview.getText().toString(), et_discription_preview.getText().toString(),rb_preview.
                        getRating(),imgUri);
                MainActivity.taskAdapter.add(task);
                    finish(); }
                else{
                    Task task;
                    if(imgUri.equals("")){
                        task = new Task(et_name_preview.getText().toString(), et_discription_preview.getText().toString(),
                                rb_preview.getRating(),MainActivity.taskAdapter.getTaskList().get(index).getImgUri());
                    }else{
                    task = new Task(et_name_preview.getText().toString(), et_discription_preview.getText().toString(),
                        rb_preview.getRating(),imgUri);}
                    MainActivity.taskAdapter.save(index,task);
                    finish();
                }
        }
    });

    btn_delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent deleteIntent = getIntent();
            int index = deleteIntent.getIntExtra("index", -100);
            MainActivity.taskAdapter.delete(index);
            finish();
        }
    });

    rb_preview.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating == 5.0f) {
                    view_preview.setBackgroundResource(R.drawable.view_5_background);
                } else if (rating== 4.0f) {
                    view_preview.setBackgroundResource(R.drawable.view_4_background);
                } else if (rating== 3.0f) {
                    view_preview.setBackgroundResource(R.drawable.view_3_background);
                } else if (rating== 2.0f) {
                    view_preview.setBackgroundResource(R.drawable.view_2_background);
                } else if (rating== 1.0f) {
                    view_preview.setBackgroundResource(R.drawable.view_1_background);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==STORAGE_READ_REQUEST_CODE){
            for (int i = 0; i <permissions.length ; i++) {
             if ( permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i]==PackageManager.PERMISSION_GRANTED){
                 //grantUriPermission("com.example.todolist10", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                 setBitmap(PreviewActivity.this,img_preview,uri);
             }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_URI_REQUEST_CODE && resultCode == RESULT_OK) {
        Uri uri=data.getData();
        imgUri = data.getDataString();
        hight=img_preview.getHeight()*2;
        width=(int)(img_preview.getWidth()*1.5);
        setBitmap(PreviewActivity.this,img_preview,uri);

        }
    }

    public void viewInitialization(){
        img_preview=findViewById(R.id.imgView);
        btn_add_preview=findViewById(R.id.add);
        btn_delete=findViewById(R.id.btn_delete);
        btn_chose_photo_preview=findViewById(R.id.pic_chose_btn);
        et_name_preview=findViewById(R.id.et_name);
        et_discription_preview=findViewById(R.id.et_description);
        rb_preview=findViewById(R.id.rb_type);
        view_preview=findViewById(R.id.view);
}



    public static void onBackgroundColorSet( Task task,View view){
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

    public static void setBitmap(Context context,ImageView view, Uri uri){
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

}