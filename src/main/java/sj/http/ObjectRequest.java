package sj.http;

/**
 * Created by Administrator on 2015/4/20.
 */
public abstract class ObjectRequest<T> extends Request<T> {
    public interface BufferUpdating {
        void onUpdate(long buffer_len, long total_len);
    }
    protected Response.Listener<T> mListener;
    public BufferUpdating mBufferUpdating;
    public ObjectRequest(int method, String url) {
        super(method, url);
    }

    public void setListener(Response.Listener<T> listener) {
        mListener = listener;
    }
    public void setBufferUpdating(BufferUpdating bufferUpdating)
    {
        mBufferUpdating = bufferUpdating;
    }
    @Override
    void deliverResponse(Response<T> response) {
        if (mListener != null)
            mListener.onResponse(this,response);
    }


}
