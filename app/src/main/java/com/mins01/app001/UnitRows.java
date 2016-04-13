package com.mins01.app001;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 유닛 정보 싱글톤 (한곳에서 관리하기 위해서)
 * Created by mins01 on 2016-03-09.
 */
public class UnitRows {
    private static UnitRows instacne = null;
    public ArrayList<JSONObject> unit_rows = null;
    public int su_cnt = 0;
    public int su_cnt_all = 0;

    public UnitRows() {
        //unit_rows = new JSONArray();

    }

    public static synchronized UnitRows getInstance() {
        if (instacne == null) {
            instacne = new UnitRows();
        }
        return instacne;
    }

    public ArrayList getRows() {
        return unit_rows;
    }

    public JSONObject getRowByUnitIdx(String unit_idx) {
        return getRowByUnitIdx(Integer.parseInt(unit_idx, 10));
    }

    public JSONObject getRowByUnitIdx(int unit_idx) {
        if (unit_rows == null) return null;

        for (int i = 0, m = unit_rows.size(); i < m; i++) {
            try {
                if (unit_rows.get(i).getInt("unit_idx") == unit_idx) {
                    return unit_rows.get(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setRows(JSONArray jsonArray) {
        if (unit_rows == null) {
            unit_rows = new ArrayList<>();
        }
        unit_rows.clear();
        try {

            for (int i = 0, m = jsonArray.length(); i < m; i++) {
                unit_rows.add((JSONObject) jsonArray.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
