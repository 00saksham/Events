package com.dummy.events.providers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dummy.events.R;
import com.dummy.events.activities.IndividualEvent;

/**
 * Created by Saksham on 24-Mar-17.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    Cursor cursor;

    public Adapter(Context context, Cursor cursor)
    {
        this.cursor = cursor;
        this.context = context;

    }
    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int position)
    {
        cursor.moveToPosition(position);
        holder.textView.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        holder.image.setImageBitmap(convertByteInImage(cursor.getBlob(cursor.getColumnIndexOrThrow("image"))));

        if (!cursor.getString(cursor.getColumnIndexOrThrow("signState")).equals("false"))
        {
            holder.signState.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_check));
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public Bitmap convertByteInImage(byte[] imageByte)
    {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);
        return bitmap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;
        ImageView image;
        ImageView signState;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.eventTitle);
            image = (ImageView) itemView.findViewById(R.id.eventImage);
            signState = (ImageView) itemView.findViewById(R.id.signState);
            card = (CardView) itemView.findViewById(R.id.card);

            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            cursor.moveToPosition(getAdapterPosition());
            Intent intent = new Intent(context,IndividualEvent.class);
            intent.putExtra("id",cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            context.startActivity(intent);
        }
    }
}
