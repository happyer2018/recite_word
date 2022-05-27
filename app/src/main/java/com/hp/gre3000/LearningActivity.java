package com.hp.gre3000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hp.gre3000.model.TodayLearnBean;
import com.hp.gre3000.model.WordBean;
import com.hp.gre3000.utils.DateUtil;
import com.hp.gre3000.utils.FileUtil;
import com.hp.gre3000.utils.SharedPre;
import com.hp.gre3000.utils.StatusBarUtils;
import com.hp.gre3000.utils.gson.GsonFix;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hp.gre3000.WordLearningActivity.DATE_KEY;
import static com.hp.gre3000.WordLearningActivity.LIST_CHANGE_KEY;
import static com.hp.gre3000.WordLearningActivity.LIST_COUNT_KEY;
import static com.hp.gre3000.WordLearningActivity.POSITION_KEY;

public class LearningActivity extends AppCompatActivity {
    public static final String SHARE_TODAY = "shareToday";
    public static final String SHARE_TODAY_LIST_NUM_S = "shareTodayListNumS";
    public static final String SHARE_TODAY_TEXT = "shareTodayText";
    private String path;
    private List<List<WordBean>> list;
    private List<TodayLearnBean> listNumS = new ArrayList<>();
    private TextView content_tv;
    private TextView learn_tv;

    public static void start(Context context, String path) {
        Intent intent = new Intent(context, LearningActivity.class);
        intent.putExtra("path", path);
        context.startActivity(intent);
    }
    public void hideActionBar() {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getSupportActionBar().hide();
        } catch (Exception e) {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        StatusBarUtils.setTransparentStatusImmerse(this);
        StatusBarUtils.setDarkFontStatusImmerse(this);
        setContentView(R.layout.activity_learning);
        content_tv = findViewById(R.id.content_tv);
        learn_tv = findViewById(R.id.learn_tv);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        learn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listNumS.isEmpty()) {
                    int currentListNum = -1;
                    for (int i = 0; i < listNumS.size(); i++) {
                        if (!listNumS.get(i).isLearn()) {
                            currentListNum = listNumS.get(i).getListNum();
                            break;
                        }
                    }
                    if (currentListNum == -1) {
                        for (int i = 0; i < listNumS.size(); i++) {
                            listNumS.get(i).setLearn(false);
                            SharedPre.set(path + listNumS.get(i).getListNum() + LIST_CHANGE_KEY, "");
                        }
                        String s = GsonFix.getInstance().getGson().toJson(listNumS);
                        SharedPre.set(path + SHARE_TODAY_LIST_NUM_S, s);
                        currentListNum = listNumS.get(0).getListNum();

                    }
                    WordLearningActivity.start(LearningActivity.this, path, currentListNum);
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        String text = "";
        listNumS.clear();
        if (list == null) {
            String readLocalJson = FileUtil.readLocalJson(this, path);
            list = GsonFix.getInstance().getGson().fromJson(readLocalJson, new TypeToken<List<List<WordBean>>>() {
            }.getType());
        }
        long today = DateUtil.getTimesMorning();
        long shareToday = SharedPre.getLong(path + SHARE_TODAY);
        if (today != shareToday) {
            SharedPre.set(path + SHARE_TODAY, today);
            for (int i = 0; i < list.size(); i++) {
                int count = SharedPre.getInt(path + i + LIST_COUNT_KEY);
                long dateMill = SharedPre.getLong(path + i + DATE_KEY);
                Log.e("hpo,onResume: ", "iist" + i + "count" + count + "----" + ((dateMill - DateUtil.getTimesMorning()) / (24 * 60 * 60 * 1000)));
                long lag = DateUtil.getTimesMorning() - dateMill;
                if (count == 1 && lag >= 2 * 24 * 60 * 60 * 1000) {
                    TodayLearnBean todayLearnBean = new TodayLearnBean(i, false);
                    listNumS.add(todayLearnBean);
                    text = text + "\n" + "list" + (i + 1) + "复习";
                } else if (count == 2 && lag >= 3 * 24 * 60 * 60 * 1000) {
                    TodayLearnBean todayLearnBean = new TodayLearnBean(i, false);
                    listNumS.add(todayLearnBean);
                    text = text + "\n" + "list" + (i + 1) + "复习";
                } else if (count == 3 && lag >= 4 * 24 * 60 * 60 * 1000) {
                    TodayLearnBean todayLearnBean = new TodayLearnBean(i, false);
                    listNumS.add(todayLearnBean);
                    text = text + "\n" + "list" + (i + 1) + "复习";

                } else if (count == 0) {
                    TodayLearnBean todayLearnBean = new TodayLearnBean(i, false);
                    listNumS.add(todayLearnBean);
                    text = text + "\n" + "list" + (i + 1) + "学习";
                    break;
                }

            }
            String s = GsonFix.getInstance().getGson().toJson(listNumS);
            SharedPre.set(path + SHARE_TODAY_LIST_NUM_S, s);
            SharedPre.set(path + SHARE_TODAY_TEXT, text);
            SharedPre.set(path + POSITION_KEY, 0);
        } else {
            String string = SharedPre.getString(path + SHARE_TODAY_LIST_NUM_S);
            listNumS = GsonFix.getInstance().getGson().fromJson(string, new TypeToken<List<TodayLearnBean>>() {
            }.getType());
            text = SharedPre.getString(path + SHARE_TODAY_TEXT);

        }

        if (listNumS.isEmpty()) {
            learn_tv.setText("今日已完成");
            learn_tv.setEnabled(false);
        }else {

        }
        content_tv.setText(text);
    }
}