package com.score.sts.presentation.view.component.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.score.sts.R;

/**
 * Created by Who Dat on 4/11/2017.
 */

public class ImageDetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button button;
    private static final String DRAWABLE_RESOURCE = "resource";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_image);

        imageView = (ImageView) findViewById(R.id.img);
        button = (Button) findViewById(R.id.btnClose);

        int drawableResource = getIntent().getIntExtra(DRAWABLE_RESOURCE, 0);
        imageView.setImageResource(drawableResource);

        button.setOnClickListener( v ->{
            finish();
        });
    }
}
