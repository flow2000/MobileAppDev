package com.ph.schedule.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CurrentScheduleAdapter extends SimpleAdapter {

    // 数据List
    private final List<Map<String, Object>> arrayList;
    // viewId
    private final int[] viewId;
    // 上下文
    private Context mContext;
    // 未选中
    private final String scheduleDetailColor = "#000000";
    // 选中
    private final String scheduleStatusColor = "#00DFFF";

    private Date start_time;
    private Date end_time;
    private Date now_time;
    private Date target_time;

    /**
     * 参数分别为：
     * 上下文context，数据集 data，ListView，from，new int[]{ R.id.itemTitle,R.id.itemText}
     **/
    public CurrentScheduleAdapter(Context context, List<Map<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.arrayList = data;
        this.viewId = to;
    }

    //配置显示的总item数量
    @Override
    public int getCount() {
        return arrayList.size();
    }

    //按照位置获取数据对象
    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    //根据位置获取item的id
    @Override
    public long getItemId(int position) {
        return position;
    }

    //每个item的显示效果
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        target.setTime(new Date(System.currentTimeMillis() + 30 * 60 * 1000));
        String str1 = (String) arrayList.get(position).get("start_time");
        String str2 = (String) arrayList.get(position).get("end_time");
        String str3 = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);
        String str4 = target.get(Calendar.HOUR_OF_DAY) + ":" + target.get(Calendar.MINUTE);
        try {
            start_time = new SimpleDateFormat("HH:mm").parse(str1);
            end_time = new SimpleDateFormat("HH:mm").parse(str2);
            now_time = new SimpleDateFormat("HH:mm").parse(str3);
            target_time = new SimpleDateFormat("HH:mm").parse(str4);
            TextView status = view.findViewById(viewId[4]);
            if (isEffectiveDate(now_time, start_time, end_time)) {
                setColor(view); // 突出显示
                long from = now_time.getTime();
                long to = end_time.getTime();
                int minutes = (int) ((to - from) / (1000 * 60));
                if (minutes > 0) {
                    status.setText(minutes + "min后下课");
                }
                status.setTextColor(Color.parseColor(scheduleStatusColor));
            } else if (isEffectiveDate(target_time, start_time, end_time)) {
                setColor(view); // 突出显示
                long from = now_time.getTime();
                long to = start_time.getTime();
                int minutes = (int) ((to - from) / (1000 * 60));
                if (minutes > 0) {
                    status.setText(minutes + "min后上课");
                }
                status.setTextColor(Color.parseColor(scheduleStatusColor));
            } else if (compareTime(end_time, now_time)) {
                status.setText("已结束");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("error", "时间转换出错");
        }
        return view;
    }

    /**
     * 设置字体颜色
     * @param view 视图
     */
    private void setColor(View view) {
        for (int i = 0; i < 4; i++) {
            TextView v = view.findViewById(viewId[i]);
            v.setTextColor(Color.parseColor(scheduleDetailColor));
        }
    }

    /**
     * 判断当前时间是否在时间区间内
     */
    private static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime() || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        return nowTime.after(startTime) && nowTime.before(endTime);
    }

    /**
     * 比较时间
     *
     * @return
     */
    private static boolean compareTime(Date targetTime, Date currentTime) {
        return targetTime.getTime() < currentTime.getTime();
    }

}
