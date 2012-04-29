/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.museum.server;

/**
 *
 * @author student
 */



public class GStreamer {


    
    public native int runGstreamer(String path, String port, String ip);
    public native void killGst();
    public native void setPathGst(String path);
    
    static {
       // System.load("/home/student/netbeans/sams/src/lib/libGstServer.so");
        System.load("/home/sam/NetBeansProjects/JNIDemocd1/dist/libGstreamer.so");
        //System.loadLibrary("libGstreamer");
        
    }
    
    
}