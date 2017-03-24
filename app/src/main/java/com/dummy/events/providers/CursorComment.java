package com.dummy.events.providers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import  android.database.Cursor;

import com.dummy.events.R;

/**
 * Created by Saksham on 24-Mar-17.
 */

public class CursorComment extends CursorAdapter {
    public CursorComment(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.comment, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView textView = (TextView) view.findViewById(R.id.commentId);

        textView.setText(cursor.getString(1));
    }
}
