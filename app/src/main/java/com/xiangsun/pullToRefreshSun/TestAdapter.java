package com.xiangsun.pullToRefreshSun;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by admin on 2018/3/15.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestHolder>{

    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_view, parent, false));
    }

    @Override
    public void onBindViewHolder(TestHolder holder, int position) {
        holder.tv.setText(""+position);
        holder.tv.setTextSize(25);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class TestHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public TestHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            tv.setText("nihaohaohoaho");
        }
    }
}
