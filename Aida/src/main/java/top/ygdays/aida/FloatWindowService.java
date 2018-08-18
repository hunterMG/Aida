package top.ygdays.aida;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.*;
import android.widget.ImageButton;

public class FloatWindowService extends Service {

    //Log用的TAG
    private static final String TAG = "FFloatWindowService";
    //要引用的布局文件.
    ConstraintLayout floatWindowLayout;
    //布局参数.
    WindowManager.LayoutParams params;
    //实例化的WindowManager.
    WindowManager windowManager;

    ImageButton btn_start;
    ImageButton btn_stop;

    //状态栏高度.（接下来会用到）
    int statusBarHeight = -1;

    public FloatWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createFloatWindow();
    }

    @Override
    public void onDestroy() {
        if(btn_start != null){
            windowManager.removeView(floatWindowLayout);
        }
        super.onDestroy();
    }

    private void createFloatWindow() {
        //赋值WindowManager&LayoutParam.
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 450;
        params.y = 300;

        //设置悬浮窗口长宽数据.
        params.width = 60;
        params.height = 60;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        floatWindowLayout = (ConstraintLayout) inflater.inflate(R.layout.float_window,null);
        //添加toucherlayout
        windowManager.addView(floatWindowLayout,params);

        Log.i(TAG,"toucherlayout-->left:" + floatWindowLayout.getLeft());
        Log.i(TAG,"toucherlayout-->right:" + floatWindowLayout.getRight());
        Log.i(TAG,"toucherlayout-->top:" + floatWindowLayout.getTop());
        Log.i(TAG,"toucherlayout-->bottom:" + floatWindowLayout.getBottom());

        //主动计算出当前View的宽高信息.
        floatWindowLayout.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourceId > 0)
        {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(TAG,"状态栏高度为:" + statusBarHeight);

        //浮动窗口按钮.
        btn_start = floatWindowLayout.findViewById(R.id.btn_start);
        btn_stop = floatWindowLayout.findViewById(R.id.btn_stop);
        btn_stop.setVisibility(View.GONE);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_stop.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.GONE);

                Intent startInputService = new Intent(FloatWindowService.this, InputService.class);
                startService(startInputService);
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_start.setVisibility(View.VISIBLE);
                btn_stop.setVisibility(View.GONE);

                Intent stopInputService = new Intent(FloatWindowService.this, InputService.class);
                stopService(stopInputService);
            }
        });

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                params.x = (int) event.getRawX() - 30;
                params.y = (int) event.getRawY() - 30 - statusBarHeight;
                windowManager.updateViewLayout(floatWindowLayout,params);
                return false;
            }
        };

        btn_start.setOnTouchListener(touchListener);
        btn_stop.setOnTouchListener(touchListener);

    }
}
