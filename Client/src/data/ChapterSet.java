package data;

import enums.ChapterId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class ChapterSet {

    HashMap<ChapterId, ArrayList<String>> valueSet;

    public ChapterSet() {
        valueSet = new LinkedHashMap<>();
    }

    public void create() {
        ArrayList<String> c1 = new ArrayList<>();
        valueSet.put(ChapterId.CHAPTER_1, c1);

        ArrayList<String> c2 = new ArrayList<>();
        valueSet.put(ChapterId.CHAPTER_2, c2);

        ArrayList<String> c3 = new ArrayList<>();
        valueSet.put(ChapterId.CHAPTER_3, c3);

        ArrayList<String> c4 = new ArrayList<>();
        valueSet.put(ChapterId.CHAPTER_4, c4);


        ArrayList<String> c5 = new ArrayList<>();
        c5.add("人脸对比");
        c5.add("人脸检测");
        c5.add("人脸搜索");
        valueSet.put(ChapterId.CHAPTER_5, c5);

        ArrayList<String> c6 = new ArrayList<>();
        valueSet.put(ChapterId.CHAPTER_6, c6);
    }

    public ArrayList<String> get(ChapterId id) {
        return valueSet.get(id);
    }
}
