package top.ygdays.aida;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class Util {
    private static Toast toast;

    public static void showToast(Context context,
                                 String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    /**
     * 执行shell命令(中文会出错)
     *
     * @param cmd
     */
    public static void execShellCmd(String cmd) {

        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.writeBytes(cmd);
            Log.i("shell", cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static char randCharacter(){
        String alphabet =
                "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM`1234567890-=[];'\\,./~!@#$%^&*()_+{}:|?><";

        int len = alphabet.length();
        int randIndex = (int) (Math.random()*10);
        return alphabet.charAt(randIndex);
    }

    public static String randSpeech(){
        int randIndex = (int) (Math.random()*5+1);
        switch (randIndex){
            case 1:
                return Preference.speech_1;
            case 2:
                return Preference.speech_2;
            case 3:
                return Preference.speech_3;
            case 4:
                return Preference.speech_4;
            case 5:
                return Preference.speech_5;
            default:
                return Preference.speech_1;
        }
    }
}
