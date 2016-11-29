package me.leefeng.recycleviewdemo;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import me.leefeng.lfrecyclerview.LFRecyclerView;
import me.leefeng.lfrecyclerview.OnItemClickListener;


/**
 * Created by limxing on 16/7/23.
 * <p/>
 * https://github.com/limxing
 * Blog: http://www.leefeng.me
 */
public class MainActivity extends AppCompatActivity implements OnItemClickListener,
        LFRecyclerView.LFRecyclerViewListener, LFRecyclerView.LFRecyclerViewScrollChange {
    private LFRecyclerView recycleview;
    private boolean b;
    private ArrayList<String> list;
    private MainAdapter adapter;

    LayoutInflater layoutInflater;
    ArrayList<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutInflater = LayoutInflater.from(this);

        list = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            list.add("leefeng.me" + i);
        }

        recycleview = (LFRecyclerView) findViewById(R.id.recycleview);
        recycleview.setLoadMore(true);
        recycleview.setRefresh(true);
        recycleview.setNoDateShow();
        recycleview.setAutoLoadMore(false);
        recycleview.setOnItemClickListener(this);
        recycleview.setLFRecyclerViewListener(this);
        recycleview.setScrollChangeListener(this);
        recycleview.setItemAnimator(new DefaultItemAnimator());

        final GridLayoutManager  manager = new GridLayoutManager(this,3);
        Log.d("TTTT","getSpanCount = " + manager.getSpanCount());
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override

            public int getSpanSize(int position) {

                int type = recycleview.getAdapter().getItemViewType(position);

                if(type == 1){
                    return 1;
                }else {
                    return manager.getSpanCount();
                }

            }

        });
        recycleview.setLayoutManager(manager);

        adapter = new MainAdapter(list);
        recycleview.setAdapter(adapter);


        View headerView = layoutInflater.inflate(R.layout.viewpager,null);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ViewPager viewPager = (ViewPager) headerView.findViewById(R.id.viewpager);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        View view1 = layoutInflater.inflate(R.layout.item,null);

        viewList.add(view1);

        View view2 = layoutInflater.inflate(R.layout.item,null);

        viewList.add(view2);


        PagerAdapter pagerAdapter = new MyAdatper();
        viewPager.setAdapter(pagerAdapter);


        recycleview.setHeaderView(headerView);
        recycleview.setFootText("ddddd");
    }






    class MyAdatper extends PagerAdapter {
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));


            return viewList.get(position);
        }
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(int po) {
        Toast.makeText(this, "Long:" + po, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                b = !b;
                list.add(0, "leefeng.me" + "==onRefresh");
                recycleview.stopRefresh(b);
                adapter.notifyItemInserted(0);
                adapter.notifyItemRangeChanged(0,list.size());

            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recycleview.stopLoadMore();
                list.add(list.size(), "leefeng.me" + "==onLoadMore");
                list.add(list.size(), "leefeng.me" + "==onLoadMore");
                adapter.notifyItemRangeInserted(list.size()-1,2);

            }
        }, 2000);
    }

    @Override
    public void onRecyclerViewScrollChange(View view, int i, int i1) {

    }
}
