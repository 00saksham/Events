package com.dummy.events;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dummy.events.database.Dao;
import com.dummy.events.database.Vo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddEvent extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    Uri imageUri;
    byte[] byteArray;
    boolean imageSelect = false;
    String selectedCategory = "";

    Dao dao;
    Vo vo;

    ImageView eventImage;
    TextView title;
    TextView description;
    Spinner category;
    FloatingActionButton fab;

    String[] spinnerCategories = {"Mobile Development", "Science","DJ","Food & Drinks","College Fest"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        eventImage = (ImageView) findViewById(R.id.eventImage);
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        category = (Spinner) findViewById(R.id.category);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        dao = new Dao(this);
        vo = new Vo();

        setSpinner();
        fab.setOnClickListener(this);
    }

    public void openGallery(View view) {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 100);
    }

    private void setSpinner()
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, spinnerCategories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setOnItemSelectedListener(this);
        category.setAdapter(dataAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data.getData();
            //imageView.setImageURI(imageUri);
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(
                        imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bmp = BitmapFactory.decodeStream(imageStream);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp = Bitmap.createScaledBitmap(bmp, 1280, 720, true);
            bmp.compress(Bitmap.CompressFormat.PNG, 80, stream);
            eventImage.setImageBitmap(bmp);
            byteArray = stream.toByteArray();
            imageSelect = true;
            try {
                stream.close();
                stream = null; // for Garbage Collection
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            if (title.getText().toString().length() != 0) {
                if (description.getText().toString().length() != 0) {
                    if (imageSelect) {
                        if (!selectedCategory.equals(""))
                        {
                            vo.setTitle(title.getText().toString());
                            vo.setSignState("false");
                            vo.setSignEmail("");
                            vo.setImage(byteArray);
                            vo.setCategory(selectedCategory);
                            vo.setComments("");
                            vo.setDescription(description.getText().toString());
                            dao.insert(vo);

                            Toast.makeText(this, "Added Event", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }else
                    {
                        Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(this, "Please Enter Description", Toast.LENGTH_SHORT).show();
                }
            }else
            {
                Toast.makeText(this, "Please Enter Title", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        selectedCategory = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return  true;
    }
}
