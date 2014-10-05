package com.denniscourt.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import java.util.Locale;

/**
 * A transparent {@link Activity} displaying a "Stop" options menu to remove the {@link com.google.android.glass.timeline.LiveCard}.
 */
public class LiveCardMenuActivity extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private boolean initialized = false;
    private String queuedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tts = new TextToSpeech(this /* context */, this /* listener */);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Open the options menu right away.
        openOptionsMenu();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            initialized = true;
            tts.setLanguage(Locale.ENGLISH);

            if (queuedText != null) {
                speak(queuedText);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.heyo_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stop:
                // Stop the service which will unpublish the live card.
                stopService(new Intent(this, HeyoCardService.class));
                return true;
            case R.id.action_play:
                speak("Heyo stop Hayo stop Hey o stop Hey oh stop Hay oh");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        // Nothing else to do, finish the Activity.
        finish();
    }

    public void speak(String text) {
        // If not yet initialized, queue up the text.
        if (!initialized) {
            queuedText = text;
            return;
        }
        queuedText = null;
        // Before speaking the current text, stop any ongoing speech.
        tts.stop();
        // Speak the text.
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        // On any motion event (including touchpad tap), say 'Hello Glass'
        speak("Hello Glass");
        return true;
    }
}
