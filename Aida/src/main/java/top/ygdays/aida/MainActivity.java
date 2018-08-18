package top.ygdays.aida;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Intent floatWindow;
    Context mContext;

    AutoCompleteTextView tv_speech_1;
    AutoCompleteTextView tv_speech_2;
    AutoCompleteTextView tv_speech_3;
    AutoCompleteTextView tv_speech_4;
    AutoCompleteTextView tv_speech_5;
    TextView tv_speech_delay_min;
    TextView tv_speech_delay_max;
    TextView tv_ui_delay_min;
    TextView tv_ui_delay_max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        if(Build.VERSION.SDK_INT >= 23){
            //当AndroidSDK>=23及Android版本6.0及以上时，需要获取OVERLAY_PERMISSION.
            //使用canDrawOverlays用于检查，下面为其源码。其中也提醒了需要在manifest文件中添加权限.
            /**
             * Checks if the specified context can draw on top of other apps. As of API
             * level 23, an app cannot draw on top of other apps unless it declares the
             * {@link android.Manifest.permission#SYSTEM_ALERT_WINDOW} permission in its
             * manifest, <em>and</em> the user specifically grants the app this
             * capability. To prompt the user to grant this approval, the app must send an
             * intent with the action
             * {@link android.provider.Settings#ACTION_MANAGE_OVERLAY_PERMISSION}, which
             * causes the system to display a permission management screen.
             *
             */
            if (Settings.canDrawOverlays(MainActivity.this)) {
                Util.showToast(mContext, "已开启悬浮窗权限");
            }else {
                //若没有权限，提示获取.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                Util.showToast(mContext, "需要取得权限以使用悬浮窗");
                startActivity(intent);
            }
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        Button btn_start_service = findViewById(R.id.btn_start_service);
        Button btn_stop_service = findViewById(R.id.btn_stop_service);
        tv_speech_1 = findViewById(R.id.tv_speech_1);
        tv_speech_2 = findViewById(R.id.tv_speech_2);
        tv_speech_3 = findViewById(R.id.tv_speech_3);
        tv_speech_4 = findViewById(R.id.tv_speech_4);
        tv_speech_5 = findViewById(R.id.tv_speech_5);
        tv_speech_delay_min = findViewById(R.id.tv_speech_daley_min);
        tv_speech_delay_max = findViewById(R.id.tv_speech_daley_max);
        tv_ui_delay_min = findViewById(R.id.tv_ui_daley_min);
        tv_ui_delay_max = findViewById(R.id.tv_ui_daley_max);

        Preference.init(mContext);
        Preference.getEarlySpeech();

        tv_speech_1.setText(Preference.speech_1);
        tv_speech_2.setText(Preference.speech_2);
        tv_speech_3.setText(Preference.speech_3);
        tv_speech_4.setText(Preference.speech_4);
        tv_speech_5.setText(Preference.speech_5);
        tv_speech_delay_min.setText(Integer.toString(Preference.speechDelayMin));
        tv_speech_delay_max.setText(Integer.toString(Preference.speechDelayMax));
        tv_ui_delay_min.setText(Integer.toString(Preference.uiDealyMin));
        tv_ui_delay_max.setText(Integer.toString(Preference.uiDelayMax));

        floatWindow = new Intent(this, FloatWindowService.class);

        btn_start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptStartScript();
            }
        });
        btn_stop_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(floatWindow);
            }
        });

        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void attemptStartScript(){
        //reset errors
        tv_speech_1.setError(null);
        tv_speech_2.setError(null);
        tv_speech_3.setError(null);
        tv_speech_4.setError(null);
        tv_speech_5.setError(null);
        tv_ui_delay_min.setError(null);
        tv_ui_delay_max.setError(null);
        tv_speech_delay_min.setError(null);
        tv_speech_delay_max.setError(null);

        boolean cancel = false;
        View focusView = null;

        String speech_1 = tv_speech_1.getText().toString().trim();
        String speech_2 = tv_speech_2.getText().toString().trim();
        String speech_3 = tv_speech_3.getText().toString().trim();
        String speech_4 = tv_speech_4.getText().toString().trim();
        String speech_5 = tv_speech_5.getText().toString().trim();
        String speech_delay_min = tv_speech_delay_min.getText().toString().trim();
        String speech_delay_max = tv_speech_delay_max.getText().toString().trim();
        String ui_delay_min = tv_ui_delay_min.getText().toString().trim();
        String ui_delay_max = tv_ui_delay_max.getText().toString().trim();

        if(TextUtils.isEmpty(speech_1)){
            tv_speech_1.setError(getString(R.string.speech_empty_error));
            focusView = tv_speech_1;
            cancel = true;
        }else if(TextUtils.isEmpty(speech_2)){
            tv_speech_2.setError(getString(R.string.speech_empty_error));
            focusView = tv_speech_2;
            cancel = true;
        }else if(TextUtils.isEmpty(speech_3)){
            tv_speech_3.setError(getString(R.string.speech_empty_error));
            focusView = tv_speech_3;
            cancel = true;
        }else if(TextUtils.isEmpty(speech_4)){
            tv_speech_4.setError(getString(R.string.speech_empty_error));
            focusView = tv_speech_4;
            cancel = true;
        }else if(TextUtils.isEmpty(speech_5)){
            tv_speech_5.setError(getString(R.string.speech_empty_error));
            focusView = tv_speech_5;
            cancel = true;
        }else if(TextUtils.isEmpty(speech_delay_min)){
            tv_speech_delay_min.setError(getString(R.string.speech_empty_error));
            focusView = tv_speech_delay_min;
            cancel = true;
        }else if(TextUtils.isEmpty(speech_delay_max)){
            tv_speech_delay_max.setError(getString(R.string.speech_empty_error));
            focusView = tv_speech_delay_max;
            cancel = true;
        }else if (TextUtils.isEmpty(ui_delay_min)){
            tv_ui_delay_min.setError(getString(R.string.speech_empty_error));
            focusView = tv_ui_delay_min;
            cancel = true;
        }else if(TextUtils.isEmpty(ui_delay_max)){
            tv_ui_delay_max.setError(getString(R.string.speech_empty_error));
            focusView = tv_ui_delay_max;
            cancel = true;
        }else {
            Preference.speechDelayMin = Integer.parseInt(speech_delay_min);
            Preference.speechDelayMax = Integer.parseInt(speech_delay_max);
            Preference.uiDealyMin = Integer.parseInt(ui_delay_min);
            Preference.uiDelayMax = Integer.parseInt(ui_delay_max);
        }

        if(cancel){
            focusView.requestFocus();
        }else {
            Preference.setSpeech(speech_1, speech_2, speech_3, speech_4, speech_5, Preference.speechDelayMin,
                    Preference.speechDelayMax, Preference.uiDealyMin, Preference.uiDelayMax);
            startService(floatWindow);
            Util.showToast(mContext, "配置已更新，悬浮窗启动");
        }
    }
}
