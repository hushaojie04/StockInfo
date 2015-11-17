package sj.android.stock.memento;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import sj.android.stock.article.ArticleInfo;
import sj.utils.FileUtils;

/**
 * Created by Administrator on 2015/11/17.
 */
public class Caretaker {
    private static Caretaker mCaretaker = new Caretaker();

    private Caretaker() {

    }

    public static Caretaker getInstance() {
        if (mCaretaker == null) mCaretaker = new Caretaker();
        return mCaretaker;
    }

    public Memento getMemento(String key) {
        return (Memento) readObjectFromFile(key);
    }

    public void setMemento(String key, Memento memento) {
        writeObjectToFile(key, memento);
    }

    private static final String filename = "memento";


    private void writeObjectToFile(String key, Object obj) {
        File file = FileUtils.getDiskCacheDir(null, filename + key);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        }
    }

    private static Object readObjectFromFile(String key) {
        Object temp = null;
        File file = FileUtils.getDiskCacheDir(null, filename + key);
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            temp = objIn.readObject();
            objIn.close();
            System.out.println("read object success!");
        } catch (IOException e) {
            System.out.println("read object failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
