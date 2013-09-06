package com.example.qt;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qt.NetworkTask.AsyncResponse;


@SuppressLint("HandlerLeak")
public class MainActivity extends Activity implements AsyncResponse{

	public static int SUNDAY        = 1;
    public static int MONDAY        = 2;
    public static int TUESDAY       = 3;
    public static int WEDNSESDAY    = 4;
    public static int THURSDAY      = 5;
    public static int FRIDAY        = 6;
    public static int SATURDAY      = 7;
     
    private TextView mTvCalendarTitle;
    private GridView mGvCalendar;
     
    private ArrayList<DayInfo> mDayList;
    private CalendarAdapter mCalendarAdapter;
     
    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTvCalendarTitle = (TextView)findViewById(R.id.gv_calendar_activity_tv_title);
        mGvCalendar = (GridView)findViewById(R.id.gv_calendar_activity_gv_calendar);
		mDayList = new ArrayList<DayInfo>();
		
		Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
		Button button = (Button)findViewById( R.id.slide_updown );
		button.setTypeface(font);
		
		String[] item = {"up", "down", "question", "feeling", "action"};
		TextView text = null;
		String name = "";
		int id = 0;;
		for(int i=0;i<item.length;i++)
		{
			name = "qt_item_"+item[i];
			id = getResources().getIdentifier(name, "id", getPackageName());
			text = (TextView) findViewById( id );
			text.setTypeface(font);
		}
		
		SlidingDrawer slide = (SlidingDrawer) findViewById(R.id.slide);
		
		slide.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener(){
			@Override
			public void onDrawerOpened() {
				// TODO Auto-generated method stub
				Button button = (Button)findViewById( R.id.slide_updown );
				button.setText(R.string.icon_hand_down);
			}	
		});
		
		slide.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener(){
			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				Button button = (Button)findViewById( R.id.slide_updown );
				button.setText(R.string.icon_hand_up);
			}	
		});
		
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
		
				LinearLayout splash = (LinearLayout) findViewById(R.id.splash);
				RelativeLayout splash_main = (RelativeLayout) findViewById(R.id.splash_main);
				splash.setAnimation(AnimationUtils.loadAnimation( MainActivity.this, R.anim.disappear_to_right));
				splash_main.setAnimation(AnimationUtils.loadAnimation( MainActivity.this, R.anim.appear_to_left));
				splash.setVisibility(View.GONE);
			}
		};
		
		handler.sendEmptyMessageDelayed(0, 1000);
		
		NetworkTask networktask = new NetworkTask();
		String url = "http://qtzine.cesspoollife.com";
		networktask.execute(url);
		networktask.delegate = this;
		
	}

	@Override
    protected void onResume()
    {
        super.onResume();
      
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
     * 달력을 셋팅한다.
     * 
     * @param calendar 달력에 보여지는 이번달의 Calendar 객체
     */
    private void getCalendar(Calendar calendar)
    {
    	
    	Calendar c = Calendar.getInstance();
    	
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;
       
        mDayList.clear();
         
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
         
        calendar.add(Calendar.MONTH, -1);
 
        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, 1);
         
        if(dayOfMonth == SUNDAY)
            dayOfMonth += 7;
         
        lastMonthStartDay -= (dayOfMonth-1)-1;
         
        mTvCalendarTitle.setText(calendar.get(Calendar.YEAR) + "년 "
                + (calendar.get(Calendar.MONTH) + 1) + "월");
 
        DayInfo day;
        
        String[] week = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
  
        int i;
        for(i=0; i<7; i++)
        {
            day = new DayInfo();
            day.setDay(week[i]);
            day.setInMonth(true);
             
            mDayList.add(day);
        }
         
        for(i=0; i<dayOfMonth-1; i++)
        {
            int tmp = lastMonthStartDay+i;
            day = new DayInfo();
            day.setDay(Integer.toString(tmp));
            day.setInMonth(false);
             
            mDayList.add(day);
        }
        int row = (i + thisMonthLastDay)/7+1;
        for(i=1; i <= thisMonthLastDay; i++)
        {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setDate(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + i);
            day.setInMonth(true);
            if (i==c.get(Calendar.DAY_OF_MONTH))
            	day.isToday(true);
            mDayList.add(day);
        }
        for(i=1; i<row*7-(thisMonthLastDay+dayOfMonth-1)+1; i++)
        {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(false);
            mDayList.add(day);
        }
         
        initCalendarAdapter(); 
    }
    
    private void initCalendarAdapter()
    {
        mCalendarAdapter = new CalendarAdapter(this, R.layout.day, mDayList);
        mGvCalendar.setAdapter(mCalendarAdapter);
        mGvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        		if(mDayList.get(position).isInMonth()){
        			NetworkTask networktask = new NetworkTask();
        			String url = "http://qtzine.cesspoollife.com?date="+mDayList.get(position).getDate();
        			networktask.execute(url);
        			networktask.delegate = MainActivity.this;
        			SlidingDrawer slide = (SlidingDrawer) findViewById(R.id.slide);
        			slide.animateClose();
        		}
        	}
		});
    }
	
	public void processFinish(String output){
		try {
			JSONObject jo = new JSONObject(output);
			@SuppressWarnings("unchecked")
			Iterator<String> i = jo.keys();
			TextView text = null;
			String key;
			String name;
			int id;
			String value;
			while (i.hasNext()) {
				key = i.next();
				name = "qt_"+key;
				id = getResources().getIdentifier(name, "id", getPackageName());
				text = (TextView)findViewById(id);
				value = jo.getString(key);
				text.setText(value);
				if( value == "null" )
					text.setVisibility(View.GONE);
				else
					text.setVisibility(View.VISIBLE);
			}
		}catch (JSONException e)
		{
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
	}
}
