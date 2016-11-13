package com.telecom.mockexamapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.telecom.mockexamapp.adapter.FileListAdapter;
import com.telecom.mockexamapp.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/30.
 */

public class LoadFileActivity extends AppCompatActivity {
    @BindView(R.id.ry_filelist) RecyclerView recyclerView;
    private FileListAdapter adapter;
    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
    private ProgressDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadfile);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new FileListAdapter(this));
        adapter.loadFileList(Environment.getExternalStorageDirectory());
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在导入习题，请稍等……");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        adapter.setProgressDialog(dialog);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(adapter != null ){
                if(!TextUtils.isEmpty(adapter.getParentPath())
                        &&ROOT_PATH.equals(adapter.getParentPath())){
                    adapter.loadFileList(new File(adapter.getParentPath()));
                    return false;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
