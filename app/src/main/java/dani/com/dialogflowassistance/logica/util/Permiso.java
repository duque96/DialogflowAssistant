package dani.com.dialogflowassistance.logica.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permiso {

    private static Permiso permiso;

    private Permiso(){
    }

    public static Permiso getInstance(){
        if (permiso == null)
            permiso = new Permiso();

        return permiso;
    }

    public void validar(Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            solicitarPermiso(activity);
        }
    }

    private void solicitarPermiso(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, 200);
        }
    }
}
