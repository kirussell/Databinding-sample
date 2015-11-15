package ru.kirussell.databinding.sample;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static ru.kirussell.databinding.sample.SpannableUtils.*;

public class DisplayDataActivity extends AppCompatActivity {

    private ImageView roundAvatar;
    private ImageView grayScaleAvatar;
    private ImageView blurAvatar;
    private TextView name;
    private FriendsAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        Picasso.with(this).setLoggingEnabled(true);
        initViews();
        loadUser();
    }

    private void initViews() {
        roundAvatar = (ImageView) findViewById(R.id.roundAvatar);
        grayScaleAvatar = (ImageView) findViewById(R.id.grayScaleAvatar);
        blurAvatar = (ImageView) findViewById(R.id.blurAvatar);
        name = (TextView) findViewById(R.id.userName);
        final TextView about = (TextView) findViewById(R.id.about);
        final EditText editAbout = (EditText) findViewById(R.id.edAbout);
        editAbout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                about.setText(s.toString());
            }
        });
        final Button saveAbout = (Button) findViewById(R.id.saveAbout);
        final View aboutBlock = findViewById(R.id.aboutBlock);
        saveAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutBlock.setVisibility(View.GONE);
            }
        });
        ListView friendsList = (ListView) findViewById(R.id.friendsList);
        friendsAdapter = new FriendsAdapter();
        friendsList.setAdapter(friendsAdapter);
    }

    private void loadUser() {
        FakeApiService.requestUser(new FakeApiService.Callback<User>() {

            @Override
            public void onData(final User user) {
                DisplayDataActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            // round
                            Picasso.with(DisplayDataActivity.this)
                                    .load(user.avatarUrl)
                                    .fit()
                                    .transform(new CircleTransform())
                                    .into(roundAvatar);
                            // grayscale
                            ColorMatrix matrix = new ColorMatrix();
                            matrix.setSaturation(0);
                            grayScaleAvatar.setColorFilter(new ColorMatrixColorFilter(matrix));
                            Picasso.with(DisplayDataActivity.this)
                                    .load(user.avatarUrl)
                                    .into(grayScaleAvatar);
                            // blur
                            Picasso.with(DisplayDataActivity.this)
                                    .load(user.avatarUrl)
                                    .transform(new BlurTransform(DisplayDataActivity.this))
                                    .into(blurAvatar);
                            // name and age
                            name.setText(prepareNameAndAge(user));
                            if (friendsAdapter != null) {
                                friendsAdapter.setData(user.friends);
                            }
                        }
                    }
                });
            }
        });
    }

    private CharSequence prepareNameAndAge(Profile profile) {
        if (profile instanceof User) {
            return normal(profile.name + ", ", typeface(SANS_SERIF_LIGHT, String.valueOf(profile.age)));
        } else {
            return profile.name + ", " + profile.age;
        }
    }

    private class FriendsAdapter extends BaseDataAdapter<Friend> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder h;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_friend, null);
                h = new ViewHolder();
                h.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                h.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(h);
            } else {
                h = (ViewHolder) convertView.getTag();
            }
            Friend friend = getItem(position);
            Picasso.with(convertView.getContext())
                    .load(friend.avatarUrl)
                    .transform(new CircleTransform())
                    .into(h.avatar);
            h.name.setText(prepareNameAndAge(friend));
            return convertView;
        }

        class ViewHolder {
            public ImageView avatar;
            public TextView name;
        }
    }
}
