package com.telecom.mockexamapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telecom.mockexamapp.thread.LoadExcclFileTask;
import com.telecom.mockexamapp.R;
import com.telecom.mockexamapp.bean.MultipleChoice;
import com.telecom.mockexamapp.bean.SingleChoice;
import com.telecom.mockexamapp.bean.TestRecord;
import com.telecom.mockexamapp.bean.YesOrNoQuestion;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * @author Administrator
 * Created by Administrator on 2016/8/30.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.MyViewHolder> {
    private Context mContext;
    private List<File> dates;
    private File localPath;
    private ProgressDialog progressDialog;

    public FileListAdapter(Context context) {
        this.mContext = context;
    }

    private void updateFiles(List<File> files) {
        this.dates = files;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.file_info_item, parent,
                false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv.setText(dates.get(position).getName());
        if (dates.get(position).isDirectory()) {
            holder.tv.setOnClickListener((View v) ->
                    loadFileList(dates.get(position))
            );
        } else {
            holder.tv.setOnClickListener((View v) -> {
                DataSupport.deleteAll(MultipleChoice.class);
                DataSupport.deleteAll(SingleChoice.class);
                DataSupport.deleteAll(YesOrNoQuestion.class);
                DataSupport.deleteAll(TestRecord.class);
                if (progressDialog == null) {
                    new LoadExcclFileTask(dates.get(position).getPath()).execute();
                } else {
                    new LoadExcclFileTask(dates.get(position).getPath(), progressDialog).execute();
                }
            });
        }
    }

    public void loadFileList(File file) {
        if (file.isDirectory() && file.length() > 0) {
            localPath = file;
            final List<File> lists = new ArrayList<>();
            Observable.from(file.listFiles())
                    .filter(fileAble -> fileAble.isDirectory() || fileAble.getName().endsWith(".xls"))
                    .map(fileItem -> {
                        lists.add(fileItem);
                        return lists;
                    })
                    .subscribe(this::updateFiles);
        }
    }

    public void setProgressDialog(ProgressDialog dialog) {
        this.progressDialog = dialog;
    }

    public String getParentPath() {
        if (localPath != null) {
            return localPath.getParent();
        }
        return null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
         MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_fileName);
        }
    }
}
