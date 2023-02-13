package com.unito.smapstestapp.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.unito.smapstestapp.R;
import java.util.List;
import java.util.Map;

public class JsonRequestAdapter extends RecyclerView.Adapter<JsonRequestAdapter.ViewHolder>{

    private List<Map<String, Object>>  mData;

    public JsonRequestAdapter(List<Map<String, Object>> data) {
        this.mData = data;
    }

    public void updateData(List<Map<String, Object>>  data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.json_request_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // 绑定数据
        holder.tv_target.setText("id = "+mData.get(position).get("id")+"     "+mData.get(position).get("target"));
        holder.tv_smaps.setText("SMAPS = "+mData.get(position).get("SMAPS"));
        holder.tv_fun.setText(mData.get(position).get("msgType")+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                一定要刷新适配器 当条目发生改变这是必须的
                getListener.onClick(position);
                notifyDataSetChanged();
            }
        });

        // 如果下标和传回来的下标相等 那么确定是点击的条目 把背景设置一下颜色
        if (position == getmPosition()) {
            holder.itemView.setBackgroundColor(Color.GRAY);
        }else{
        // 否则的话就全白色初始化背景
            holder.itemView.setBackgroundColor(R.color.color_d3d3d3);
        }

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_target;
        TextView tv_smaps;
        TextView tv_fun;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_target = (TextView) itemView.findViewById(R.id.tv_target);
            tv_smaps = (TextView) itemView.findViewById(R.id.tv_smaps);
            tv_fun = (TextView) itemView.findViewById(R.id.tv_fun);
        }
    }

    public interface GetListener {

        void onClick(int position);
    }

    private GetListener getListener;

    public void setGetListener(GetListener getListener) {
        this.getListener = getListener;
    }
    private  int mPosition = -1;

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
