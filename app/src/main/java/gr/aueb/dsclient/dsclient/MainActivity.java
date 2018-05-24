package gr.aueb.dsclient.dsclient;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

public class MainActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private boolean isInt(String num){
        return num.matches("\\d+");
    }

    private boolean isPort(String port){
        return port.matches("\\d{1,5}");
    }

    private boolean isDouble(String num){
        return num.matches("\\d+[.,]\\d+");
    }

    private boolean isIpV4(String ip){
        return ip.matches("(\\d{1,3}.){3}\\d{1,3}");
    }

}
