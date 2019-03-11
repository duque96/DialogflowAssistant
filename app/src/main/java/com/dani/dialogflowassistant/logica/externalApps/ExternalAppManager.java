package com.dani.dialogflowassistant.logica.externalApps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class ExternalAppManager {
    public void display(String[] extra, Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(extra[0])));
    }
}
