package com.example.denisdemin.frogogotest.ui.mainActivity.presenter;

import android.content.res.Resources;
import android.util.Log;

import com.example.denisdemin.frogogotest.R;
import com.example.denisdemin.frogogotest.data.Api.ApiService;
import com.example.denisdemin.frogogotest.data.Api.RetrofitInit;
import com.example.denisdemin.frogogotest.data.Api.model.User;
import com.example.denisdemin.frogogotest.ui.mainActivity.view.IView;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Presenter implements IPresenter {

    private IView mView;

    private Resources resources;

    private Retrofit retrofit;

    private ApiService apiService;

    private CompositeDisposable compositeDisposable;

    public Presenter(IView mView, Resources resources) {
        this.mView = mView;
        this.resources = resources;
        retrofit = RetrofitInit.getRetroObject();
        apiService = retrofit.create(ApiService.class);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getUserList(boolean isSwipeRefresh) {
        compositeDisposable.add(apiService.getUserList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (!isSwipeRefresh) {
                        mView.showProgressBar();
                    }
                    mView.hideErrorButton();
                    mView.hideFab();
                }).subscribe(listResponse -> {
                    mView.setListData(listResponse.body());
                    mView.showFab();
                    respondToItemClick();
                }, throwable -> {
                    mView.showSnackBar(resources.getString(R.string.load_error));
                    mView.hideProgressBar();
                    mView.showErrorButton();
                }, () -> mView.hideProgressBar()));
    }

    @Override
    public void dispatchUser(User user) {
        if (user.getId() != null) {
            updateUser(user);
        } else {
            createUser(user);
        }
    }

    @Override
    public void createUser(User user) {
        compositeDisposable.add(apiService.createNewUser(user)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mView.showProgressBar();
                    mView.hideErrorButton();
                    mView.hideFab();
                })
                .flatMap((Function<Response<User>, ObservableSource<Response<List<User>>>>) userResponse -> apiService.getUserList())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listResponse -> {
                    mView.showSnackBar(resources.getString(R.string.create_success));
                    mView.setListData(listResponse.body());
                    mView.showFab();
                    respondToItemClick();
                }, throwable -> mView.showSnackBar(resources.getString(R.string.create_failed)), () -> {
                    mView.hideProgressBar();
                    mView.showFab();
                }));
    }

    @Override
    public void updateUser(User user) {
        compositeDisposable.add(apiService.editUser(user.getId(),user)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mView.showProgressBar();
                    mView.hideErrorButton();
                    mView.hideFab();
                })
                .flatMap((Function<Response<User>, ObservableSource<Response<List<User>>>>) userResponse -> apiService.getUserList())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listResponse -> {
                    mView.showSnackBar(resources.getString(R.string.update_success));
                    mView.setListData(listResponse.body());
                    mView.showFab();
                    respondToItemClick();
                }, throwable -> mView.showSnackBar(resources.getString(R.string.update_failed)), () -> {
                    mView.hideProgressBar();
                    mView.showFab();
                }));
    }

    @Override
    public void onFabClicked() {
        mView.showDialog(null, true);
    }

    @Override
    public void respondToItemClick() {
        compositeDisposable.add(mView.itemClicks().subscribe(user -> mView.showDialog(user, false)));
    }

    @Override
    public void respondToDialogSave() {
        compositeDisposable.add(mView.dialogSave().subscribe(this::dispatchUser));
    }

    @Override
    public void cancelNetworkCall() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
