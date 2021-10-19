package arroon.android.login.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;

import arroon.android.login.R;

public class User extends AppCompatActivity {

    public static void start(Activity activity) {
        Explode explode = new Explode();
        explode.setDuration(500);

        activity.getWindow().setExitTransition(explode);
        activity.getWindow().setEnterTransition(explode);

        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        Intent starter = new Intent(activity, User.class);
        activity.startActivity(starter, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        Explode explode = new Explode();
        explode.setDuration(500);
        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);
    }
}
