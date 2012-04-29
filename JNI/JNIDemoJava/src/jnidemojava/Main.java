/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jnidemojava;

/**
 *
 * @author sam
 */
public class Main {

    /**
     * @param args the command line arguments
     */

    static {
        System.load("/home/sam/NetBeansProjects/JNIDemocd1/dist/libJNIDemoCdl.so");
       }

   

public static void main(String[] args) {
       new Main().nativePrint("i done it");
   }


   private native void nativePrint(String str);

    

}
