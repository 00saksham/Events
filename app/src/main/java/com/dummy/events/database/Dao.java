package com.dummy.events.database;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Saksham on 23-Mar-17.
 */

public class Dao {
    DbAdapter adapter;

    public Dao(Context context) {
        adapter = new DbAdapter(context);
    }

    public void anyQuery(String query) {
        adapter.anyQuery(query);
    }

    public void insert(Vo vo) {
        adapter.insert(vo);
    }

    public void insertComment(StringVo vo) {
        adapter.insertComments(vo);
    }

    public Cursor fetch(String query) {
        return adapter.fetch(query);
    }

}
