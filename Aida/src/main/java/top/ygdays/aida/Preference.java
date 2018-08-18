package top.ygdays.aida;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @Author: Guang Yan
 * @Description: save and update user info
 * @Date: Created in 下午12:41 2018/1/6
 */
public class Preference {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    private static String tag = "Preference";

    private static String TAG_SPEECH_DELAY_MIN = "speech_delay_min";
    private static String TAG_SPEECH_DELAY_MAX = "speech_delay_max";
    private static String TAG_UI_DELAY_MIN = "ui_delay_min";
    private static String TAG_UI_DELAY_MAX = "ui_delay_max";


    public static String speech_1;
    public static String speech_2;
    public static String speech_3;
    public static String speech_4;
    public static String speech_5;

    public static int speechDelayMin;   //s
    public static int speechDelayMax;
    public static int uiDealyMin;   //ms
    public static int uiDelayMax;

    public static void init(Context context){
        sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //获取之前存储的话术
    public static void getEarlySpeech(){
        speech_1 = sharedPreferences.getString("speech_1", "");
        speech_2 = sharedPreferences.getString("speech_2", "");
        speech_3 = sharedPreferences.getString("speech_3", "");
        speech_4 = sharedPreferences.getString("speech_4", "");
        speech_5 = sharedPreferences.getString("speech_5", "");
        speechDelayMin = sharedPreferences.getInt(TAG_SPEECH_DELAY_MIN, 10);
        speechDelayMax = sharedPreferences.getInt(TAG_SPEECH_DELAY_MAX, 15);
        uiDealyMin = sharedPreferences.getInt(TAG_UI_DELAY_MIN, 2000);
        uiDelayMax = sharedPreferences.getInt(TAG_UI_DELAY_MAX, 4000);
        Log.i(tag, "get:"+speech_1);

    }

    //存储话术
    public static void setSpeech(String speech_1, String speech_2, String speech_3, String speech_4, String speech_5,
                                 int speechDelayMin, int speechDelayMax, int uiDealyMin, int uiDelayMax){
        editor.putString("speech_1", speech_1);
        editor.putString("speech_2", speech_2);
        editor.putString("speech_3", speech_3);
        editor.putString("speech_4", speech_4);
        editor.putString("speech_5", speech_5);
        editor.putInt(TAG_SPEECH_DELAY_MIN, speechDelayMin);
        editor.putInt(TAG_SPEECH_DELAY_MAX, speechDelayMax);
        editor.putInt(TAG_UI_DELAY_MIN, uiDealyMin);
        editor.putInt(TAG_UI_DELAY_MAX, uiDelayMax);
        editor.commit();
        Log.i(tag, speech_1+":"+speech_2+":"+speech_3+":"+speech_4+":"+speech_5);
    }
}
