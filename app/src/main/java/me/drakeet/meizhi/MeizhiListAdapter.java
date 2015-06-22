package me.drakeet.meizhi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.drakeet.meizhi.model.Meizhi;

/**
 * Created by drakeet on 6/20/15.
 */
public class MeizhiListAdapter extends RecyclerView.Adapter<MeizhiListAdapter.ViewHolder> {

    private List<Meizhi> mList;
    private Context mContext;

    public MeizhiListAdapter(Context context, List<Meizhi> meizhiList) {
        mList = meizhiList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.item_meizhi, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Meizhi meizhi = mList.get(position);
        viewHolder.meizhi = meizhi;
        Picasso.with(mContext).load(meizhi.getUrl()).into(viewHolder.meizhiView);
        viewHolder.titleView.setText(meizhi.getMid());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Meizhi meizhi;
        ImageView meizhiView;
        TextView titleView;

        public ViewHolder(View itemView) {
            super(itemView);
            meizhiView = (ImageView) itemView.findViewById(R.id.iv_meizhi);
            titleView = (TextView) itemView.findViewById(R.id.tv_title);
            meizhiView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (meizhi == null) return;

            if (v == meizhiView) {
                Intent i = new Intent(mContext, PictureActivity.class);
                i.putExtra(PictureActivity.EXTRA_IMAGE_URL, meizhi.getUrl());
                i.putExtra(PictureActivity.EXTRA_IMAGE_TITLE, meizhi.getMid());

                if (mContext instanceof Activity) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            (Activity) mContext, meizhiView, PictureActivity.TRANSIT_PIC);
                    ActivityCompat.startActivity((Activity) mContext, i, optionsCompat.toBundle());
                } else {
                    mContext.startActivity(i);
                }
            }
        }
    }
}
