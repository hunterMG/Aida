package top.ygdays.aida;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;

public class InputService extends Service {

    //Log用的TAG
    private static final String TAG = "InputService";
    private int speechDelay;    //s
    private int uiDelay;    //ms
    private Context mContext;
    public InputService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        speechDelay = (int) (Preference.speechDelayMin+ Math.random()*(Preference.speechDelayMax-Preference.speechDelayMin));
        uiDelay = (int) (Preference.uiDealyMin+ Math.random()*(Preference.uiDelayMax-Preference.uiDealyMin));
        updateTimer();
        countDownTimer1.start();
        Log.i(TAG, "Create：speechDelay-"+ speechDelay +", uiDelay-"+ uiDelay);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "startCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        cancelTimer();
        super.onDestroy();
        Log.i(TAG, "destroy");
    }

    private void cancelTimer(){
        if (countDownTimer1 != null){
            countDownTimer1.cancel();
        }
        if (countDownTimer2 != null){
            countDownTimer2.cancel();
        }
        if (countDownTimer3 != null){
            countDownTimer3.cancel();
        }
        if (countDownTimer4 != null){
            countDownTimer4.cancel();
        }
        if (countDownTimer5 != null){
            countDownTimer5.cancel();
        }
        if (countDownTimer6 != null){
            countDownTimer6.cancel();
        }
    }

    private CountDownTimer countDownTimer1, countDownTimer2, countDownTimer3, countDownTimer4, countDownTimer5, countDownTimer6;

    private void updateTimer(){
        countDownTimer1 = new CountDownTimer(speechDelay *1000+500, 1000) {
            @Override
            public void onTick(long l) {
                Util.showToast(mContext, l/1000+"s 后开始本次脚本");
            }

            @Override
            public void onFinish() {
                // 在帖子列表界面点击一则帖子
                Shell.SU.run("input tap 340 450");
                countDownTimer2.start();
            }
        };

        countDownTimer2 = new CountDownTimer(uiDelay, 1000) {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                //点击评论输入框
                Shell.SU.run("input tap 120 780");
                countDownTimer3.start();
            }
        };
        countDownTimer3 = new CountDownTimer(uiDelay, 1000) {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                Log.i(TAG, "before input");

                String comment_s = Util.randSpeech();

                String comment = comment_s + Util.randCharacter() + "\n\n\n\n";
                String command = "am broadcast -a ADB_INPUT_TEXT --es msg '" +
                        comment + "'";
                //输入评论
                CommandResult result = Shell.SU.run(command);
                Log.i(TAG, "command: " + command);
                Log.i("CommandResult", result.getStdout());
                Log.i(TAG, "after input");
                countDownTimer4.start();
            }
        };
        countDownTimer4 = new CountDownTimer(uiDelay, 1000) {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                //点击发布按钮
                Shell.SU.run("input tap 450 675");
                countDownTimer5.start();
            }
        };
        countDownTimer5 = new CountDownTimer(uiDelay, 1000) {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                //按返回键
                Shell.SU.run("input keyevent 4");
                countDownTimer6.start();
                //随机下一次的延迟时间
                speechDelay = (int) (Preference.speechDelayMin+ Math.random()*(Preference.speechDelayMax-Preference.speechDelayMin));
                uiDelay = (int) (Preference.uiDealyMin+ Math.random()*(Preference.uiDelayMax-Preference.uiDealyMin));
            }
        };
        countDownTimer6 = new CountDownTimer(uiDelay, 1000) {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                //向上滑动屏幕（帖子列表页面）
//            CommandResult result = Shell.SU.run("input swipe 340 750 340 450");
//            Util.execShellCmd("input swipe 340 750 340 450");
//            Log.i(TAG, "swipe result: "+ result.getStdout());
//            Log.i(TAG, "swipe result: "+ result.getStderr());
                for(int i=0; i<8;i++){
                    Shell.SU.run("input keyevent 20");
                }
                // keyCode 20: down
                updateTimer();
                countDownTimer1.start();
            }
        };
    }


}
