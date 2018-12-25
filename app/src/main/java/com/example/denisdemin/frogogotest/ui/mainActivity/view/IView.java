package com.example.denisdemin.frogogotest.ui.mainActivity.view;

import android.support.annotation.Nullable;

import com.example.denisdemin.frogogotest.data.Api.model.User;

import java.util.List;

import io.reactivex.Observable;

public interface IView {
    void setListData(List<User> userList);
    Observable<User> itemClicks();
    void showDialog(@Nullable User user,boolean createNew);
    void showProgressBar();
    void hideProgressBar();
    void showError();
    void hideError();
    void showFab();
    void hideFab();
    void showSnackBar(String message);
}
