/******************************************************************************************
* gstServer.c *
* Description: This file will setup gstreamer on the server side and stream audio to the *
* client.
* External globals: None *
* Author: James Sleeman *
* This is based on the Gstreamer hello world application which cam be found here: *
* http://gstreamer.freedesktop.org/data/doc/gstreamer/head/manual/html/chapter-helloworld.html
*****************************************************************************************/
#include <jni.h>
#include <stdio.h>
#include "gstreamer.h"

#include <gst/gst.h>
#include <glib.h>

#include <linux/limits.h> // not sure which one i need for MAX_PATH
#include <limits.h>
//#define STANDALONE 1

GMainLoop *loop;
GstElement *source, *pipeline;

static gboolean bus_call(GstBus *bus, GstMessage *msg, gpointer data)
{
  loop = (GMainLoop *) data;

  switch (GST_MESSAGE_TYPE (msg))
    {

    case GST_MESSAGE_EOS:
      g_print ("End of stream\n");
      g_main_loop_quit (loop);
      break;

    case GST_MESSAGE_ERROR:
      {
        gchar *debug;
        GError *error;

        gst_message_parse_error (msg, &error, &debug);
        g_free (debug);

        g_printerr ("Error: %s\n", error->message);
        g_error_free (error);

        g_main_loop_quit (loop);
        break;
      }

    default:
      break;
    }

  return TRUE;
}


#ifdef STANDALONE
int main (int argc, char *argv[])
#else

JNIEXPORT jint JNICALL Java_com_museum_server_GStreamer_runGstreamer
  (JNIEnv *env, jobject, jstring tpath, jstring tport, jstring tip)

  const char *ip;
ip = (char*) malloc (512);
  ip = (*env)->GetStringUTFChars(env, tip, 0);
  printf("%s", ip);
  (*env)->ReleaseStringUTFChars(env, ip, str);


  const char *path = (*env)->GetStringUTFChars(env, tpath, 0);
  printf("%s", path);
  (*env)->ReleaseStringUTFChars(env, path, str);

    const char *iport = (*env)->GetStringUTFChars(env, tport, 0);
  printf("%s", port);
  (*env)->ReleaseStringUTFChars(env,port, str);

  int port = atoi(iport);

#endif
{

  GstElement *sink;
  GstBus *bus;

  /* Initialisation */
  gst_init (NULL, NULL);
  loop = g_main_loop_new (NULL, FALSE);

  /* Create gstreamer elements */
  pipeline = gst_pipeline_new ("client");
  source = gst_element_factory_make ("filesrc", "file-source");
  sink = gst_element_factory_make ("autoaudiosink", "client");

  if (!pipeline || !source || !sink)
    {
      g_printerr ("One element could not be created. Exiting.\n");
      return -1;
    }

  /* Set up the pipeline */
  /* we set the input filename to the source element */
#ifdef STANDALONE
  g_object_set (G_OBJECT (source), "location", argv[1], NULL);
#else
  g_object_set (G_OBJECT (source), "location", path, NULL);
#endif
  g_object_set (G_OBJECT (source), "host", ip, NULL);
  g_object_set (G_OBJECT (source), "port", port, NULL);

  /* we add a message handler */
  bus = gst_pipeline_get_bus (GST_PIPELINE (pipeline));
  gst_bus_add_watch (bus, bus_call, loop);
  gst_object_unref (bus);

  /* we add all elements into the pipeline */
  /* source | sink */
  gst_bin_add_many (GST_BIN (pipeline), source, sink, NULL);


  /* we link the elements together */
  /* source -> sink */
  gst_element_link (source, sink);

  /* Set the pipeline to "playing" state*/
#ifdef STANDALONE
  g_print ("Now playing: %s\n", argv[1]);
#else
  g_print ("Now playing: %s\n", path);
#endif

  gst_element_set_state (pipeline, GST_STATE_PLAYING);


  /* Iterate */
  g_print ("Running...\n");
  g_main_loop_run (loop);


  /* Out of the main loop, clean up nicely */
  g_print ("Returned, stopping playback\n");
  gst_element_set_state (pipeline, GST_STATE_NULL);

  g_print ("Deleting pipline\n");
  gst_object_unref (GST_OBJECT (pipeline));

  return 0;
}

void killGst()
{
  g_main_loop_quit(loop);
}
void playGst()
{
  gst_element_set_state(pipeline, GST_STATE_PLAYING);
}
/*void stopGst()
{
  gst_element_set_state(pipeline, GST_STATE_STOPPED);
}*/
void setPathGst(char * path)
{
 g_object_set (G_OBJECT (source), "location", path, NULL);
}