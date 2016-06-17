/* ********************************************************************************
 * All rights reserved.
 ******************************************************************************* */
package com.pack.brick.models;

public class Coordinates {
    private int x;
    private int y;
    private int z;
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getZ() {
        return z;
    }
    public void setZ(int z) {
        this.z = z;
    }
    
    public void set2DCoOrodinates(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public void set3DCoOrdinates(int x, int y , int z){
        this.z = z;
        this.y = y;
        this.x = x;
    }
    
    
}
