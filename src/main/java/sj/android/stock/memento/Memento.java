package sj.android.stock.memento;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayDeque;

import sj.android.stock.article.ArticleInfo;
import sj.utils.FileUtils;

/**
 * Created by Administrator on 2015/11/17.
 */
public class Memento implements Serializable {
    private ArrayDeque<ArticleInfo> queue = new ArrayDeque<ArticleInfo>();

    public Memento(ArrayDeque<ArticleInfo> queue) {
        this.queue.addAll(queue);
    }

    public ArrayDeque<ArticleInfo> getArrayDeque() {
        return queue;
    }
}
