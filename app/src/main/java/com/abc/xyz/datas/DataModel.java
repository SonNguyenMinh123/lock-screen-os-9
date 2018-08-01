package com.abc.xyz.datas;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DataModel {
    //format 01,02,....
    public static DecimalFormat formatter = new DecimalFormat("00");

    

    public static List<String> getWallpapers() {
        List<String> listSticker = new ArrayList<String>();
        for (int i = 0; i < 73; i++) {
            String aFormatted = formatter.format(i + 1);
            listSticker.add("wallpaper/wwp (" + aFormatted + ").jpg");
        }
        return listSticker;
    }

}
