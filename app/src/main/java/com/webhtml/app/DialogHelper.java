package com.webhtml.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

public class DialogHelper {

    public static void showCustomAlert(Activity activity, String message, JsResult result) {
        activity.runOnUiThread(() -> {
            float density = activity.getResources().getDisplayMetrics().density;

            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);

            int padHorizontal = (int) (16 * density);
            int padTop = (int) (14 * density);
            int padBottom = (int) (12 * density);

            layout.setPadding(padHorizontal, padTop, padHorizontal, padBottom);

            GradientDrawable backgroundDrawable = new GradientDrawable();
            backgroundDrawable.setColor(Color.parseColor("#1A1A1C"));
            backgroundDrawable.setCornerRadius(10 * density);
            backgroundDrawable.setStroke((int) (0.9f * density), Color.parseColor("#7C874F"));
            layout.setBackground(backgroundDrawable);

            TextView titleView = new TextView(activity);
            titleView.setText("Alert!");
            titleView.setTextColor(Color.parseColor("#CBD868"));
            titleView.setTextSize(16);
            titleView.setTypeface(null, Typeface.BOLD);
            titleView.setGravity(Gravity.CENTER);
            titleView.setPadding(0, (int) (3 * density), 0, (int) (16 * density));
            layout.addView(titleView);

            TextView messageView = new TextView(activity);
            messageView.setText(message);
            messageView.setTextColor(Color.parseColor("#BAB9BF"));
            messageView.setTextSize(15);
            messageView.setLineSpacing(0, 1.4f);
            messageView.setGravity(Gravity.START);
            messageView.setPadding((int) (6 * density), (int) (20 * density), 0, (int) (32 * density));
            layout.addView(messageView);

            TextView closeButton = createStyledButton(activity, "Close", "#000000", 13.5f, density);

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    (int) (90 * density),
                    (int) (38 * density)
            );

            LinearLayout buttonLayout = new LinearLayout(activity);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            buttonLayout.setPadding(0, (int) (12 * density), 0, 0);
            buttonLayout.addView(closeButton, btnParams);
            layout.addView(buttonLayout);

            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setView(layout)
                    .setCancelable(false)
                    .create();

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            closeButton.setOnClickListener(v -> {
                result.confirm();
                dialog.dismiss();
            });

            dialog.show();

            if (dialog.getWindow() != null) {
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.dimAmount = 0.08f;
                dialog.getWindow().setAttributes(params);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
                lp.width = screenWidth - (int) (100 * density);
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
            }
        });
    }

    public static void showNativeDownloadDialog(Activity activity, String suggestedFileName, String url, String mimetype, boolean isBlob, DownloadCallback callback) {
        activity.runOnUiThread(() -> {
            float density = activity.getResources().getDisplayMetrics().density;

            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);

            int padHorizontal = (int) (14 * density);
            int padTop = (int) (14 * density);
            int padBottom = (int) (14 * density);

            layout.setPadding(padHorizontal, padTop, padHorizontal, padBottom);

            GradientDrawable backgroundDrawable = new GradientDrawable();
            backgroundDrawable.setColor(Color.parseColor("#1A1A1C"));
            backgroundDrawable.setCornerRadius(10 * density);
            backgroundDrawable.setStroke((int) (0.9f * density), Color.parseColor("#7C874F"));
            layout.setBackground(backgroundDrawable);

            TextView titleView = new TextView(activity);
            titleView.setText("Save File");
            titleView.setTextColor(Color.parseColor("#CBD868"));
            titleView.setTextSize(16);
            titleView.setTypeface(null, Typeface.BOLD);
            titleView.setGravity(Gravity.CENTER);
            titleView.setPadding(0, 0, 0, (int) (16 * density));
            layout.addView(titleView);

            TextView labelView = new TextView(activity);
            labelView.setText("File Name:");
            labelView.setTextColor(Color.parseColor("#DADADA"));
            labelView.setTextSize(14);
            labelView.setPadding((int) (6 * density), (int) (10 * density), 0, (int) (1 * density));
            layout.addView(labelView);

            final EditText input = new EditText(activity);
            input.setText(suggestedFileName);
            input.setTextSize(14);
            input.setTextColor(Color.parseColor("#9E9DA4"));
            input.setSingleLine(true);
            input.setPadding((int) (8 * density), (int) (10 * density), (int) (8 * density), (int) (10 * density));
            
            GradientDrawable inputBg = new GradientDrawable();
            inputBg.setColor(Color.parseColor("#1A1A1A"));
            inputBg.setCornerRadius(10 * density);
            inputBg.setStroke((int) (0.8f * density), Color.parseColor("#606060"));
            input.setBackground(inputBg);

            LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            inputParams.setMargins(0, (int) (4 * density), 0, (int) (16 * density));
            layout.addView(input, inputParams);

            // Korišćenje oštrijeg, punog kontrasta za tekst na dugmiću
            TextView saveButton = createStyledButton(activity, "Save", "#222222", 14f, density);
            TextView closeButton = createStyledButton(activity, "Close", "#222222", 14f, density);

            LinearLayout buttonLayout = new LinearLayout(activity);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            buttonLayout.setPadding(0, 0, 0, 0);

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    (int) (38 * density)
            );
            btnParams.weight = 1;
            btnParams.setMargins(0, 0, (int) (8 * density), 0);

            saveButton.setLayoutParams(btnParams);
            closeButton.setLayoutParams(btnParams);

            buttonLayout.addView(saveButton);
            buttonLayout.addView(closeButton);
            layout.addView(buttonLayout);

            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setView(layout)
                    .setCancelable(false)
                    .create();

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            closeButton.setOnClickListener(v -> dialog.dismiss());

            saveButton.setOnClickListener(v -> {
                String finalName = input.getText().toString().trim();
                if (finalName.isEmpty()) {
                    finalName = suggestedFileName;
                }
                callback.onSave(finalName);
                dialog.dismiss();
            });

            dialog.show();

            if (dialog.getWindow() != null) {
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.dimAmount = 0.08f;
                dialog.getWindow().setAttributes(params);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
                lp.width = screenWidth - (int) (80 * density);
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
            }
        });
    }

    private static TextView createStyledButton(Activity activity, String text, String textColorHex, float textSizeSp, float density) {
        TextView button = new TextView(activity);
        button.setText(text);
        button.setTextColor(Color.parseColor(textColorHex));
        button.setTextSize(textSizeSp);
        // Uklonjen veštački BOLD koji je razlivao i bledo prikazivao slova na Androidu
        button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        button.setGravity(Gravity.CENTER);
        button.setIncludeFontPadding(false);
        button.getPaint().setSubpixelText(true);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.parseColor("#CBD868"));
        bg.setCornerRadius(10 * density);
        button.setBackground(bg);

        // Već potvrđeni tačni brightness efekat pri dodiru
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.getBackground().setColorFilter(new LightingColorFilter(Color.WHITE, Color.argb(255, 40, 40, 20)));
                    v.invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.getBackground().clearColorFilter();
                    v.invalidate();
                    break;
            }
            return false;
        });

        return button;
    }

    public interface DownloadCallback {
        void onSave(String fileName);
    }
}
