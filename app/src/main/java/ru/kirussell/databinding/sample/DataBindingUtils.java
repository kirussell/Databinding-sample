package ru.kirussell.databinding.sample;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.graphics.Color;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

/**
 * Created by russellkim on 15/11/15.
 * Data binding customization
 */
@SuppressWarnings("unused")
public class DataBindingUtils {

    @BindingAdapter("bind:roundImageUrl")
    public static void setRoundImage(ImageView image, String imageUrl) {
        Picasso.with(image.getContext())
                .load(imageUrl)
                .transform(new CircleTransform())
                .into(image);
    }

    @BindingAdapter("bind:imageUrl")
    public static void setImage(ImageView image, String imageUrl) {
        Picasso.with(image.getContext())
                .load(imageUrl)
                .into(image);
    }

    @BindingAdapter("bind:blurImageUrl")
    public static void setBlurImage(ImageView image, String imageUrl) {
        Context ctx = image.getContext();
        Picasso.with(ctx)
                .load(imageUrl)
                .transform(new BlurTransform(ctx))
                .into(image);
    }

    @BindingAdapter("android:addTextChangedListener")
    public static void addTextChangedListener(EditText edit, TextWatcher watcher) {
        if (watcher != null) {
            edit.addTextChangedListener(watcher);
        }
    }

    @BindingAdapter("bind:listAdapter")
    public static void setListAdapter(ListView list, ListAdapter adapter) {
        list.setAdapter(adapter);
    }
}
