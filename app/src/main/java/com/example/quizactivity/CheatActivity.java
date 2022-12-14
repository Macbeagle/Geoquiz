package com.example.quizactivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.quizactivity.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.example.quizactivity.answer_shown";

    private static final String KEY_ANSWER_SHOWN = "answer_shown";
    private static final String KEY_ANSWER = "answer";

    private boolean mAnswerIsTrue;
    private boolean mIsAnswerShown;


    private TextView mAnswerTextView;
    private Button mShowAnswerButton;


    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        // get the data from quiz activity
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        // set up answer text view
        mAnswerTextView = findViewById(R.id.answer_text_view);

        if (savedInstanceState != null) {
            mIsAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN, false);
            mAnswerIsTrue = savedInstanceState.getBoolean(KEY_ANSWER, false);
        }

        // set up show answer button
        mShowAnswerButton = findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(v -> {
            mIsAnswerShown = true;
            showAnswer(true);
            setAnswerShownResult(mIsAnswerShown);
        });
        showAnswer(mIsAnswerShown);
        setAnswerShownResult(mIsAnswerShown);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    private void showAnswer(boolean isAnswerShown) {
        if (isAnswerShown) {
            if (mAnswerIsTrue) {
                mAnswerTextView.setText(R.string.true_button);
            } else {
                mAnswerTextView.setText(R.string.false_button);
            }
            int cx = mShowAnswerButton.getWidth() / 2;
            int cy = mShowAnswerButton.getHeight() / 2;
            float radius = mShowAnswerButton.getWidth();
            if (radius != 0) {
                Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mShowAnswerButton.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();
            } else {
                mShowAnswerButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, mIsAnswerShown);
        savedInstanceState.putBoolean(KEY_ANSWER, mAnswerIsTrue);
    }

}
