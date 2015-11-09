package sj.android.stock;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/9.
 */
public class AsyncHandler extends Handler {
    WorkerHandler mWorkerHandler;
    HandlerThread workerThead;
    private static AsyncHandler mAsyncHandler;
    private static final int DOING = 0;
    private static final int DONE = 1;
    private Callback mCallback;

    private AsyncHandler() {
        if (mWorkerHandler == null) {
            workerThead = new HandlerThread("Async-Worker");
            workerThead.start();
            mWorkerHandler = new WorkerHandler(workerThead.getLooper());
        }
    }

    public void stopLooper() {
        if (workerThead != null) {
            workerThead.getLooper().quit();
        }
    }

    public void handle(Work work, Callback callback) {
        mCallback = callback;
        mWorkerHandler.work(work);
    }

    public static AsyncHandler getInstance() {
        if (mAsyncHandler == null)
            mAsyncHandler = new AsyncHandler();
        return mAsyncHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (mCallback != null) {
            mCallback.onCallback(msg.obj);
        }
    }

    class WorkerHandler extends Handler {
        private Work command;

        public WorkerHandler(Looper looper) {
            super(looper);
        }

        public void work(Work work) {
            command = work;
            WorkerHandler.this.sendEmptyMessage(0);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AsyncHandler.this.sendEmptyMessage(DOING);
            Message msg1 = new Message();
            msg1.obj = command.work();
            msg1.what = DONE;
            AsyncHandler.this.sendMessage(msg1);
        }
    }

    public interface Callback {
        void onCallback(Object object);
    }

    public interface Work {
        Object work();
    }


}
