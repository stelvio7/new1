package com.new1.listlive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.new1.model.BroadcastList;
import com.new1.settop.R;

import java.util.ArrayList;

public class ListMenuLiveAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private  ArrayList<BroadcastList> liveList;
    private int mLayout;

    public ListMenuLiveAdapter(Context context,
                               ArrayList<BroadcastList> channelList) {
        this.mContext = context;
        this.liveList = channelList;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (liveList != null)
            return liveList.size();
        else
            return 0;
    }



    @Override
    public Object getItem(int position) {
        return liveList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LiveMenuListViewHolder viewHolder;
        // ImageView iv = (ImageView)convertView.findViewById(R.id.imageView1);
        // iv.setImageResource(img[position]);
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.livemenu_row, parent, false);

            viewHolder = new LiveMenuListViewHolder();
            //viewHolder.ivNowBroad = (ImageView)convertView.findViewById(R.id.ivNowBroad);
            viewHolder.txtNowBroad = (TextView)convertView.findViewById(R.id.txtNowBroad);
            //viewHolder.ivNowChannel = (ImageView)convertView.findViewById(R.id.ivNowChannel);
            viewHolder.txtNowChannel = (TextView)convertView.findViewById(R.id.txtNowChannel);
            viewHolder.txtNextTime = (TextView)convertView.findViewById(R.id.txtNextTime);
            viewHolder.txtNextBroad = (TextView)convertView.findViewById(R.id.txtNextBroad);
            //viewHolder.ivLine1 = (ImageView)convertView.findViewById(R.id.ivLine1);
            //viewHolder.ivLine2 = (ImageView)convertView.findViewById(R.id.ivLine2);
            viewHolder.ll1 = (LinearLayout) convertView.findViewById(R.id.ll1);
            viewHolder.ll2 = (LinearLayout) convertView.findViewById(R.id.ll2);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LiveMenuListViewHolder) convertView.getTag();
        }

        viewHolder.txtNowBroad.setText(liveList.get(position).getNowTitle());
        viewHolder.txtNowChannel.setText(liveList.get(position).getTitle());
        viewHolder.txtNextTime.setText(liveList.get(position).getNextTime());
        viewHolder.txtNextBroad.setText(liveList.get(position).getNextTitle());
        viewHolder.txtNowChannel.setFocusable(true);

        viewHolder.ll1.setTag("ll1" + String.valueOf(position));
        viewHolder.ll2.setTag("ll2" + String.valueOf(position));
        if(liveList.get(position).isFocused()){
            if(viewHolder.ll1.getTag().equals("ll1" + String.valueOf(position)))
                viewHolder.ll1.setVisibility(View.VISIBLE);
            if(viewHolder.ll2.getTag().equals("ll2" + String.valueOf(position)))
                viewHolder.ll2.setVisibility(View.VISIBLE);
        }else{
            if(viewHolder.ll1.getTag().equals("ll1" + String.valueOf(position)))
                viewHolder.ll1.setVisibility(View.GONE);
            if(viewHolder.ll2.getTag().equals("ll2" + String.valueOf(position)))
                viewHolder.ll2.setVisibility(View.GONE);
        }
//        viewHolder.txtNowChannel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    viewHolder.ll1.setVisibility(View.VISIBLE);
//                    viewHolder.ll2.setVisibility(View.VISIBLE);
//                } else {
//                    viewHolder.ll1.setVisibility(View.GONE);
//                    viewHolder.ll2.setVisibility(View.GONE);
//                }
//            }
//        });
//        ll1.setVisibility(View.GONE);
//        ivNowChannel.setVisibility(View.GONE);
//        ll2.setVisibility(View.GONE);
//        ivLine1.setVisibility(View.GONE);
//        ivLine2.setVisibility(View.GONE);


        return convertView;
    }

    public void selected(){

    }

    private void setOnFocus(LinearLayout selectVod1, TextView listTime1){
        final TextView listTime = listTime1;
        selectVod1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    listTime.setBackgroundResource(R.drawable.time_on);
                } else {
                    listTime.setBackgroundResource(R.drawable.time_off);

                }
            }
        });
    }

}
