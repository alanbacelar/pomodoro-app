package br.edu.fa7.pomodoro.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.edu.fa7.pomodoro.model.Task;

/**
 * Created by bruno on 30/08/15.
 */
public class TaskDao extends GenericDAO<Task> {

    private static final String TABLE_NAME = "task";

    public TaskDao(Context context) {
        super(context);
    }

    @Override
    public void insert(Task obj) {
        db.insert(TABLE_NAME, null, createContentValue(obj));
    }

    @Override
    public void update(Task obj) {
        ContentValues values = createContentValue(obj);
        db.update(TABLE_NAME, values, "_id = ?", new String[]{obj.getId().toString()});
    }

    @Override
    public void delete(Task obj) {
        db.delete(TABLE_NAME, "_id = ?", new String[]{obj.getId().toString()});
    }

    @Override
    public Task find(Long id) {
        Log.d("ID", Long.toString(id));
        Cursor cursor = db.query(TABLE_NAME, null, "_id = ?", new String[]{Long.toString(id)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return curso2Task(cursor);
        }

        return null;
    }

    @Override
    public List<Task> findAll() {
        List<Task> list = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "done ASC, _id");
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add(curso2Task(cursor));

            }
        }

        return list;
    }

    protected Task curso2Task(Cursor cursor) {
        Long id = cursor.getLong(cursor.getColumnIndex("_id"));
        String title = cursor.getString(cursor.getColumnIndex("title"));
        String description = cursor.getString(cursor.getColumnIndex("description"));
        int tomatoes = cursor.getInt(cursor.getColumnIndex("tomatoes"));
        int done = cursor.getInt(cursor.getColumnIndex("done"));

        boolean isDone = (done > 0);

        Task task = new Task(id, title, description, tomatoes);
        task.setDone(isDone);

        return task;
    }

    @Override
    protected ContentValues createContentValue(Task obj) {
        ContentValues values = new ContentValues();
        values.put("_id", obj.getId());
        values.put("title", obj.getTitle());
        values.put("description", obj.getDescription());
        values.put("tomatoes", obj.getTomatoes());

        int done = (obj.isDone()) ? 1 : 0;
        values.put("done", done);

        return values;
    }

}
