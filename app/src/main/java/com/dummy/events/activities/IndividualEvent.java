package com.dummy.events.activities;

import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dummy.events.providers.CursorComment;
import com.dummy.events.providers.NonScrollListView;
import com.dummy.events.R;
import com.dummy.events.database.Dao;
import com.dummy.events.database.StringVo;
import com.pixplicity.easyprefs.library.Prefs;

public class IndividualEvent extends AppCompatActivity implements View.OnClickListener{

    ImageView image;
    TextView category;
    TextView description;
    TextView signedUp;
    EditText comment;
    Button post;
    Button signUp;
    NonScrollListView list;
    FloatingActionButton fab;
    CollapsingToolbarLayout collapsingToolbar;
    Cursor cursor,cursor1;

    String query ;
    String comment1 ;
    int id;

    Dao dao;
    StringVo vo;
    CursorComment cursorComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        image = (ImageView) findViewById(R.id.image);
        category = (TextView) findViewById(R.id.eventCategory);
        signUp = (Button) findViewById(R.id.signUp);
        signedUp = (TextView) findViewById(R.id.signedUp);
        description = (TextView) findViewById(R.id.description);
        comment = (EditText) findViewById(R.id.newComment);
        post = (Button) findViewById(R.id.postComment);
        list = (NonScrollListView) findViewById(R.id.list);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        dao = new Dao(this);
        vo = new StringVo();

        query = "Select * from events where AID="+id;
        cursor = dao.fetch(query);
        cursor.moveToFirst();

        image.setImageBitmap(convertByteInImage(cursor.getBlob(cursor.getColumnIndexOrThrow("image"))));
        category.setText(cursor.getString(cursor.getColumnIndexOrThrow("category")));
        description.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));
        collapsingToolbar.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));

        if (cursor.getString(cursor.getColumnIndexOrThrow("signstate")).equals("true"))
        {
            signUp.setVisibility(View.GONE);
            signedUp.setVisibility(View.VISIBLE);
        }

        signUp.setOnClickListener(this);
        post.setOnClickListener(this);

    }

    public Bitmap convertByteInImage(byte[] imageByte)
    {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.postComment)
        {

            if (comment.getText().toString().length()!=0)
            {
                vo.setComment(comment.getText().toString());
                vo.setId(id);

                dao.insertComment(vo);
                Toast.makeText(this, "Posted", Toast.LENGTH_SHORT).show();

                listRefresh();
                comment.setText("");

            }else
            {
                Toast.makeText(this, "Please Enter a Comment", Toast.LENGTH_SHORT).show();
            }


        }else if (v.getId()==R.id.signUp)
        {
            if (Prefs.contains("Email"))
            {
                String query = "UPDATE events SET signstate='true', signemail='"+
                        Prefs.getString("Email","")+"'";
                dao.anyQuery(query);
                signUp.setVisibility(View.GONE);
                signedUp.setVisibility(View.VISIBLE);

                Toast.makeText(this, "Signed Up - "+Prefs.getString("Email",""), Toast.LENGTH_SHORT).show();
            }else
            {
                final MaterialDialog dialog = new MaterialDialog.Builder(this).
                        title("Email")
                        .customView(R.layout.email,false)
                        .build();

                dialog.show();

                final EditText text = (EditText) dialog.findViewById(R.id.emailtext);
                Button button = (Button) dialog.findViewById(R.id.emailPost);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String query = "UPDATE events SET signstate='true', signemail='"+
                                text.getText().toString()+"'";
                        dao.anyQuery(query);
                        signUp.setVisibility(View.GONE);
                        signedUp.setVisibility(View.VISIBLE);

                        Toast.makeText(IndividualEvent.this, "Signed Up - "+text.getText().toString(),
                                Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });
            }
        }
    }

    private void listRefresh()
    {
        query = "Select _id,comment from comments where _id="+id+" order by AID desc";
        cursor1 = dao.fetch(query);
        cursorComment = new CursorComment(this,cursor1,0);
        list.invalidate();
        list.setAdapter(cursorComment);
    }

    @Override
    protected void onResume() {
        super.onResume();

        listRefresh();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
