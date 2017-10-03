package com.score.sts.presentation.model;

/**
 * Created by Who Dat on 10/1/2017.
 */

public enum TrackInfo{

    POSITION_NUMBER("POSITION_NUMBER"),
    CREATED_BY("CREATED_BY"),
    TRACK_NAME("TRACK_NAME"),
    TRACK_LENGTH("TRACK_LENGTH");

    String info;

    TrackInfo(String info){
        this.info= info;
    }
}
