package com.example.denisdemin.frogogotest.ui.mainActivity.view;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.denisdemin.frogogotest.R;
import com.example.denisdemin.frogogotest.common.UserDialog;
import com.example.denisdemin.frogogotest.data.Api.model.User;
import com.example.denisdemin.frogogotest.ui.mainActivity.adapters.UsersAdapter;
import com.example.denisdemin.frogogotest.ui.mainActivity.presenter.Presenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.internal.queue.MpscLinkedQueue;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity implements IView, SwipeRefreshLayout.OnRefreshListener {

    private Presenter mPresenter;

    private UsersAdapter usersAdapter = new UsersAdapter();

    private UserDialog dialog;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recycler_view_main)
    RecyclerView recyclerView;

    @BindView(R.id.button_reload)
    Button buttonReload;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @OnClick(R.id.button_reload)
    void onReloadClicked(){
        mPresenter.getUserList(false);
    }

    @OnClick(R.id.fab)
    void onFabClicked(){
        mPresenter.onFabClicked();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter = new Presenter(this,getResources());

        recyclerView.setAdapter(usersAdapter);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);

        mPresenter.getUserList(false);
    }

    @Override
    public void setListData(List<User> userList) {
        usersAdapter.swapAdapter(userList);
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public Observable<User> itemClicks() {
        return usersAdapter.observeClicks();
    }

    @Override
    public Observable<User> dialogSave() {
        return dialog.respondToClick();
    }

    @Override
    public void showDialog(@Nullable User user, boolean createNew) {
        dialog = new UserDialog(getLayoutInflater(),this);

        dialog.getDialog(user, createNew).show();
        mPresenter.respondToDialogSave();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void showFab() {
        floatingActionButton.show();
    }

    @Override
    public void hideFab() {
        floatingActionButton.hide();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(recyclerView,message,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorButton() {
        buttonReload.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorButton() {
        buttonReload.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRefresh() {
        mPresenter.getUserList(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.cancelNetworkCall();
    }
}
