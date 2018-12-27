package com.example.denisdemin.frogogotest.ui.mainActivity.presenter;

import com.example.denisdemin.frogogotest.data.Api.model.User;

public interface IPresenter {
    void getUserList(boolean isSwipeRefresh);
    void dispatchUser(User user);
    void createUser(User user);
    void updateUser(User user);
    void onFabClicked();
    void respondToItemClick();
    void respondToDialogSave();
    void cancelNetworkCall();
}
