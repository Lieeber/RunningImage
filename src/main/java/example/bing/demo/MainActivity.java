package example.bing.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima.viewpagertest.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ViewPager viewPager;
    private TextView tvDesc;
    /**
     * 用来装点点的布局
     */
    private LinearLayout pointGroup;

    // 图片资源ID
    private final int[] imageIds = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};
    // 图片标题集合
    private final String[] imageDescriptions = {"第一张图片", "第二张图片", "第三张图片", "第四张图片", "第五张图片"};
    private List<ImageView> imageList;
    /**
     * 页面切换后，的上一个位置
     */
    private int lastPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        tvDesc.setText(imageDescriptions[0]);
        pointGroup = (LinearLayout) findViewById(R.id.ll_point_group);

		/*
         * 准备工作
		 */
        imageList = new ArrayList<ImageView>();

        for (int i = 0; i < imageIds.length; i++) {
            ImageView image = new ImageView(this);
            image.setImageResource(imageIds[i]);
            imageList.add(image);
            /*
             * 添加指示点
			 */
            ImageView point = new ImageView(this);
            /*
			 * LayoutParams 的类型和要该view的父view的类型一致
			 */
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, -2);
            params.leftMargin = 15;
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.point_bg);
            /*
            默认进程序的时候第一个点事高亮的
             */
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }
            pointGroup.addView(point);

        }
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            /**
             * 当选择的页面发生改变的时候，回调此方法
             * position 选择的新的页面
             */
            public void onPageSelected(int position) {
                position = position % imageList.size();
                tvDesc.setText(imageDescriptions[position]);
                // 改变指示点的状态，
                // 让上一个位置的点，设置 enable 为 false
                pointGroup.getChildAt(lastPosition).setEnabled(false);
                // 让当前位置的点，设置enable 为true
                pointGroup.getChildAt(position).setEnabled(true);
                // 更新上一个位置的值
                lastPosition = position;

            }

            @Override
            /**
             * 当页面在滑动上，不断的调用
             */
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            /**
             * 当页面的滑动状发生改变的时候，回调 ，
             * 状态有：按下，滑动，抬起，
             */
            public void onPageScrollStateChanged(int state) {
            }
        });
		
		/*
		 * 设置viewPager当前页面的页面
		 * item 是页面的位置
		 */
        int item = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageList.size();
        viewPager.setCurrentItem(item);
		
		/*
		 * 实现动画的方式：
		 * 1、开子线程 while(true)  + Thread.sleep()
		 * 2、Timer
		 * 3、ClockManager 直接调用手机自身的功能
		 * 4、handler 
		 */
        isRunning = true;
        handler.sendEmptyMessageDelayed(99, 2000);

    }

    private boolean isRunning = false;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            if (isRunning) {
                handler.sendEmptyMessageDelayed(99, 2000);
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        /**
         * 返回页面的个数,为了实现图片的轮询播放,把图片的张数设置为了Integer.MAX_VALUE
         */
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        /**
         * 实例化相应的条目
         * position  该页面对应的位置
         * container 页面view的父view  其实就是viewPager 自身
         */
        public Object instantiateItem(ViewGroup container, int position) {
            // 1、根据当前position获得对应的view,并把view添加至container
            position = position % imageList.size();
            View view = imageList.get(position);
            container.addView(view);
            // 2、返回一个和view有关系的Object对象
            return view;
        }

        @Override
        /**
         * 判断view和object的对应关系
         * view 是 instantiateItem 方法中 添加至 container 的view对象
         * object 是 instantiateItem方法的返回值
         */
        public boolean isViewFromObject(View view, Object object) {
//			if(view == object){
//				return true;
//			}else{
//				return false;
//			}
            return view == object;
        }

        @Override
        /**
         * 销毁某一个页面
         * object instantiateItem方法的返回值
         */
        public void destroyItem(ViewGroup container, int position, Object object) {
            //下面这句，必须注掉，否则会抛异常
//			super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
