package com.hp.gre3000;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hp.gre3000.adapter.WordAdapter;
import com.hp.gre3000.model.TodayLearnBean;
import com.hp.gre3000.model.WordBean;
import com.hp.gre3000.utils.DateUtil;
import com.hp.gre3000.utils.FileUtil;
import com.hp.gre3000.utils.SharedPre;
import com.hp.gre3000.utils.StatusBarUtils;
import com.hp.gre3000.utils.gson.GsonFix;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.hp.gre3000.LearningActivity.SHARE_TODAY_LIST_NUM_S;

public class WordLearningActivity extends AppCompatActivity {
    public static final String LIST_NUM_KEY = "listNumKey";
    public static final String POSITION_KEY = "positionKey";
    public static final String DATE_KEY = "dateKey";
    public static final String LIST_COUNT_KEY = "listCountKey";
    private RecyclerView word_list;
    private TextView listNumTv;
    private TextView itemCount;
    private List<List<WordBean>> list;
    private int listNum;
    private String path;
    private int position;
    private TextToSpeech tts;
    boolean isLastOnce;
    boolean isShowChinese;
    CheckBox checkbox;
    private WordAdapter wordAdapter;

    public static void start(Context context, String path, int listNum) {
        Intent intent = new Intent(context, WordLearningActivity.class);

        intent.putExtra("path", path);
        intent.putExtra("listNum", listNum);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setDarkFontStatus(this);
        setContentView(R.layout.activity_word_learning);
        Intent intent = getIntent();

        path = intent.getStringExtra("path");
        listNum = intent.getIntExtra("listNum", 0);
        position = SharedPre.getInt(path + POSITION_KEY);
        initView();
        initRecyclerView();
        String readLocalJson = FileUtil.readLocalJson(this, path);
        list = GsonFix.getInstance().getGson().fromJson(readLocalJson, new TypeToken<List<List<WordBean>>>() {
        }.getType());
        Log.e("hpo", "onCreate" + list);
        wordAdapter = new WordAdapter(list.get(listNum), isShowChinese);
        word_list.setAdapter(wordAdapter);


        word_list.scrollToPosition(position);
        listNumTv.setText("list" + (listNum + 1));
        itemCount.setText((position + 1) + "/" + list.get(listNum).size());
        initTextToSpeech();
    }

    private void initView() {
        word_list = findViewById(R.id.word_list);
        listNumTv = findViewById(R.id.listNum);
        itemCount = findViewById(R.id.itemCount);
        checkbox = findViewById(R.id.checkbox);
        isShowChinese = checkbox.isChecked();
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShowChinese = isChecked;
                wordAdapter.updateItem(isShowChinese);
            }
        });

    }

    private void initTextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {//实例化自带语音对象
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {//设置语音
                    tts.setLanguage(Locale.ENGLISH);
                    String english = list.get(listNum).get(position).getEnglish();
                    tts.speak(english, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        final PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(word_list);
        word_list.setLayoutManager(linearLayoutManager);
        word_list.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View snapView = pagerSnapHelper.findSnapView(recyclerView.getLayoutManager());
                    position = recyclerView.getLayoutManager().getPosition(snapView);
                    itemCount.setText((position + 1) + "/" + list.get(listNum).size());
                    String english = list.get(listNum).get(position).getEnglish();
                    tts.speak(english, TextToSpeech.QUEUE_FLUSH, null);

                    if (position == list.get(listNum).size() - 1) {
                        if (SharedPre.getLong(path + listNum + DATE_KEY) - DateUtil.getTimesMorning() != 0) {
                            SharedPre.set(path + listNum + DATE_KEY, DateUtil.getTimesMorning());
                            int count = SharedPre.getInt(path + listNum + LIST_COUNT_KEY);
                            SharedPre.set(path + listNum + LIST_COUNT_KEY, count + 1);
                            SharedPre.set(path + POSITION_KEY, 0);

                        }
                        String string = SharedPre.getString(path + SHARE_TODAY_LIST_NUM_S);
                        List<TodayLearnBean> listNumS = GsonFix.getInstance().getGson().fromJson(string, new TypeToken<List<TodayLearnBean>>() {
                        }.getType());
                        for (int i = 0; i < listNumS.size(); i++) {
                            if (listNumS.get(i).getListNum() == listNum) {
                                listNumS.get(i).setLearn(true);
                            }
                        }
                        SharedPre.set(path + SHARE_TODAY_LIST_NUM_S, GsonFix.getInstance().getGson().toJson(listNumS));
                        isLastOnce = true;
                    } else {

                        SharedPre.set(path + POSITION_KEY, position);
                    }

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        isLastOnce = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}