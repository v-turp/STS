package com.score.sts.presentation.model;

/**
 * Created by Who Dat on 9/18/2016.
 * This is an object for Video content that will
 * be housed in the the DataList.
 */
public class VideoContent implements IContentDescription {

    private static final String TAG = VideoContent.class.getSimpleName();
    private int trackPosition;
    private String trackName;
    private String trackCreator;
    private int trackLength;

    public VideoContent(){}

    public VideoContent(int trackPosition, String trackName, String trackCreator, int trackLength){
        this.trackPosition = trackPosition;
        this.trackName = trackName;
        this.trackCreator = trackCreator;
        this.trackLength =  trackLength;
    }

    @Override
    public void setTrackPosition(int trackPosition) {
        this.trackPosition = trackPosition;
    }

    @Override
    public void setTrackLength(int trackLength) {
        this.trackLength = trackLength;
    }

    @Override
    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    @Override
    public void setTrackCreator(String trackCreator) {
        this.trackCreator = trackCreator;
    }

    public static  IContentDescription getInstance(){
        IContentDescription contentDescription = new VideoContent();
        return contentDescription;
    }

    @Override
    public int getTrackPosition() {
        return trackPosition;
    }

    @Override
    public String getTrackName() {
        return trackName;
    }

    @Override
    public String getTrackCreator() {
        return trackCreator;
    }

    @Override
    public int getTrackLength() {
        return trackLength;
    }
}
