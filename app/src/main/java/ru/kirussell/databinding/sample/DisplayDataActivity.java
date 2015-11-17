package ru.kirussell.databinding.sample;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.kirussell.databinding.sample.databinding.ActivityDisplayDataBinding;
import ru.kirussell.databinding.sample.databinding.ItemFriendBinding;

import static ru.kirussell.databinding.sample.SpannableUtils.*;

public class DisplayDataActivity extends AppCompatActivity {

    ActivityDisplayDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Picasso.with(this).setLoggingEnabled(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_display_data);
        loadUser();
    }

    private void loadUser() {
        FakeApiService.requestUser(new FakeApiService.Callback<User>() {

            @Override
            public void onData(final User user) {
                binding.setUser(new UserViewModel(user));
            }
        });
    }

    private static CharSequence prepareNameAndAge(Profile profile) {
        if (profile instanceof User) {
            return normal(profile.name + ", ", typeface(SANS_SERIF_LIGHT, String.valueOf(profile.age)));
        } else {
            return profile.name + ", " + profile.age;
        }
    }

    public
    static class FriendsAdapter extends BaseDataAdapter<Friend> {

        FriendsAdapter(List<Friend> data) {
            super();
            this.data.addAll(data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemFriendBinding binding;
            if (convertView == null) {
                binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.getContext()));
                convertView = binding.getRoot();
                convertView.setTag(binding);
            } else {
                binding = (ItemFriendBinding) convertView.getTag();
            }
            binding.setFriend(getItem(position));
            binding.executePendingBindings();
            return convertView;
        }
    }

    public static class UserViewModel extends BaseObservable {

        User user;
        public ObservableField<String> about;
        public ObservableBoolean hasAbout;
        public FriendsAdapter friends;

        UserViewModel(User user) {
            setUser(user);
        }

        public void setUser(User user) {
            this.user = user;
            about = new ObservableField<>(user.about);
            hasAbout = new ObservableBoolean(!TextUtils.isEmpty(user.about));
            friends = new FriendsAdapter(user.friends);
        }

        @Bindable
        public CharSequence getName() {
            return prepareNameAndAge(user);
        }

        @Bindable
        public String getAvatarUrl() {
            return user.avatarUrl;
        }

        public TextWatcher getAboutTextListener() {
            return new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = s.toString();
                    if (!str.equals(about.get())) {
                        about.set(str);
                    }
                }
            };
        }

        public void onSaveAbout(View view) {
            hasAbout.set(true);
        }
    }
}
