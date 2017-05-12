package com.vtg.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Windows 10 Gamer on 10/12/2016.
 */
public class ModelTimeData {

    public String name;
    public List<ModelSubData> listSubs;

    public ModelTimeData(){
        listSubs = new ArrayList<>();
        name = "";
    }
}
