package com.new1.listlive;

import java.util.ArrayList;

import com.new1.model.LiveChannel;
import com.new1.settop.R;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListLiveAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<LiveChannel> channelList;
    private int mLayout;
    private TextView listTime1;
    private TextView listTime2;
    private TextView listTime3;
    private TextView listTime4;
    private ListLiveActivity listLiveActivity;


    public ListLiveAdapter(Context context,
                           ArrayList<LiveChannel> channelList, TextView listTime1, TextView listTime2, TextView listTime3, TextView listTime4, ListLiveActivity listLiveActivity) {
        this.mContext = context;
        this.channelList = channelList;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listTime1 = listTime1;
        this.listTime2 = listTime2;
        this.listTime3 = listTime3;
        this.listTime4 = listTime4;
        this.listLiveActivity = listLiveActivity;
    }

    @Override
    public int getCount() {
        if (channelList != null)
            return channelList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return channelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LiveListViewHolder viewHolder;
        // ImageView iv = (ImageView)convertView.findViewById(R.id.imageView1);
        // iv.setImageResource(img[position]);
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.livelistrow, parent, false);

            viewHolder = new LiveListViewHolder();
            viewHolder.broadTitle = (TextView) convertView.findViewById(R.id.broadTitle);
            viewHolder.hasVod1 = (ImageView) convertView.findViewById(R.id.hasVod1);
            viewHolder.vodTime1 = (TextView) convertView.findViewById(R.id.vodTime1);
            viewHolder.vodTitle1 = (TextView) convertView.findViewById(R.id.vodTitle1);
            viewHolder.selectVod1 = (LinearLayout)  convertView.findViewById(R.id.selectVod1);
            viewHolder.hasVod2 = (ImageView) convertView.findViewById(R.id.hasVod2);
            viewHolder.vodTime2 = (TextView) convertView.findViewById(R.id.vodTime2);
            viewHolder.vodTitle2 = (TextView) convertView.findViewById(R.id.vodTitle2);
            viewHolder.selectVod2 = (LinearLayout)  convertView.findViewById(R.id.selectVod2);
            viewHolder.hasVod3 = (ImageView) convertView.findViewById(R.id.hasVod3);
            viewHolder.vodTime3 = (TextView) convertView.findViewById(R.id.vodTime3);
            viewHolder.vodTitle3 = (TextView) convertView.findViewById(R.id.vodTitle3);
            viewHolder.selectVod3 = (LinearLayout)  convertView.findViewById(R.id.selectVod3);
            viewHolder.hasVod4 = (ImageView) convertView.findViewById(R.id.hasVod4);
            viewHolder.vodTime4 = (TextView) convertView.findViewById(R.id.vodTime4);
            viewHolder.vodTitle4 = (TextView) convertView.findViewById(R.id.vodTitle4);
            viewHolder.selectVod4 = (LinearLayout)  convertView.findViewById(R.id.selectVod4);
            viewHolder.hasVod5 = (ImageView) convertView.findViewById(R.id.hasVod5);
            viewHolder.vodTime5 = (TextView) convertView.findViewById(R.id.vodTime5);
            viewHolder.vodTitle5 = (TextView) convertView.findViewById(R.id.vodTitle5);
            viewHolder.selectVod5 = (LinearLayout)  convertView.findViewById(R.id.selectVod5);
            viewHolder.hasVod6 = (ImageView) convertView.findViewById(R.id.hasVod6);
            viewHolder.vodTime6 = (TextView) convertView.findViewById(R.id.vodTime6);
            viewHolder.vodTitle6 = (TextView) convertView.findViewById(R.id.vodTitle6);
            viewHolder.selectVod6 = (LinearLayout)  convertView.findViewById(R.id.selectVod6);
            viewHolder.hasVod7 = (ImageView) convertView.findViewById(R.id.hasVod7);
            viewHolder.vodTime7 = (TextView) convertView.findViewById(R.id.vodTime7);
            viewHolder.vodTitle7 = (TextView) convertView.findViewById(R.id.vodTitle7);
            viewHolder.selectVod7 = (LinearLayout)  convertView.findViewById(R.id.selectVod7);
            viewHolder.hasVod8 = (ImageView) convertView.findViewById(R.id.hasVod8);
            viewHolder.vodTime8 = (TextView) convertView.findViewById(R.id.vodTime8);
            viewHolder.vodTitle8 = (TextView) convertView.findViewById(R.id.vodTitle8);
            viewHolder.selectVod8 = (LinearLayout)  convertView.findViewById(R.id.selectVod8);
            viewHolder.hasVod9 = (ImageView) convertView.findViewById(R.id.hasVod9);
            viewHolder.vodTime9 = (TextView) convertView.findViewById(R.id.vodTime9);
            viewHolder.vodTitle9 = (TextView) convertView.findViewById(R.id.vodTitle9);
            viewHolder.selectVod9 = (LinearLayout)  convertView.findViewById(R.id.selectVod9);
            viewHolder.hasVod10 = (ImageView) convertView.findViewById(R.id.hasVod10);
            viewHolder.vodTime10 = (TextView) convertView.findViewById(R.id.vodTime10);
            viewHolder.vodTitle10 = (TextView) convertView.findViewById(R.id.vodTitle10);
            viewHolder.selectVod10 = (LinearLayout)  convertView.findViewById(R.id.selectVod10);
            viewHolder.hasVod11 = (ImageView) convertView.findViewById(R.id.hasVod11);
            viewHolder.vodTime11 = (TextView) convertView.findViewById(R.id.vodTime11);
            viewHolder.vodTitle11 = (TextView) convertView.findViewById(R.id.vodTitle11);
            viewHolder.selectVod11 = (LinearLayout)  convertView.findViewById(R.id.selectVod11);
            viewHolder.hasVod12 = (ImageView) convertView.findViewById(R.id.hasVod12);
            viewHolder.vodTime12 = (TextView) convertView.findViewById(R.id.vodTime12);
            viewHolder.vodTitle12 = (TextView) convertView.findViewById(R.id.vodTitle12);
            viewHolder.selectVod12 = (LinearLayout)  convertView.findViewById(R.id.selectVod12);
            //viewHolder.btn1 = (Button) convertView.findViewById(R.id.btn1);
            //viewHolder.btn2 = (Button) convertView.findViewById(R.id.btn2);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LiveListViewHolder) convertView.getTag();
        }
        viewHolder.broadTitle.setText(channelList.get(position).getName());
        viewHolder.vodTitle1.setText("");
        viewHolder.vodTime1.setText("");
        viewHolder.vodTitle2.setText("");
        viewHolder.vodTime2.setText("");
        viewHolder.vodTitle3.setText("");
        viewHolder.vodTime3.setText("");
        viewHolder.vodTitle4.setText("");
        viewHolder.vodTime4.setText("");
        viewHolder.vodTitle5.setText("");
        viewHolder.vodTime5.setText("");
        viewHolder.vodTitle6.setText("");
        viewHolder.vodTime6.setText("");
        viewHolder.vodTitle7.setText("");
        viewHolder.vodTime7.setText("");
        viewHolder.vodTitle8.setText("");
        viewHolder.vodTime8.setText("");
        viewHolder.vodTitle9.setText("");
        viewHolder.vodTime9.setText("");
        viewHolder.vodTitle10.setText("");
        viewHolder.vodTime10.setText("");
        viewHolder.vodTitle11.setText("");
        viewHolder.vodTime11.setText("");
        viewHolder.vodTitle12.setText("");
        viewHolder.vodTime12.setText("");
        viewHolder.hasVod1.setVisibility(View.INVISIBLE);
        viewHolder.selectVod1.setFocusable(false);
        viewHolder.hasVod2.setVisibility(View.INVISIBLE);
        viewHolder.selectVod2.setFocusable(false);
        viewHolder.hasVod3.setVisibility(View.INVISIBLE);
        viewHolder.selectVod3.setFocusable(false);
        viewHolder.hasVod4.setVisibility(View.INVISIBLE);
        viewHolder.selectVod4.setFocusable(false);
        viewHolder.hasVod5.setVisibility(View.INVISIBLE);
        viewHolder.selectVod5.setFocusable(false);
        viewHolder.hasVod6.setVisibility(View.INVISIBLE);
        viewHolder.selectVod6.setFocusable(false);
        viewHolder.hasVod7.setVisibility(View.INVISIBLE);
        viewHolder.selectVod7.setFocusable(false);
        viewHolder.hasVod8.setVisibility(View.INVISIBLE);
        viewHolder.selectVod8.setFocusable(false);
        viewHolder.hasVod9.setVisibility(View.INVISIBLE);
        viewHolder.selectVod9.setFocusable(false);
        viewHolder.hasVod10.setVisibility(View.INVISIBLE);
        viewHolder.selectVod10.setFocusable(false);
        viewHolder.hasVod11.setVisibility(View.INVISIBLE);
        viewHolder.selectVod11.setFocusable(false);
        viewHolder.hasVod12.setVisibility(View.INVISIBLE);
        viewHolder.selectVod12.setFocusable(false);


        if (channelList.get(position).getListLiveDataList()[0] != null)
            if (channelList.get(position).getListLiveDataList()[0].size() > 0)
                if (channelList.get(position).getListLiveDataList()[0].get(0).getTitle() != null) {
                    viewHolder.vodTitle1.setText(channelList.get(position).getListLiveDataList()[0].get(0).getTitle());
                    viewHolder.vodTime1.setText(channelList.get(position).getListLiveDataList()[0].get(0).getTime());
                    viewHolder.hasVod1.setVisibility(View.VISIBLE);
                    viewHolder.selectVod1.setFocusable(true);
                    if(channelList.get(position).getListLiveDataList()[0].get(0).getHasVod().equals("2"))
                        viewHolder.hasVod1.setImageResource(R.drawable.rec_no1);
                    else if(channelList.get(position).getListLiveDataList()[0].get(0).getHasVod().equals("1"))
                        viewHolder.hasVod1.setImageResource(R.drawable.rec_pre1);
                }
        if (channelList.get(position).getListLiveDataList()[0] != null)
            if (channelList.get(position).getListLiveDataList()[0].size() > 1)
                if (channelList.get(position).getListLiveDataList()[0].get(1).getTitle() != null) {
                    viewHolder.vodTitle2.setText(channelList.get(position).getListLiveDataList()[0].get(1).getTitle());
                    viewHolder.vodTime2.setText(channelList.get(position).getListLiveDataList()[0].get(1).getTime());
                    viewHolder.hasVod2.setVisibility(View.VISIBLE);
                    viewHolder.selectVod2.setFocusable(true);
                    if(channelList.get(position).getListLiveDataList()[0].get(1).getHasVod().equals("2"))
                        viewHolder.hasVod2.setImageResource(R.drawable.rec_no1);
                    else if(channelList.get(position).getListLiveDataList()[0].get(1).getHasVod().equals("1"))
                        viewHolder.hasVod2.setImageResource(R.drawable.rec_pre1);
                }
        if (channelList.get(position).getListLiveDataList()[0] != null)
            if (channelList.get(position).getListLiveDataList()[0].size() > 2)
                if (channelList.get(position).getListLiveDataList()[0].get(2).getTitle() != null) {
                    viewHolder.vodTitle3.setText(channelList.get(position).getListLiveDataList()[0].get(2).getTitle());
                    viewHolder.vodTime3.setText(channelList.get(position).getListLiveDataList()[0].get(2).getTime());
                    viewHolder.hasVod3.setVisibility(View.VISIBLE);
                    viewHolder.selectVod3.setFocusable(true);
                    if(channelList.get(position).getListLiveDataList()[0].get(2).getHasVod().equals("2"))
                        viewHolder.hasVod3.setImageResource(R.drawable.rec_no1);
                    else if(channelList.get(position).getListLiveDataList()[0].get(2).getHasVod().equals("1"))
                        viewHolder.hasVod3.setImageResource(R.drawable.rec_pre1);
                }
        if (channelList.get(position).getListLiveDataList()[1] != null)
            if (channelList.get(position).getListLiveDataList()[1].size() > 0)
                if (channelList.get(position).getListLiveDataList()[1].get(0).getTitle() != null) {
                    viewHolder.vodTitle4.setText(channelList.get(position).getListLiveDataList()[1].get(0).getTitle());
                    viewHolder.vodTime4.setText(channelList.get(position).getListLiveDataList()[1].get(0).getTime());
                    viewHolder.hasVod4.setVisibility(View.VISIBLE);
                    viewHolder.selectVod4.setFocusable(true);
                    if(channelList.get(position).getListLiveDataList()[1].get(0).getHasVod().equals("2"))
                        viewHolder.hasVod4.setImageResource(R.drawable.rec_no1);
                    else if(channelList.get(position).getListLiveDataList()[1].get(0).getHasVod().equals("1"))
                        viewHolder.hasVod4.setImageResource(R.drawable.rec_pre1);
                }
        if (channelList.get(position).getListLiveDataList()[1] != null)
            if (channelList.get(position).getListLiveDataList()[1].size() > 1)
                if (channelList.get(position).getListLiveDataList()[1].get(1).getTitle() != null) {
                    viewHolder.vodTitle5.setText(channelList.get(position).getListLiveDataList()[1].get(1).getTitle());
                    viewHolder.vodTime5.setText(channelList.get(position).getListLiveDataList()[1].get(1).getTime());
                    viewHolder.hasVod5.setVisibility(View.VISIBLE);
                    viewHolder.selectVod5.setFocusable(true);
                    if(channelList.get(position).getListLiveDataList()[1].get(1).getHasVod().equals("2"))
                        viewHolder.hasVod5.setImageResource(R.drawable.rec_no1);
                    else if(channelList.get(position).getListLiveDataList()[1].get(1).getHasVod().equals("1"))
                        viewHolder.hasVod5.setImageResource(R.drawable.rec_pre1);
                }
        if (channelList.get(position).getListLiveDataList()[1] != null)
            if (channelList.get(position).getListLiveDataList()[1].size() > 2)
                if (channelList.get(position).getListLiveDataList()[1].get(2).getTitle() != null) {
                    viewHolder.vodTitle6.setText(channelList.get(position).getListLiveDataList()[1].get(2).getTitle());
                    viewHolder.vodTime6.setText(channelList.get(position).getListLiveDataList()[1].get(2).getTime());
                    viewHolder.hasVod6.setVisibility(View.VISIBLE);
                    viewHolder.selectVod6.setFocusable(true);
                    if(channelList.get(position).getListLiveDataList()[1].get(2).getHasVod().equals("2"))
                        viewHolder.hasVod6.setImageResource(R.drawable.rec_no1);
                    else if(channelList.get(position).getListLiveDataList()[1].get(2).getHasVod().equals("1"))
                        viewHolder.hasVod6.setImageResource(R.drawable.rec_pre1);
                }
        if (channelList.get(position).getListLiveDataList()[2] != null)
            if (channelList.get(position).getListLiveDataList()[2].size() > 0)
                if (channelList.get(position).getListLiveDataList()[2].get(0).getTitle() != null) {
                    viewHolder.vodTitle7.setText(channelList.get(position).getListLiveDataList()[2].get(0).getTitle());
                    viewHolder.vodTime7.setText(channelList.get(position).getListLiveDataList()[2].get(0).getTime());
                    viewHolder.hasVod7.setVisibility(View.VISIBLE);
                    viewHolder.selectVod7.setFocusable(true);
                    if(channelList.get(position).getListLiveDataList()[2].get(0).getHasVod().equals("2"))
                        viewHolder.hasVod7.setImageResource(R.drawable.rec_no1);
                    else if(channelList.get(position).getListLiveDataList()[2].get(0).getHasVod().equals("1"))
                        viewHolder.hasVod7.setImageResource(R.drawable.rec_pre1);
                }
        if (channelList.get(position).getListLiveDataList()[2] != null)
            if (channelList.get(position).getListLiveDataList()[2].size() > 1)
                if (channelList.get(position).getListLiveDataList()[2].get(1).getTitle() != null) {
                    viewHolder.vodTitle8.setText(channelList.get(position).getListLiveDataList()[2].get(1).getTitle());
                    viewHolder.vodTime8.setText(channelList.get(position).getListLiveDataList()[2].get(1).getTime());
                    viewHolder.hasVod8.setVisibility(View.VISIBLE);
                    viewHolder.selectVod8.setFocusable(true);
                    if(channelList.get(position).getListLiveDataList()[2].get(1).getHasVod().equals("2"))
                        viewHolder.hasVod8.setImageResource(R.drawable.rec_no1);
                    else if(channelList.get(position).getListLiveDataList()[2].get(1).getHasVod().equals("1"))
                        viewHolder.hasVod8.setImageResource(R.drawable.rec_pre1);
                }
        if (channelList.get(position).getListLiveDataList()[2] != null)
            if (channelList.get(position).getListLiveDataList()[2].size() > 2)
                if (channelList.get(position).getListLiveDataList()[2].get(2).getTitle() != null) {
                    viewHolder.vodTitle9.setText(channelList.get(position).getListLiveDataList()[2].get(2).getTitle());
                    viewHolder.vodTime9.setText(channelList.get(position).getListLiveDataList()[2].get(2).getTime());
                    viewHolder.hasVod9.setVisibility(View.VISIBLE);
                    viewHolder.selectVod9.setFocusable(true);
                    if(channelList.get(position).getListLiveDataList()[2].get(2).getHasVod().equals("2"))
                        viewHolder.hasVod9.setImageResource(R.drawable.rec_no1);
                    else if(channelList.get(position).getListLiveDataList()[2].get(2).getHasVod().equals("1"))
                        viewHolder.hasVod9.setImageResource(R.drawable.rec_pre1);
                }
        if (channelList.get(position).getListLiveDataList()[3] != null)
            if (channelList.get(position).getListLiveDataList()[3].size() > 0)
                if (channelList.get(position).getListLiveDataList()[3].get(0).getTitle() != null) {
                    viewHolder.vodTitle10.setText(channelList.get(position).getListLiveDataList()[3].get(0).getTitle());
                    viewHolder.vodTime10.setText(channelList.get(position).getListLiveDataList()[3].get(0).getTime());
                    viewHolder.hasVod10.setVisibility(View.VISIBLE);
                    viewHolder.selectVod10.setFocusable(true);
                    if(channelList.get(position).getListLiveDataList()[3].get(0).getHasVod().equals("2"))
                        viewHolder.hasVod10.setImageResource(R.drawable.rec_no1);
                    else if(channelList.get(position).getListLiveDataList()[3].get(0).getHasVod().equals("1"))
                        viewHolder.hasVod10.setImageResource(R.drawable.rec_pre1);
                }
        if (channelList.get(position).getListLiveDataList()[3] != null)
            if (channelList.get(position).getListLiveDataList()[3].size() > 1)
                if (channelList.get(position).getListLiveDataList()[3].get(1).getTitle() != null) {
                    viewHolder.vodTitle11.setText(channelList.get(position).getListLiveDataList()[3].get(1).getTitle());
                    viewHolder.vodTime11.setText(channelList.get(position).getListLiveDataList()[3].get(1).getTime());
                    viewHolder.hasVod11.setVisibility(View.VISIBLE);
                    viewHolder.selectVod11.setFocusable(true);
                    if(channelList.get(position).getListLiveDataList()[3].get(1).getHasVod().equals("2"))
                        viewHolder.hasVod11.setImageResource(R.drawable.rec_no1);
                    else if(channelList.get(position).getListLiveDataList()[3].get(1).getHasVod().equals("1"))
                        viewHolder.hasVod11.setImageResource(R.drawable.rec_pre1);
                }
        if (channelList.get(position).getListLiveDataList()[3] != null)
            if (channelList.get(position).getListLiveDataList()[3].size() > 2)
                if (channelList.get(position).getListLiveDataList()[3].get(2).getTitle() != null) {
                    viewHolder.vodTitle12.setText(channelList.get(position).getListLiveDataList()[3].get(2).getTitle());
                    viewHolder.vodTime12.setText(channelList.get(position).getListLiveDataList()[3].get(2).getTime());
                    viewHolder.hasVod12.setVisibility(View.VISIBLE);
                    viewHolder.selectVod12.setFocusable(true);
                    if(channelList.get(position).getListLiveDataList()[3].get(2).getHasVod().equals("2"))
                        viewHolder.hasVod12.setImageResource(R.drawable.rec_no1);
                    else if(channelList.get(position).getListLiveDataList()[3].get(2).getHasVod().equals("1"))
                        viewHolder.hasVod12.setImageResource(R.drawable.rec_pre1);
                }

        setOnFocus(viewHolder.selectVod1, listTime1);
        setOnFocus(viewHolder.selectVod2, listTime1);
        setOnFocus(viewHolder.selectVod3, listTime1);
        setOnFocus(viewHolder.selectVod4, listTime2);
        setOnFocus(viewHolder.selectVod5, listTime2);
        setOnFocus(viewHolder.selectVod6, listTime2);
        setOnFocus(viewHolder.selectVod7, listTime3);
        setOnFocus(viewHolder.selectVod8, listTime3);
        setOnFocus(viewHolder.selectVod9, listTime3);
        setOnFocus(viewHolder.selectVod10, listTime4);
        setOnFocus(viewHolder.selectVod11, listTime4);
        setOnFocus(viewHolder.selectVod12, listTime4);

        goLeft(viewHolder.selectVod1);
        goLeft(viewHolder.selectVod2);
        goLeft(viewHolder.selectVod3);
        goRight(viewHolder.selectVod10);
        goRight(viewHolder.selectVod11);
        goRight(viewHolder.selectVod12);

        //viewHolder.vodTitle2.setText(channelList.get(0).getListLiveDataList().get(0).getTitle());
        //viewHolder.vodTitle3.setText(channelList.get(0).getListLiveDataList().get(0).getTitle());

        return convertView;
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

    private void goLeft(LinearLayout selectVod){
        selectVod.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        listLiveActivity.goLeft();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void goRight(LinearLayout selectVod){
        selectVod.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        listLiveActivity.goRight();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
