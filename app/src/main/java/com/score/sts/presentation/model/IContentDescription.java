package com.score.sts.presentation.model;

/**
 * Created by Who Dat on 9/18/2016.
 */
public interface IContentDescription {

    void setTrackPosition( int trackPosition );
    int getTrackPosition();
    void setTrackLength( int trackLength );
    int getTrackLength();
    void setTrackName( String trackName );
    String getTrackName();
    void setTrackCreator(String trackCreatedBy );
    String getTrackCreator();

}
