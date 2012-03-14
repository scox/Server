    /**
     *
     * @author Sam
     * Date: 14/02/2012
     * Track.java: Reusable track bean containing information needed by the client
     * regarding an individual track
     * 
     */
package com.museum.javabeans;

public class Track {
    
    private String trackInfo;
    private String audioLocation;

    public String getAudioLocation() {
        return audioLocation;
    }

    public void setAudioLocation(String audioLocation) {
        this.audioLocation = audioLocation;
    }

    public String getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(String trackInfo) {
        this.trackInfo = trackInfo;
    }
 
    
    
    
}