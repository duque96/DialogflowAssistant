package dani.com.dialogflowassistance.logica.externalApps;

import android.content.Context;

public class ExternalAppManage {
    private ExternalApp externalApp;

    public void display(String[] extra, Context context){
        if (extra[0].equals("YouTube")){
            externalApp = new YoutubeApp(extra[1], context);
        }

        externalApp.execute();
    }
}
