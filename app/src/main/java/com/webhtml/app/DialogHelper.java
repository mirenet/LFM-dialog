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

        int pad = (int) (12 * density);
        layout.setPadding(pad, pad, pad, pad);

        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(Color.parseColor("#1A1A1C"));
        backgroundDrawable.setCornerRadius(10 * density);
        backgroundDrawable.setStroke((int) (0.9f * density), Color.parseColor("#7C874F"));
        layout.setBackground(backgroundDrawable);

        TextView titleView = new TextView(activity);
        titleView.setText("Alert!");
        titleView.setTextColor(Color.parseColor("#CBD868"));
        titleView.setTextSize(16);
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        titleView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, (int) (3 * density), 0, (int) (16 * density));
        titleView.setLayoutParams(titleParams);
        layout.addView(titleView);

        TextView messageView = new TextView(activity);
        messageView.setText(message);
        messageView.setTextColor(Color.parseColor("#BAB9BF"));
        messageView.setTextSize(15);
        messageView.setGravity(Gravity.START);
        messageView.setLineSpacing(0, 1.4f);
        LinearLayout.LayoutParams msgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        msgParams.setMargins((int) (6 * density), (int) (20 * density), 0, (int) (32 * density));
        messageView.setLayoutParams(msgParams);
        layout.addView(messageView);

        TextView closeButton = new TextView(activity);
        closeButton.setText("Close");
        closeButton.setTextColor(Color.parseColor("#000000"));
        closeButton.setTextSize(13.5f);
        closeButton.setGravity(Gravity.CENTER);
        closeButton.setTypeface(null, android.graphics.Typeface.BOLD);

        GradientDrawable btnBg = new GradientDrawable();
        btnBg.setColor(Color.parseColor("#CBD868"));
        btnBg.setCornerRadius(10 * density);
        closeButton.setBackground(btnBg);
        closeButton.setPadding((int) (10 * density), (int) (10 * density), (int) (10 * density), (int) (10 * density));

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                (int) (90 * density),
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        closeButton.setLayoutParams(btnParams);

        LinearLayout buttonLayout = new LinearLayout(activity);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowParams.setMargins(0, (int) (12 * density), 0, 0);
        buttonLayout.setLayoutParams(rowParams);
        buttonLayout.addView(closeButton);
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
        }

        if (dialog.getWindow() != null) {
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

        int pad = (int) (14 * density);
        layout.setPadding(pad, pad, pad, pad);

        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(Color.parseColor("#1A1A1C"));
        backgroundDrawable.setCornerRadius(10 * density);
        backgroundDrawable.setStroke((int) (0.9f * density), Color.parseColor("#7C874F"));
        layout.setBackground(backgroundDrawable);

        TextView titleView = new TextView(activity);
        titleView.setText("Save File");
        titleView.setTextColor(Color.parseColor("#CBD868"));
        titleView.setTextSize(16);
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        titleView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, (int) (16 * density));
        titleView.setLayoutParams(titleParams);
        layout.addView(titleView);

        TextView labelView = new TextView(activity);
        labelView.setText("File Name:");
        labelView.setTextColor(Color.parseColor("#DADADA"));
        labelView.setTextSize(14);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        labelParams.setMargins((int) (6 * density), (int) (10 * density), 0, (int) (1 * density));
        labelView.setLayoutParams(labelParams);
        layout.addView(labelView);

        final EditText input = new EditText(activity);
        input.setText(suggestedFileName);
        input.setTextSize(14);
        input.setTextColor(Color.parseColor("#9E9DA4"));
        input.setSingleLine(true);
        input.setPadding((int) (8 * density), (int) (10 * density), (int) (8 * density), (int) (10 * density));
        input.setSelectAllOnFocus(false);

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

        LinearLayout buttonLayout = new LinearLayout(activity);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowParams.setMargins(0, 0, 0, 0);
        buttonLayout.setLayoutParams(rowParams);

        TextView saveButton = new TextView(activity);
        saveButton.setText("Save");
        saveButton.setTextColor(Color.parseColor("#555555"));
        saveButton.setTextSize(14);
        saveButton.setGravity(Gravity.CENTER);
        saveButton.setTypeface(null, android.graphics.Typeface.BOLD);

        GradientDrawable saveBg = new GradientDrawable();
        saveBg.setColor(Color.parseColor("#CBD868"));
        saveBg.setCornerRadius(10 * density);
        saveButton.setBackground(saveBg);
        saveButton.setPadding((int) (10 * density), (int) (10 * density), (int) (10 * density), (int) (10 * density));

        TextView closeButton = new TextView(activity);
        closeButton.setText("Close");
        closeButton.setTextColor(Color.parseColor("#555555"));
        closeButton.setTextSize(14);
        closeButton.setGravity(Gravity.CENTER);
        closeButton.setTypeface(null, android.graphics.Typeface.BOLD);

        GradientDrawable closeBg = new GradientDrawable();
        closeBg.setColor(Color.parseColor("#CBD868"));
        closeBg.setCornerRadius(10 * density);
        closeButton.setBackground(closeBg);
        closeButton.setPadding((int) (10 * density), (int) (10 * density), (int) (10 * density), (int) (10 * density));

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        btnParams.weight = 1;
        btnParams.setMargins(0, 0, (int) (10 * density), 0);
        saveButton.setLayoutParams(btnParams);

        LinearLayout.LayoutParams closeParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        closeParams.weight = 1;
        closeButton.setLayoutParams(closeParams);

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
        }

        if (dialog.getWindow() != null) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
            lp.width = screenWidth - (int) (80 * density);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
        }
    });
}

public interface DownloadCallback {
    void onSave(String fileName);
}


}
