package com.webhtml.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
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

            int padHorizontal = (int) (24 * density);
            int padTop = (int) (24 * density);
            int padBottom = (int) (10 * density);

            layout.setPadding(padHorizontal, padTop, padHorizontal, padBottom);

            GradientDrawable backgroundDrawable = new GradientDrawable();
            backgroundDrawable.setColor(Color.parseColor("#1C1C1E"));
            backgroundDrawable.setCornerRadius(24 * density);
            backgroundDrawable.setStroke((int) (1.5f * density), Color.parseColor("#373737"));
            layout.setBackground(backgroundDrawable);

            TextView messageView = new TextView(activity);
            messageView.setText(message);
            messageView.setTextColor(Color.parseColor("#C5C5C5"));
            messageView.setTextSize(16);
            messageView.setGravity(Gravity.START);
            messageView.setPadding(0, 0, 0, (int) (10 * density));
            layout.addView(messageView);

            TextView okButton = new TextView(activity);
            okButton.setText("OK");
            okButton.setTextColor(Color.parseColor("#8FA8FF"));
            okButton.setTextSize(14);
            okButton.setGravity(Gravity.CENTER);
            okButton.setTypeface(null, android.graphics.Typeface.NORMAL);
            okButton.setPadding((int) (16 * density), (int) (10 * density), (int) (16 * density), (int) (10 * density));

            android.util.TypedValue okRipple = new android.util.TypedValue();
            activity.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, okRipple, true);
            okButton.setBackgroundResource(okRipple.resourceId);

            LinearLayout buttonLayout = new LinearLayout(activity);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            buttonLayout.setPadding(0, (int) (40 * density), 0, (int) (4 * density));
            buttonLayout.addView(okButton);
            layout.addView(buttonLayout);

            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setView(layout)
                    .setCancelable(false)
                    .create();

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            okButton.setOnClickListener(v -> {
                result.confirm();
                dialog.dismiss();
            });

            dialog.show();

            if (dialog.getWindow() != null) {
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.dimAmount = 0.08f;
                dialog.getWindow().setAttributes(params);
            }

            if (dialog.getWindow() != null) {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
                lp.width = screenWidth - (int) (64 * density);
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

            int padHorizontal = (int) (24 * density);
            int padTop = (int) (24 * density);
            int padBottom = (int) (10 * density);

            layout.setPadding(padHorizontal, padTop, padHorizontal, padBottom);

            GradientDrawable backgroundDrawable = new GradientDrawable();
            backgroundDrawable.setColor(Color.parseColor("#1C1C1E"));
            backgroundDrawable.setCornerRadius(24 * density);
            backgroundDrawable.setStroke((int) (1.5f * density), Color.parseColor("#373737"));
            layout.setBackground(backgroundDrawable);

            TextView titleView = new TextView(activity);
            titleView.setText("Save File");
            titleView.setTextColor(Color.parseColor("#C5C5C5"));
            titleView.setTextSize(19);
            titleView.setTypeface(null, android.graphics.Typeface.BOLD);
            titleView.setGravity(Gravity.START);
            titleView.setPadding(0, 0, 0, (int) (18 * density));
            layout.addView(titleView);

            final EditText input = new EditText(activity);
            input.setText(suggestedFileName);
            input.setTextSize(16);
            input.setTextColor(Color.parseColor("#D9D9DD"));
            input.setSingleLine(true);
            input.setBackgroundColor(Color.TRANSPARENT);
            input.setPadding(0, 0, 0, (int) (10 * density));
            input.setSelectAllOnFocus(false);

            LinearLayout inputContainer = new LinearLayout(activity);
            inputContainer.setOrientation(LinearLayout.VERTICAL);
            inputContainer.addView(input, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            View bottomLine = new View(activity);
            bottomLine.setBackgroundColor(Color.parseColor("#37373A"));
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (int) (1 * density)
            );
            inputContainer.addView(bottomLine, lineParams);
            layout.addView(inputContainer);

            LinearLayout buttonLayout = new LinearLayout(activity);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
            buttonLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            buttonLayout.setPadding(0, (int) (40 * density), 0, (int) (4 * density));

            TextView closeButton = new TextView(activity);
            closeButton.setText("Close");
            closeButton.setTextColor(Color.parseColor("#8FA8FF"));
            closeButton.setTextSize(14);
            closeButton.setGravity(Gravity.CENTER);
            closeButton.setTypeface(null, android.graphics.Typeface.NORMAL);
            closeButton.setPadding((int) (14 * density), (int) (10 * density), (int) (14 * density), (int) (10 * density));

            android.util.TypedValue closeRipple = new android.util.TypedValue();
            activity.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, closeRipple, true);
            closeButton.setBackgroundResource(closeRipple.resourceId);

            TextView saveButton = new TextView(activity);
            saveButton.setText("Save");
            saveButton.setTextColor(Color.parseColor("#8FA8FF"));
            saveButton.setTextSize(14);
            saveButton.setGravity(Gravity.CENTER);
            saveButton.setTypeface(null, android.graphics.Typeface.NORMAL);
            saveButton.setPadding((int) (16 * density), (int) (10 * density), (int) (16 * density), (int) (10 * density));

            android.util.TypedValue saveRipple = new android.util.TypedValue();
            activity.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, saveRipple, true);
            saveButton.setBackgroundResource(saveRipple.resourceId);

            LinearLayout.LayoutParams closeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            closeParams.setMargins(0, 0, (int) (4 * density), 0);
            closeButton.setLayoutParams(closeParams);

            buttonLayout.addView(closeButton);
            buttonLayout.addView(saveButton);
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
            }

            if (dialog.getWindow() != null) {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
                lp.width = screenWidth - (int) (64 * density);
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
            }
        });
    }

    public interface DownloadCallback {
        void onSave(String fileName);
    }
}
