package me.drakeet.meizhi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.drakeet.meizhi.model.Meizhi;
import me.drakeet.meizhi.util.DateUtils;
import me.drakeet.meizhi.util.HttpUtils;
import me.drakeet.meizhi.util.TaskUtils;
import me.drakeet.meizhi.util.ToastUtils;

public class MainActivity extends SwipeRefreshBaseActivity {

    RecyclerView mRecyclerView;
    Handler mHandler;
    MeizhiListAdapter mMeizhiListAdapter;
    List<Meizhi> mMeizhiList;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        mMeizhiList = new ArrayList<>();
        mMeizhiList.addAll(OldMeizhi.init());
        setUpRecyclerView();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 358
        );
    }

    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_meizhi);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL
        );
        mRecyclerView.setLayoutManager(layoutManager);
        mMeizhiListAdapter = new MeizhiListAdapter(this, mMeizhiList);
        mRecyclerView.setAdapter(mMeizhiListAdapter);
//        mRecyclerView.addOnScrollListener(
//                new RecyclerView.OnScrollListener() {
//                    boolean isIdle;
//                    int mScrollY;
//
//                    @Override
//                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                        super.onScrollStateChanged(recyclerView, newState);
//                        isIdle = newState == RecyclerView.SCROLL_STATE_IDLE;
//                        if (isIdle) {
//                            mScrollY = 0;
//                        }
//                    }
//
//                    @Override
//                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                        super.onScrolled(recyclerView, dx, dy);
//                        mScrollY += dy;
//                        // show or hide header view
//                        if (mScrollY > 12) {
//                            hideOrShowToolbar();
//                        } else {
//                            hideOrShowToolbar();
//                        }
//                    }
//                }
//        );
    }

    private void getData() {
        setRefreshing(true);
        TaskUtils.executeAsyncTask(
                new AsyncTask<Object, Object, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Object... params) {
                        HttpUtils httpUtils = new HttpUtils();
                        Calendar calendar = Calendar.getInstance();
                        Date date = new Date();
                        calendar.set(2015, 5, 21);

                        int oLength = mMeizhiList.size();
                        while (date.after(calendar.getTime())) {
                            Meizhi meizhi = new Meizhi();
                            String dateString = DateUtils.toDate(date);
                            date = DateUtils.getLastdayDate(date);
                            List<Meizhi> qList = DataSupport.where("mid = ?", dateString)
                                                            .find(Meizhi.class);
                            if (qList.size() > 0) {
                                mMeizhiList.add(0, qList.get(0));
                                continue;
                            }
                            publishProgress(dateString);
                            meizhi.setMid(dateString);
                            String httpContent = httpUtils.download("http://gank.io/" + dateString);
                            int s0 = httpContent.indexOf("<img");
                            if (s0 == -1) {
                                continue;
                            }
                            int s1 = httpContent.indexOf("src=\"", s0) + "src=\"".length();
                            int e1 = httpContent.indexOf("\"", s1);
                            meizhi.setUrl(httpContent.substring(s1, e1));
                            mMeizhiList.add(0, meizhi);
                            meizhi.save();
                        }
                        return mMeizhiList.size() > oLength;
                    }

                    @Override
                    protected void onProgressUpdate(Object... values) {
                        super.onProgressUpdate(values);
                        ToastUtils.showShort("正在加载：" + values[0]);
                    }

                    @Override
                    protected void onPostExecute(Boolean o) {
                        super.onPostExecute(o);
                        if (o)
                            mMeizhiListAdapter.notifyDataSetChanged();
                        setRefreshing(false);
                    }
                }
        );

    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
