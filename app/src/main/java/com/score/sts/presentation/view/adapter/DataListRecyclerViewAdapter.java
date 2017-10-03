package com.score.sts.presentation.view.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.score.sts.R;
import com.score.sts.presentation.model.IGeneralContentDescription;
import com.score.sts.presentation.model.TrackInfo;
import com.score.sts.presentation.view.fragment.FeatureListFragment;

import java.util.List;

/**
 * Created by Who Dat on 9/18/2016.
 * This adapter will most likely be the base adapter
 */
public class DataListRecyclerViewAdapter extends RecyclerView.Adapter<DataListRecyclerViewAdapter.DataListViewHolder>{

    private static final String TAG = DataListRecyclerViewAdapter.class.getSimpleName();
    private List<IGeneralContentDescription> contentDescription;
    private View.OnClickListener itemListener;
    private DataListViewHolder dataListViewHolder;

    //---for the handler
    FeatureListFragment.FeatureListHandler featureListHandler;
    private Bundle bndlListInfo;
    private Message msgListInfo;

    public interface OnItemClickListener{
        void onItemClick(IGeneralContentDescription iGeneralContentDescription);
    }

    public DataListRecyclerViewAdapter(){}

    public DataListRecyclerViewAdapter(List<IGeneralContentDescription> contentDescription){
        this.contentDescription = contentDescription;
        Log.d(TAG, "Constructor called");
    }

    public DataListRecyclerViewAdapter(List<IGeneralContentDescription> contentDescription, FeatureListFragment.FeatureListHandler featureHandler){
        this.contentDescription = contentDescription;
        this.featureListHandler = featureHandler;
        Log.d(TAG, "Constructor called");
    }
    //---END CONSTRUCTORS---//

    @Override
    public DataListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        // inflate the item view and pass it to the view holder constructor
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_list, parent, false);
        dataListViewHolder = new DataListViewHolder(viewItem);
        return dataListViewHolder;
    }

    @Override
    public void onBindViewHolder(DataListViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");

        holder.bind(contentDescription.get(position), itemClickListener );
        holder.positionNumber.setText(contentDescription.get(position).getColumnData1());
        holder.createdBy.setText(contentDescription.get(position).getColumnData2());
        holder.trackName.setText(contentDescription.get(position).getColumnData3());
        holder.trackLength.setText(contentDescription.get(position).getColumnData4());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return contentDescription.size() ;
    }

    OnItemClickListener itemClickListener = (IGeneralContentDescription generalContentDescription) ->{
        msgListInfo = featureListHandler.obtainMessage();
        bndlListInfo = new Bundle();
        bndlListInfo.putString(TrackInfo.CREATED_BY.toString(), generalContentDescription.getColumnData2());
        msgListInfo.setData(bndlListInfo);
        featureListHandler.sendMessage(msgListInfo);
    };


    public static class DataListViewHolder extends RecyclerView.ViewHolder{

        public TextView positionNumber;
        public TextView trackName;
        public TextView createdBy;
        public TextView trackLength;

        public DataListViewHolder(View itemView) {
            super(itemView);
            positionNumber = (TextView) itemView.findViewById(R.id.tv_position_number);
            trackName = (TextView) itemView.findViewById(R.id.tv_track_name);
            createdBy = (TextView) itemView.findViewById(R.id.tv_created_by);
            trackLength = (TextView) itemView.findViewById(R.id.tv_track_length);
        }

        public void bind(final IGeneralContentDescription generalContentDescription, final OnItemClickListener itemClickListener){
            itemView.setOnClickListener( v->{
                itemClickListener.onItemClick(generalContentDescription);

            });
        }
    }
}
