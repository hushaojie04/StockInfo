package sj.android.stock.memento;

import org.apache.commons.logging.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import sj.android.stock.article.ArticleInfo;
import sj.utils.LogUtils;

/**
 * Created by Administrator on 2015/11/17.
 */
public class Originator {
    private ArrayDeque<ArticleInfo> queue = new ArrayDeque<ArticleInfo>();
    private String key;

    public ArrayDeque<ArticleInfo> getDataQueue() {
        return queue;
    }

    public boolean isEmpty() {
        return queue.size() == 0;
    }

    public boolean isContain(int start) {
        return start < queue.size();
    }

    public ArticleInfo[] get(int start, int end) {
        ArticleInfo[] temp = null;
        if (start < queue.size()) {
            ArticleInfo[] array = new ArticleInfo[queue.size()];
            end = end > queue.size() ? queue.size() : end;
            queue.toArray(array);
            temp = new ArticleInfo[end - start];
            System.arraycopy(array, start, temp, 0, end - start);
        }
        return temp;
    }

    public void addFirst(ArticleInfo info) {
        queue.addFirst(info);
    }

    public void addFirstAll(List<ArticleInfo> infos) {
        ArrayDeque<ArticleInfo> temp = queue.clone();
        queue.clear();
        queue.addAll(infos);
        queue.addAll(temp);
    }

    public void addLastAll(List<ArticleInfo> infos) {
        queue.addAll(infos);
    }

    public void addLast(ArticleInfo info) {
        queue.addLast(info);
    }

    public Memento createMemento() {
        return new Memento(queue);
    }

    public void restoreMemento(Memento memento) {
        if (memento == null) {
            LogUtils.D("uuu", "memento==null");
            return;
        }
        this.queue.addAll(memento.getArrayDeque());
        LogUtils.D("restoreMemento queue size  ==" + queue.size());
    }
}
