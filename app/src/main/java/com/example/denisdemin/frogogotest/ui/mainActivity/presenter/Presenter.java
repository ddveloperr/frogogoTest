package com.example.denisdemin.frogogotest.ui.mainActivity.presenter;

import android.util.Log;

import com.example.denisdemin.frogogotest.data.Api.ApiService;
import com.example.denisdemin.frogogotest.data.Api.RetrofitInit;
import com.example.denisdemin.frogogotest.data.Api.model.User;
import com.example.denisdemin.frogogotest.ui.mainActivity.view.IView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Presenter implements IPresenter {

    private IView mView;

    private Retrofit retrofit;

    private ApiService apiService;

    private CompositeDisposable compositeDisposable;

    public Presenter(IView mView) {
        this.mView = mView;
        retrofit = RetrofitInit.getRetroObject();
        apiService = retrofit.create(ApiService.class);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getUserList() {
        compositeDisposable.add(apiService.getUserList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showProgressBar();
                    }
                }).subscribe(new Consumer<Response<List<User>>>() {
                    @Override
                    public void accept(Response<List<User>> listResponse) throws Exception {
                        mView.setListData(listResponse.body());
                        respondToClick();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showSnackBar("Ошибка");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideProgressBar();
                    }
                }));
    }

    @Override
    public void onFabClicked() {

    }

    @Override
    public void respondToClick() {
        compositeDisposable.add(mView.itemClicks().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        mView.showDialog(user,true);
                    }
                }));
    }

    @Override
    public void cancelNetworkCall() {
        if(!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }
}
