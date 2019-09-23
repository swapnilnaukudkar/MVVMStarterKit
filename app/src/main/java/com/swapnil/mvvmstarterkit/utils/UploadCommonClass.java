package com.swapnil.mvvmstarterkit.utils;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.swapnil.mvvmstarterkit.R;
import com.swapnil.mvvmstarterkit.base.BaseActivity;
import com.swapnil.mvvmstarterkit.helper.LogHelper;
import com.swapnil.mvvmstarterkit.pojo.ImageTableEntity;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class UploadCommonClass {

    private static ProgressDialog pdConnection;
    private static final String TAG = "UploadCommonClass";

    public static void sendSingleImageToServer(final BaseActivity activity, String url, final String filepath, final String name, final String from) throws MalformedURLException {
        final ProgressDialog progressDialog = CommonUtils.showLoadingDialog(activity);
        String authToken = SharedPrefUtils.getString(activity, Constants.SPK_LOGIN, Constants.SPV_AUTH_TOKEN);
        final String uploadId = UUID.randomUUID().toString();
        MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(activity, uploadId, url);
        try {
            multipartUploadRequest.addFileToUpload(filepath, "media");
            multipartUploadRequest.addHeader("Content-Type", "application/json");
            multipartUploadRequest.addHeader("x-access-token", authToken);
            multipartUploadRequest.addParameter("name", name);
            multipartUploadRequest.addParameter("timezone", "Indian Standard Time");
            multipartUploadRequest.setNotificationConfig(new UploadNotificationConfig().setTitleForAllStatuses("Uploading image"));

            multipartUploadRequest.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                    Log.v("onprogress", "asd");
                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                    // Log.e("On Error", serverResponse.getBodyAsString());
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (activity.isNetworkConnected()) {
                        Toast.makeText(context, "Internet is slow. Please try again after sometime.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (from.equals(Constants.MAKE)) {
                            SharedPrefUtils.setBoolean(context, Constants.SPK_MAKE_OFFLINE, Constants.SPV_IS_MAKE_OFFLINE, true);
                        } else {
                            SharedPrefUtils.setBoolean(context, Constants.SPK_PLAY_OFFLINE, Constants.SPV_IS_PLAY_OFFLINE, true);
                        }

                        activity.setResult(RESULT_CANCELED);
                        activity.finish();
                    }
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    Log.v("onComplete", "asd");
                    LogHelper.printLog("On complete", serverResponse.getBodyAsString());

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                    activity.setResult(RESULT_OK);
                    activity.finish();
                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {

                }
            });

            multipartUploadRequest.startUpload();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void sendMultipleImageToServer(final BaseActivity activity,
                                                 final List<ImageTableEntity> imageTableEntityList,
                                                 String url,
                                                 final String from,
                                                 final int ctr) throws MalformedURLException {
        //final boolean[] isCompleted = {false};

        activity.runOnUiThread(new Runnable() {
            public void run() {
                pdConnection = new ProgressDialog(activity);
                pdConnection.setCancelable(false);
            }
        });

        String authToken = SharedPrefUtils.getString(activity, Constants.SPK_LOGIN, Constants.SPV_AUTH_TOKEN);
        final String uploadId = UUID.randomUUID().toString();
        MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(activity, uploadId,  url);
        try {
            multipartUploadRequest.addFileToUpload(imageTableEntityList.get(ctr).getFilePath(), "media");
            multipartUploadRequest.addHeader("Content-Type", "application/json");
            multipartUploadRequest.addHeader("x-access-token", authToken);
            multipartUploadRequest.addParameter("name", imageTableEntityList.get(ctr).getName());
            multipartUploadRequest.addParameter("timezone", "Indian Standard Time");
            multipartUploadRequest.setNotificationConfig(new UploadNotificationConfig().setTitleForAllStatuses("Uploading image"));

            long currentTime = System.currentTimeMillis();

            multipartUploadRequest.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                    if (from.equals(Constants.MAKE)) {
                        SharedPrefUtils.setBoolean(context, Constants.SPK_MAKE_OFFLINE, Constants.SPV_IS_MAKE_OFFLINE, false);
                    } else {
                        SharedPrefUtils.setBoolean(context, Constants.SPK_PLAY_OFFLINE, Constants.SPV_IS_PLAY_OFFLINE, false);
                    }
             /*       Log.d(TAG, "onProgress: uploaded bytes"+uploadInfo.getUploadedBytes());
                    Log.d(TAG, "onProgress: upload rate"+uploadInfo.getUploadRate());
                    Log.d(TAG, "onProgress: retries"+uploadInfo.getNumberOfRetries());
                    Log.d(TAG, "onProgress: total bytes"+uploadInfo.getTotalBytes());
                    Log.d(TAG, "onProgress: uploaded files left"+uploadInfo.getFilesLeft());
                    Log.d(TAG, "onProgress: progress percent"+uploadInfo.getProgressPercent());*/
                   /* if (System.currentTimeMillis()-currentTime>3000)
                    {
                        //jugad
                        //since on complete was not called everytime, So I assumed if user is spending more than 3 secs in uploading one image
                        //it is already uploaded so calling onComplete functionalities
                        Log.d(TAG, "onProgress: calling onComplete from onProgress");
                        deleteEntryFromDbAndCallRecursive(context, imageTableEntityList, ctr, activity);
                    }*/
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            pdConnection.setMessage("Uploading image " + ctr + "/" + imageTableEntityList.size());
                        }
                    });
                }

                @Override
                public void onError(final Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                    // Log.e("On Error", serverResponse.getBodyAsString());
                    Log.d(TAG, "onError: ");
                    if (from.equals(Constants.MAKE)) {
                        SharedPrefUtils.setBoolean(context, Constants.SPK_MAKE_OFFLINE, Constants.SPV_IS_MAKE_OFFLINE, true);

                    } else {
                        SharedPrefUtils.setBoolean(context, Constants.SPK_PLAY_OFFLINE, Constants.SPV_IS_PLAY_OFFLINE, true);
                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            if (pdConnection.isShowing()) {
                                pdConnection.dismiss();
                            }
                            if (activity.isNetworkConnected()) {
                                Toast.makeText(context, "Internet is slow. Please try again after sometime.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, R.string.err_no_internet, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    Log.d(TAG, "onCompleted: ");


                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    Log.d(TAG, "onCancelled: ");
                }
            });
            multipartUploadRequest.startUpload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void uploadProfilePic(Context context, String filepath, String name) {
        Log.d(TAG, "uploadProfilePic: " + filepath);


       /* RequestBody requestFile = null;
        File file = FileUtils.getFile(getMvpView().getContext(), fileUri);
        // create RequestBody instance from
        if (getMvpView().getContext().getContentResolver().getType(fileUri) != null) {
            requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            Log.d(TAG, "uploadProfilePic: reqFile: " + requestFile);
            MultipartBody.Part multipartFileBody = MultipartBody.Part.createFormData(Constants.IMAGE_UPLOAD_FILE, file.getName(), requestFile);
            RequestBody requestBody = RequestBody.create(MultipartBody.FORM, "Image description");
            ApiInterface apiInterface = getRCommonInstance().getAPIInterface(getMvpView(), ApiEndPoint.UPLOAD_CONNECTION_IMAGE, this, true, false);
            callAPI(apiInterface.uploadConnProfilePic(requestBody, multipartFileBody));
        }*/


        File file = FileUtils.getFile(context, Uri.parse(filepath));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart("media", null, requestFile)
                .addFormDataPart("name", "name")
                .addFormDataPart("time zone", "timezone")
                .build();

    }
}
