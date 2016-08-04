package com.numero.sojodia;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.numero.sojodia.Adapter.TimeTableRowAdapter;
import com.numero.sojodia.Model.TimeTableItem;
import com.numero.sojodia.Model.TimeTableRow;

import java.util.ArrayList;

public class TimeTableDialog {

    private AlertDialog.Builder builder;

    private ArrayList<TimeTableRow> rows;
    private ArrayList<TimeTableItem> timeTableItems;
    private TimeTableRowAdapter adapter;
    private TimeData timeData;
    private int route, reciprocating;

    public TimeTableDialog(Context context, int route, int reciprocating) {

        String reciprocatingStrings[] = {"登校", "下校"};
        String stationStrings[] = {"JR高槻駅北", "JR富田駅"};

        View view = LayoutInflater.from(context).inflate(R.layout.table_dialog, null);
        builder = new AlertDialog.Builder(context);
        builder.setTitle(reciprocatingStrings[reciprocating]);
        builder.setView(view);
        builder.setPositiveButton("閉じる", null);

        this.route = route;
        this.reciprocating = reciprocating;

        timeTableItems = new ArrayList<>();
        timeData = new TimeData();
        rows = new ArrayList<>();

        TextView stationTextView = (TextView) view.findViewById(R.id.station);
        stationTextView.setText(stationStrings[route]);

        initListView(context, view);
    }

    public void show() {
        AlertDialog dialog = builder.create();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
        initTimeTable();
    }

    private void initListView(Context context, View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TimeTableRowAdapter(rows, context);

        recyclerView.setAdapter(adapter);
    }

    private void initTimeTable() {
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                timeData.setAll(timeTableItems, route, reciprocating);
                setRowItem();
                handler.sendMessage(Message.obtain());
            }
        });
        thread.start();
    }

    private void setRowItem() {
        for (int i = 0; i < 18; i++) {
            TimeTableRow row = new TimeTableRow();
            row.setHourString((i + 6 > 9 ? "" + String.valueOf(i + 6) : "0" + String.valueOf(i + 6)));
            rows.add(row);
        }
        for (int i = 0; i < timeTableItems.size(); i++) {
            switch (timeTableItems.get(i).week) {
                case 0:
                    rows.get(timeTableItems.get(i).hour - 6).addStringTimeOnWeekday((timeTableItems.get(i).min > 9 ? "" + String.valueOf(timeTableItems.get(i).min) : "0" + String.valueOf(timeTableItems.get(i).min)));
                    break;
                case 1:
                    rows.get(timeTableItems.get(i).hour - 6).addStringTimeOnSaturday((timeTableItems.get(i).min > 9 ? "" + String.valueOf(timeTableItems.get(i).min) : "0" + String.valueOf(timeTableItems.get(i).min)));
                    break;
                case 2:
                    rows.get(timeTableItems.get(i).hour - 6).addStringTimeOnSunday((timeTableItems.get(i).min > 9 ? "" + String.valueOf(timeTableItems.get(i).min) : "0" + String.valueOf(timeTableItems.get(i).min)));
                    break;
                case 3:
                    rows.get(timeTableItems.get(i).hour - 6).addStringTimeOnHoliday((timeTableItems.get(i).min > 9 ? "" + String.valueOf(timeTableItems.get(i).min) : "0" + String.valueOf(timeTableItems.get(i).min)));
                    break;
            }
        }
    }

}