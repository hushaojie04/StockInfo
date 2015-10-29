package sj.http;

import android.os.Handler;


/**
 * Created by Administrator on 2015/4/20.
 */
public class NetworkDispatcher {
    MediatorDelivery mMediatorDelivery;

    public NetworkDispatcher(Handler handler) {
        mMediatorDelivery = new MediatorDelivery(handler);
    }

    public void dispatch(Request request) {
        new PostThread(request).start();

    }

    class PostThread extends Thread {
        private Request mRequest;

        public PostThread(Request request) {
            mRequest = request;
        }

        @Override
        public void run() {
            super.run();
            synchronized (mRequest) {
                if (mRequest != null) {
                    mRequest.postRequest();
                    mMediatorDelivery.postResponse(mRequest, mRequest.parseNetworkResponse(), null);
                    mRequest = null;
                }

            }
        }
    }
}
