package ru.kirussell.databinding.sample;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by russellkim on 11/11/15.
 * Adapter that holds generic data list
 */
public abstract class BaseDataAdapter<T> extends BaseAdapter {

    List<T> data = new ArrayList<>(10);

    public BaseDataAdapter() {
    }

    public void setData(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return data != null ? data.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
