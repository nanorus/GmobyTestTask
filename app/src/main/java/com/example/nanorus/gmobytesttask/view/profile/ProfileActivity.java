package com.example.nanorus.gmobytesttask.view.profile;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.presenter.base.BasePresenterActivity;
import com.example.nanorus.gmobytesttask.presenter.base.PresenterFactory;
import com.example.nanorus.gmobytesttask.presenter.profile.IProfilePresenter;
import com.example.nanorus.gmobytesttask.presenter.profile.ProfilePresenter;
import com.example.nanorus.gmobytesttask.presenter.profile.ProfilePresenterFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileActivity extends BasePresenterActivity<ProfilePresenter, IProfileActivity> implements IProfileActivity {

    private IProfilePresenter mPresenter;

    private SurfaceView mPreview;
    private SurfaceHolder mSurfaceHolder;
    private HolderCallback holderCallback;
    private Camera mCamera;
    private byte[] mSavedPhoto;

    private ImageView activity_profile_iv_avatar;
    final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    final int MY_PERMISSIONS_REQUEST_STORAGE = 1;
    String PICTURE_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        activity_profile_iv_avatar = (ImageView) findViewById(R.id.activity_profile_iv_avatar);
        activity_profile_iv_avatar.setOnClickListener(view -> mPresenter.onAvatarClicked());
        mPreview = (SurfaceView) findViewById(R.id.activity_profile_sv_photo);
        mSurfaceHolder = mPreview.getHolder();
        holderCallback = new HolderCallback();
        mSurfaceHolder.addCallback(holderCallback);

        PICTURE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + getString(R.string.pictures_directory) + "profile_avatar.jpg";

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        updateAvatar();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mCamera = Camera.open();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // show an explanation
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCamera = Camera.open();
                } else {
                    Toast.makeText(this, "no access to camera", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    saveTakenPhoto();
                } else {
                    Toast.makeText(this, "no storage access", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @NonNull
    @Override
    protected String tag() {
        return null;
    }

    @NonNull
    @Override
    protected PresenterFactory<ProfilePresenter> getPresenterFactory() {
        return new ProfilePresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull ProfilePresenter presenter) {
        mPresenter = presenter;
    }

    private void saveTakenPhoto() {
        File saveDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState())
                + getString(R.string.pictures_directory));
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
        try {
            FileOutputStream saveImageStream = new FileOutputStream(PICTURE_PATH);
            saveImageStream.write(mSavedPhoto);
            saveImageStream.close();
            mSavedPhoto = null;
            updateAvatar();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mCamera != null)
            mCamera.startPreview();
    }

    private void updateAvatar() {
        Glide.with(this).load(PICTURE_PATH)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(activity_profile_iv_avatar);
    }

    @Override
    public void takePhoto() {
        if (mCamera != null) {
            try {
                mCamera.autoFocus((b, camera) -> camera.takePicture(null, null, (bytes, camera1) -> {
                    // rotate if portrait
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    mSavedPhoto = stream.toByteArray();

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        saveTakenPhoto();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            // show an explanation
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_STORAGE);
                        } else {
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_STORAGE);
                        }
                    }
                }));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    class HolderCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (mCamera != null) {
                try {
                    mCamera.setPreviewDisplay(holder);
                    mCamera.setDisplayOrientation(90);
                    mCamera.startPreview();
                    mCamera.autoFocus((b, camera) -> takePhoto());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            if (mCamera != null) {
                mCamera.stopPreview();
                try {
                    mCamera.setPreviewDisplay(holder);
                    mCamera.setDisplayOrientation(90);
                    mCamera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

    }

}