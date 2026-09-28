package com.webhtml.app;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

private static void applyButtonTouchEffect(View button) {
    button.setOnTouchListener((v, event) -> {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Simulacija CSS filter: brightness(1.25) / osvetljavanje dugmeta
                v.getBackground().setColorFilter(Color.argb(50, 255, 255, 255), PorterDuff.Mode.SRC_ATOP);
                v.invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                v.getBackground().clearColorFilter();
                v.invalidate();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                }
                break;
        }
        return true;
    });
}

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
        titleView.setPadding(0, (int) (3 * density), 0, (int) (16 * density));
        layout.addView(titleView);

        TextView messageView = new TextView(activity);
        messageView.setText(message);
        messageView.setTextColor(Color.parseColor("#BAB9BF"));
        messageView.setTextSize(15);
        messageView.setGravity(Gravity.START);
        messageView.setLineSpacing(0, 1.4f);
        messageView.setPadding((int) (6 * density), (int) (20 * density), 0, (int) (32 * density));
        layout.addView(messageView);

        LinearLayout buttonLayout = new LinearLayout(activity);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        buttonLayout.setPadding(0, (int) (12 * density), 0, 0);

        TextView closeButton = new TextView(activity);
        closeButton.setText("Close");
        closeButton.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000"))); // U alert primeru tekst je crn (#000)
        closeButton.setTextSize(13.5f);
        closeButton.setGravity(Gravity.CENTER);
        closeButton.setTypeface(null, android.graphics.Typeface.NORMAL);
        closeButton.getPaint().setFakeBoldText(true); // font-weight: 500

        GradientDrawable btnBg = new GradientDrawable();
        btnBg.setColor(Color.parseColor("#CBD868"));
        btnBg.setCornerRadius(10 * density);
        closeButton.setBackground(btnBg);

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                (int) (90 * density),
                (int) (38 * density)
        );
        closeButton.setLayoutParams(btnParams);
        closeButton.setPadding(0, 0, 0, 0);
        
        applyButtonTouchEffect(closeButton);

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
        titleView.setPadding(0, 0, 0, (int) (16 * density));
        layout.addView(titleView);

        TextView labelView = new TextView(activity);
        labelView.setText("File Name:");
        labelView.setTextColor(Color.parseColor("#DADADA"));
        labelView.setTextSize(14);
        labelView.setPadding((int) (6 * density), 0, 0, (int) (1 * density));
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
        
        LinearLayout inputContainer = new LinearLayout(activity);
        inputContainer.setOrientation(LinearLayout.VERTICAL);
        inputContainer.addView(input, inputParams);
        layout.addView(inputContainer);

        LinearLayout buttonLayout = new LinearLayout(activity);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);

        TextView saveButton = new TextView(activity);
        saveButton.setText("Save");
        saveButton.setTextColor(ColorStateList.valueOf(Color.parseColor("#555555"))); // Eksplicitna boja #555
        saveButton.setTextSize(14);
        saveButton.setGravity(Gravity.CENTER);
        saveButton.setTypeface(null, android.graphics.Typeface.NORMAL);
        saveButton.getPaint().setFakeBoldText(true); // Simulacija font-weight: 600

        GradientDrawable saveBg = new GradientDrawable();
        saveBg.setColor(Color.parseColor("#CBD868"));
        saveBg.setCornerRadius(10 * density);
        saveButton.setBackground(saveBg);

        LinearLayout.LayoutParams saveParams = new LinearLayout.LayoutParams(
                0,
                (int) (38 * density),
                1f
        );
        saveParams.setMargins(0, 0, (int) (10 * density), 0);
        saveButton.setLayoutParams(saveParams);
        saveButton.setPadding(0, 0, 0, 0);
        applyButtonTouchEffect(saveButton);

        TextView closeButton = new TextView(activity);
        closeButton.setText("Close");
        closeButton.setTextColor(ColorStateList.valueOf(Color.parseColor("#555555"))); // Eksplicitna boja #555
        closeButton.setTextSize(14);
        closeButton.setGravity(Gravity.CENTER);
        closeButton.setTypeface(null, android.graphics.Typeface.NORMAL);
        closeButton.getPaint().setFakeBoldText(true); // Simulacija font-weight: 600

        GradientDrawable closeBg = new GradientDrawable();
        closeBg.setColor(Color.parseColor("#CBD868"));
        closeBg.setCornerRadius(10 * density);
        closeButton.setBackground(closeBg);

        LinearLayout.LayoutParams closeParams = new LinearLayout.LayoutParams(
                0,
                (int) (38 * density),
                1f
        );
        closeButton.setLayoutParams(closeParams);
        closeButton.setPadding(0, 0, 0, 0);
        applyButtonTouchEffect(closeButton);

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
