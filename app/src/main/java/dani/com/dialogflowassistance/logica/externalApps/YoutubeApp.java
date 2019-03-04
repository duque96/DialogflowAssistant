package dani.com.dialogflowassistance.logica.externalApps;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class YoutubeApp implements ExternalApp {
    private String videoID;
    private Context context;

    public YoutubeApp(String videoID, Context context) {
        this.videoID = videoID;
        this.context = context;
    }

    @Override
    public void execute() {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoID));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + videoID));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
