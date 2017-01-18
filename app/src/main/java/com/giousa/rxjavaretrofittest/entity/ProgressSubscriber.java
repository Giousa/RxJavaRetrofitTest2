package com.giousa.rxjavaretrofittest.entity;

import android.content.Context;
import android.util.Log;

import com.giousa.rxjavaretrofittest.utils.ProgressDialogHandler;

import rx.Subscriber;

/**
 * Description:
 * Author:Giousa
 * Date:2017/1/18
 * Email:65489469@qq.com
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private final String TAG = ProgressSubscriber.class.getSimpleName();

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private Context mContext;
    private ProgressDialogHandler mProgressDialogHandler;


    public ProgressSubscriber(SubscriberOnNextListener subscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = subscriberOnNextListener;
        this.mContext = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    @Override
    public void onStart() {
        super.onStart();
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        Log.d(TAG,"onCompleted");
        dismissProgressDialog();
    }


    @Override
    public void onError(Throwable e) {
        Log.d(TAG,"onError");
        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {
        Log.d(TAG,"onNext");
        mSubscriberOnNextListener.onNext(t);
    }


    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }
    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onCancelProgress() {
        if(!this.isUnsubscribed()){
            this.unsubscribe();
        }
    }
}
