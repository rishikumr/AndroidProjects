package com.dynasty.myapplication.utils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission_group.CAMERA;
import static androidx.core.app.ActivityCompat.startActivityForResult;

public class CaptureImage {

    private static URI picUri;


    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 110;

    
}
