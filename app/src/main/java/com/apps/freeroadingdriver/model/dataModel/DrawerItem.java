package com.apps.freeroadingdriver.model.dataModel;

/**
 * Created by Harshil on 11/30/2017.
 */

public class DrawerItem {
    String name;
    int icon;
    String name2;

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public DrawerItem(String name, int icon,String name2) {
        this.name = name;
        this.icon = icon;
        this.name2=name2;
    }

    public DrawerItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
