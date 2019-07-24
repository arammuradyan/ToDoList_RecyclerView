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
private final int STORAGE_READ_REQUEST_CODE=654;

public String imgUri="";
public String imgUriForSaveAndPreview="";
public Uri uri=null;
public Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        utils=new Utils();
        viewInitialization();

        Intent stateIntent = getIntent();
        if (stateIntent.getBundleExtra("TaskToPreview") !=null) {

            Bundle taskBundle=stateIntent.getBundleExtra("TaskToPreview");
            Task taskToPreview=(Task) taskBundle.get("TaskToPreview");

           utils.onBackgroundColorSet(taskToPreview,view_preview);

            btn_delete.setVisibility(Button.VISIBLE);
            btn_add_preview.setText(R.string.save_btn);

            rb_preview.setRating(taskToPreview.getType());
            et_name_preview.setText(taskToPreview.getName());
            et_discription_preview.setText(taskToPreview.getDescription());

          imgUriForSaveAndPreview=taskToPreview.getImgUri();
          Uri urii=Uri.parse(imgUriForSaveAndPreview);


          if( ActivityCompat.checkSelfPermission(PreviewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.
                  PERMISSION_GRANTED){
              ActivityCompat.requestPermissions(PreviewActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                      STORAGE_READ_REQUEST_CODE);

          }else{ utils.setBitmap(PreviewActivity.this,img_preview,urii,600,900);}

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
            Intent intent=getIntent();
            if (intent.getBundleExtra("TaskToPreview")==null) {


                Task addTask = new Task(et_name_preview.getText().toString(), et_discription_preview.getText().toString(),rb_preview.
                        getRating(),imgUri);
                Bundle addBundle=new Bundle();
                addBundle.putParcelable("addTask",addTask);
                Intent addIntent=new Intent();
                addIntent.putExtra("addTask",addBundle);
                imgUri="";
                imgUriForSaveAndPreview="";
                setResult(RESULT_OK,addIntent);
                finish(); }
                else{
                int index = intent.getIntExtra("index", -100);

                Bundle bundle=intent.getBundleExtra("TaskToPreview");
                 Task saveTask=bundle.getParcelable("TaskToPreview");
                    Task task;
                   if(imgUri.equals("")){
                        task = new Task(et_name_preview.getText().toString(), et_discription_preview.getText().toString(),
                                rb_preview.getRating(),saveTask.getImgUri());
                        imgUri="";
                        imgUriForSaveAndPreview="";
                    }else{
                    task = new Task(et_name_preview.getText().toString(), et_discription_preview.getText().toString(),
                        rb_preview.getRating(),imgUri);}
                imgUriForSaveAndPreview="";
                imgUri="";
                Intent saveIntent=new Intent();
                Bundle saveBundle= new Bundle();
                saveBundle.putParcelable("saveTask",task);
                saveIntent.putExtra("saveTask",saveBundle);
                saveIntent.putExtra("index",index);
                setResult(RESULT_OK,saveIntent);
                    finish();
                }
        }
    });

    btn_delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent receivedDeleteIntent = getIntent();
            int index = receivedDeleteIntent.getIntExtra("index", -100);
            Intent deleteIntent=new Intent();
            deleteIntent.putExtra("index",index);
            imgUri="";
            imgUriForSaveAndPreview="";
            setResult(RESULT_OK,deleteIntent);
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

   // @Override
   /* public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==STORAGE_READ_REQUEST_CODE){
            for (int i = 0; i <permissions.length ; i++) {
             if ( permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i]==PackageManager.PERMISSION_GRANTED){
                utils.setBitmap(PreviewActivity.this,img_preview,uri,600,900);
             }
            }
        }
    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_URI_REQUEST_CODE && resultCode == RESULT_OK) {
        Uri uri=data.getData();
        imgUri = data.getDataString();
       int hight=img_preview.getHeight()*2;
       int width=(int)(img_preview.getWidth()*1.5);
        utils.setBitmap(PreviewActivity.this,img_preview,uri,600,900);

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


}