package com.example.nanorus.gmobytesttask.view.profile;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Surface;
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
import java.util.List;

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
    final int CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_FRONT;
    String PICTURE_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        activity_profile_iv_avatar = (ImageView) findViewById(R.id.activity_profile_iv_avatar);
        activity_profile_iv_avatar.setOnClickListener(view -> mPresenter.onAvatarClicked());
        mPreview = (SurfaceView) findViewById(R.id.activity_profile_sv_photo);
        mPreview.setOnClickListener(view -> autoFocus());
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
            mCamera = Camera.open(CAMERA_ID);
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
                    mCamera = Camera.open(CAMERA_ID);
                    setPreviewSize(false);
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
        File saveDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
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
    public void autoFocus() {
        if (mCamera != null) {
            mCamera.startPreview();
            mCamera.autoFocus((b, camera) -> {
            });
        }
    }

    @Override
    public void takePhoto() {
        if (mCamera != null) {
            mCamera.startPreview();
            try {
                mCamera.takePicture(null, null, (bytes, camera1) -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            Matrix matrix = new Matrix();
                            matrix.postRotate(-90);
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
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    void setPreviewSize(boolean fullScreen) {

        // получаем размеры экрана
        Display display = getWindowManager().getDefaultDisplay();
        boolean widthIsMax = display.getWidth() > display.getHeight();

        // определяем размеры превью камеры
        Camera.Size size = mCamera.getParameters().getPreviewSize();

        RectF rectDisplay = new RectF();
        RectF rectPreview = new RectF();

        // RectF экрана, соотвествует размерам экрана
        rectDisplay.set(0, 0, display.getWidth(), display.getHeight());

        // RectF первью
        if (widthIsMax) {
            // превью в горизонтальной ориентации
            rectPreview.set(0, 0, size.width, size.height);
        } else {
            // превью в вертикальной ориентации
            rectPreview.set(0, 0, size.height, size.width);
        }

        Matrix matrix = new Matrix();
        // подготовка матрицы преобразования
        if (!fullScreen) {
            // если превью будет "втиснут" в экран (второй вариант из урока)
            matrix.setRectToRect(rectPreview, rectDisplay,
                    Matrix.ScaleToFit.START);
        } else {
            // если экран будет "втиснут" в превью (третий вариант из урока)
            matrix.setRectToRect(rectDisplay, rectPreview,
                    Matrix.ScaleToFit.START);
            matrix.invert(matrix);
        }
        // преобразование
        matrix.mapRect(rectPreview);

        // установка размеров surface из получившегося преобразования
        mPreview.getLayoutParams().height = (int) (rectPreview.bottom);
        mPreview.getLayoutParams().width = (int) (rectPreview.right);
    }

    void setCameraDisplayOrientation(int cameraId) {
        // определяем насколько повернут экран от нормального положения
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result = 0;

        // получаем инфо по камере cameraId
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        // задняя камера
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            result = ((360 - degrees) + info.orientation);
        } else
            // передняя камера
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = ((360 - degrees) - info.orientation);
                result += 360;
            }
        result = result % 360;
        mCamera.setDisplayOrientation(result);
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    private class HolderCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (mCamera != null) {
                try {
                    mCamera.setPreviewDisplay(holder);
                    mCamera.setDisplayOrientation(90);

                    Camera.Parameters params = mCamera.getParameters();
                    params.setPreviewSize(params.getSupportedPreviewSizes().get(0).width,
                            params.getSupportedPreviewSizes().get(0).height);
                    List<Camera.Size> sizes = params.getSupportedPictureSizes();
                    Camera.Size size = sizes.get(0);
                    for (int i = 0; i < sizes.size(); i++) {
                        if (sizes.get(i).width > size.width)
                            size = sizes.get(i);
                    }
                    params.setPictureSize(size.width, size.height);
                    params.setPictureFormat(ImageFormat.JPEG);

                    mCamera.setParameters(params);
                    mCamera.startPreview();
                    setPreviewSize(false);
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
                    setCameraDisplayOrientation(CAMERA_ID);
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