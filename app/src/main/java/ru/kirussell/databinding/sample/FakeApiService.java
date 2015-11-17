package ru.kirussell.databinding.sample;

import android.support.annotation.WorkerThread;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by russellkim on 10/11/15.
 * Emulates api service, can provide user object with {@link FakeApiService#requestUser(Callback)} method
 */
public class FakeApiService {

    private static String[] NAMES = new String[]{
            "Bill Murray",
            "Sean Connery ",
            "Michael Caine",
            "Anthony Hopkins",
            "Harrison Ford",
            "Morgan Freeman",
            "Tom Hanks",
            "Jack Nicholson",
            "Gary Oldman",
            "Russell Crowe",
    };
    private static Random rand = new Random();

    public static void requestUser(final Callback<User> c) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e("FakeApiService", e.toString());
                }
                c.onData(createUser("Russell Kim", 26, generateAvatarUrl(), generateFriends(), null));
            }
        }).start();
    }

    private static User createUser(String name, int age, String avatar, List<Friend> friends, String about) {
        User user = new User();
        user.age = age;
        user.name = name;
        user.avatarUrl = avatar;
        user.friends = friends;
        user.about = about;
        return user;
    }

    private static List<Friend> generateFriends() {
        ArrayList<Friend> res = new ArrayList<>(10);
        for (String name : NAMES) {
            res.add(
                    createFriend(
                            name,
                            50 + rand.nextInt(10),
                            generateAvatarUrl(),
                            "I am awesome! Deal with it!"
                    )
            );
        }
        return res;
    }

    private static String generateAvatarUrl() {
        return "http://lorempixel.com/200/200/cats/" + rand.nextInt(10);
    }

    private static Friend createFriend(String name, int age, String avatar, String about) {
        Friend user = new Friend();
        user.age = age;
        user.name = name;
        user.avatarUrl = avatar;
        user.about = about;
        return user;
    }

    public interface Callback<T> {
        @WorkerThread
        void onData(T data);
    }
}
