package br.edu.fa7.pomodoro.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.fa7.pomodoro.R;
import br.edu.fa7.pomodoro.activity.MainActivity;
import br.edu.fa7.pomodoro.dao.TaskDao;
import br.edu.fa7.pomodoro.model.Task;
import br.edu.fa7.pomodoro.util.EditModeType;

/**
 * Created by bruno on 31/08/15.
 */
public class FormFragment extends Fragment implements View.OnClickListener {

    private AppCompatActivity mMainActiivty;
    private Toolbar mMainToolbar;
    private TaskDao mTaskDao;
    private Task mTask;
    private Bundle mBundle;
    private EditModeType mMode;

    private EditText mTitle;
    private EditText mDescription;
    private EditText mTomatoes;
    private Button mRemoveBtn;
    private CheckBox mDoneCheck;
    private boolean mWasDone = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_form, container, false);

        mMainActiivty = (AppCompatActivity) getActivity();

        mMainToolbar = (Toolbar) mMainActiivty.findViewById(R.id.main_toolbar);
        mMainActiivty.setSupportActionBar(mMainToolbar);
        mMainActiivty.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTaskDao = new TaskDao(mMainActiivty);

        mTitle = (EditText) v.findViewById(R.id.fragment_form_title);
        mDescription = (EditText) v.findViewById(R.id.fragment_form_description);
        mTomatoes = (EditText) v.findViewById(R.id.fragment_form_tomatoes);
        mRemoveBtn = (Button) v.findViewById(R.id.fragment_form_remove_btn);
        mDoneCheck = (CheckBox) v.findViewById(R.id.fragment_form_done_check);

        mDoneCheck.setOnClickListener(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        mBundle = getArguments();
        mMode = (EditModeType) mBundle.getSerializable("mode");

        mTask = new Task();

        switch (mMode) {
            case EDIT_NEW_MODE :
                setupInsertMode();
                break;

            case EDIT_UPDATE_MODE :
                setupUpdateMode();
                break;
        }

        populateForm();
    }

    private void setupInsertMode() {
        mMainToolbar.setTitle("New Task");
        mRemoveBtn.setVisibility(View.GONE);
        mDoneCheck.setVisibility(View.GONE);
        mDoneCheck.setChecked(false);
    }

    private void setupUpdateMode() {
        mMainToolbar.setTitle("Update Task");

        Long id = mBundle.getLong("id");

        if (id != null) {
            mTask = mTaskDao.find(id);
            mWasDone = mTask.isDone();
        } else {
            Toast.makeText(mMainActiivty, "Task not found", Toast.LENGTH_SHORT).show();
        }

        mRemoveBtn.setOnClickListener(this);
    }

    private void populateForm() {
        if (mTask != null) {
            String title = (mTask.getTitle() != null) ? mTask.getTitle() : "";
            String description = (mTask.getTitle() != null) ? mTask.getDescription() : "";

            mTitle.setText(title);
            mDescription.setText(description);
            mTomatoes.setText(Integer.toString(mTask.getTomatoes()));
            mDoneCheck.setChecked(mTask.isDone());

            onDoneCheckBoxChange();
        }
    }

    private void save() {
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();
        int tomatoes = Integer.parseInt(mTomatoes.getText().toString());
        boolean done = mDoneCheck.isChecked();

        Task task = new Task(title, description, tomatoes);
        task.setDoneTomatoes(mTask.getDoneTomatoes());

        if (mWasDone != done) {
            task.setDoneTomatoes(0);
        }

        if (!mWasDone && done) {
            task.setDoneTomatoes(task.getTomatoes());
        }

        if (task.isValid()) {
            switch (mMode) {
                case EDIT_NEW_MODE:
                    mTaskDao.insert(task);
                    break;

                case EDIT_UPDATE_MODE:
                    task.setId(mTask.getId());
                    task.setDone(done);

                    mTaskDao.update(task);
                    break;
            }
        } else {
            Toast.makeText(mMainActiivty, "Title can not be empty. Task could not be save.", Toast.LENGTH_LONG).show();
        }

        ((MainActivity) mMainActiivty).getMainFragment().dataChanged();
    }

    private void remove() {
        Long id = mBundle.getLong("id");

        if (id != null) {
            Task task = new Task(id);
            mTaskDao.delete(task);

            ((MainActivity) mMainActiivty).getMainFragment().dataChanged();
        } else {
            Toast.makeText(mMainActiivty, "Task not found!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }


    public void onDoneCheckBoxChange() {
        mTitle.setEnabled(!mDoneCheck.isChecked());
        mDescription.setEnabled(!mDoneCheck.isChecked());
        mTomatoes.setEnabled(!mDoneCheck.isChecked());
    }


    @Override
    public void onStop() {
        super.onStop();
        save();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_form_remove_btn:
                remove();
                break;

            case R.id.fragment_form_done_check:
                onDoneCheckBoxChange();
                break;
        }
    }

    private void finish() {
        FragmentTransaction transaction = mMainActiivty.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, new MainFragment());
        transaction.commit();
    }
}
