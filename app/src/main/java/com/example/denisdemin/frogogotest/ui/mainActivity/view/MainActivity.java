package com.example.denisdemin.frogogotest.ui.mainActivity.view;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.denisdemin.frogogotest.R;
import com.example.denisdemin.frogogotest.data.Api.model.User;
import com.example.denisdemin.frogogotest.ui.mainActivity.adapters.UsersAdapter;
import com.example.denisdemin.frogogotest.ui.mainActivity.presenter.Presenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public class MainActivity extends AppCompatActivity implements IView{

    private Presenter mPresenter;

    private UsersAdapter usersAdapter;

    private AlertDialog.Builder dialogBuilder;

    private AlertDialog dialog;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recycler_view_main)
    RecyclerView recyclerView;

    @OnClick(R.id.fab)
    void onFabClicked(){

    }

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter = new Presenter(this);

        usersAdapter = new UsersAdapter();

        dialogBuilder = new AlertDialog.Builder(MainActivity.this);

        recyclerView.setAdapter(usersAdapter);

        mPresenter.getUserList();
    }

    @Override
    public void setListData(List<User> userList) {
        usersAdapter.swapAdapter(userList);
    }

    @Override
    public Observable<User> itemClicks() {
        return usersAdapter.observeClicks();
    }

    @Override
    public void showDialog(@Nullable User user, boolean createNew) {
        dialogBuilder.setView(R.layout.item_dialog);
        dialog = dialogBuilder.create();//todo editText, TextInputLayout, check that out
        View dialogView = getLayoutInflater().inflate(R.layout.item_dialog,null);
        dialog.show();
        if (createNew) {
            ((TextView)dialogView.findViewById(R.id.text_view_dialog_header)).setText(getResources().getString(R.string.dialog_create));
        }else{
            ((TextView)dialogView.findViewById(R.id.text_view_dialog_header)).setText(getResources().getString(R.string.dialog_edit));
            ((TextView)dialogView.findViewById(R.id.edit_text_surName)).setText(user.getLastName());
            ((TextView)dialogView.findViewById(R.id.edit_text_name)).setText(user.getFirstName());
            ((TextView)dialogView.findViewById(R.id.edit_text_email)).setText(user.getEmail());
        }
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
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
    public void showError() {

    }

    @Override
    public void hideError() {

    }
}
