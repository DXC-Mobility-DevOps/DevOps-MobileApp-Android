package com.hpe.devops.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hpe.devops.entity.ProjectDetails;
import com.hpe.devops.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ProjectDetails> projectDetailsList = new ArrayList<>();

    HomeActivity homeActivity;

    Context context;

    public ProjectListAdapter(List<ProjectDetails> projectDetailsList, HomeActivity homeActivity) {
        this.projectDetailsList = projectDetailsList;
        this.homeActivity = homeActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_project_details, parent, false);
        viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ProjectListViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProjectListViewHolder projectListViewHolder = (ProjectListViewHolder) holder;
        projectListViewHolder.bindView(projectDetailsList.get(position), context, homeActivity);
    }

    @Override
    public int getItemCount() {
        return projectDetailsList.size();
    }
}
