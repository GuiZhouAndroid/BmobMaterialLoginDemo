package com.yj.system.utils.singlepicker.PickerBean;

import java.io.Serializable;

public class PickerItem implements Serializable {
    //    private String ChinaPartyMember;//中共党员
//    private String ChinaGetSetPartyMember;//中共预备党员
//    private String CYouthLeagueMember;//共青团员
//    private String TheMasses;//群众
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PickerItem(int id, String name) {
        this.id = id;
        this.name = name;
    }
    @Override
    public String toString() {
        //重写该方法，作为选择器显示的名称
        return name;
    }
}
