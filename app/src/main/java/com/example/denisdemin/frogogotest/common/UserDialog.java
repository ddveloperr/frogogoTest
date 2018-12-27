package com.example.denisdemin.frogogotest.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.denisdemin.frogogotest.R;
import com.example.denisdemin.frogogotest.data.Api.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class UserDialog {

    private EditText editTextSurName;

    private EditText editTextName;

    private EditText editTextEmail;

    private Animation shakeAnimation;

    private LayoutInflater layoutInflater;

    private Context context;

    private AlertDialog.Builder alertDialogBuilder;

    private View dialogView;

    private String dialogHeader;

    private PublishSubject<User> publishSubject = PublishSubject.create();

    private User user;

    private AlertDialog alertDialog;

    private Resources resources;

    public UserDialog(LayoutInflater layoutInflater, Context context) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        resources = context.getResources();
        alertDialogBuilder = new AlertDialog.Builder(context);
        dialogView = layoutInflater.inflate(R.layout.item_dialog, null);
        editTextSurName = dialogView.findViewById(R.id.edit_text_surName);
        editTextName = dialogView.findViewById(R.id.edit_text_name);
        editTextEmail = dialogView.findViewById(R.id.edit_text_email);
        shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake_anim);
    }

    public AlertDialog getDialog(@Nullable User user, boolean createNew) {
        ButterKnife.bind(dialogView);

        if (user != null) {
            this.user = user;
        } else {
            user = new User();
        }

        if (createNew) {
            dialogHeader = resources.getString(R.string.dialog_create);
        } else {
            dialogHeader = resources.getString(R.string.dialog_edit);
            editTextSurName.setText(user.getLastName());
            editTextName.setText(user.getFirstName());
            editTextEmail.setText(user.getEmail());
        }

        alertDialog = alertDialogBuilder
                .setTitle(dialogHeader)
                .setPositiveButton(resources.getString(R.string.save), null)
                .setView(dialogView)
                .create();

        final User finalUser = user;
        alertDialog.setOnShowListener(dialogInterface -> alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(view -> {
                    finalUser.setLastName(editTextSurName.getText().toString());
                    finalUser.setFirstName(editTextName.getText().toString());
                    finalUser.setEmail(editTextEmail.getText().toString());
                    int formValid = validateForm(finalUser);
                    if (formValid < 0) {
                        publishSubject.onNext(finalUser);
                        alertDialog.dismiss();
                    } else {
                        showError(formValid);
                    }
                }));

        return alertDialog;
    }

    private int validateForm(User user) {
        if (!FieldValidators.isFieldValid(user.getLastName())) {
            return R.id.text_input_layout_surName;
        } else if (!FieldValidators.isFieldValid(user.getFirstName())) {
            return R.id.text_input_layout_name;
        } else if (!FieldValidators.isEmailFieldValid(user.getEmail())) {
            return R.id.text_input_layout_email;
        } else {
            return -1;
        }
    }

    private void showError(int viewId) {
        dialogView.findViewById(viewId).startAnimation(shakeAnimation);
    }

    public Observable<User> respondToClick() {
        return publishSubject;
    }
}
