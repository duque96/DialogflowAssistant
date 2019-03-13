package com.dani.dialogflowassistant.logica.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class ButtonAction {
    public void action(String[] extra, Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(extra[0])));
    }
}
