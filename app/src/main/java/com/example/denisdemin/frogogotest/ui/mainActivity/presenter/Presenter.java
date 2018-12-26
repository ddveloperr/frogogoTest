package com.example.denisdemin.frogogotest.ui.mainActivity.presenter;

import android.content.res.Resources;
import android.util.Log;

import com.example.denisdemin.frogogotest.R;
import com.example.denisdemin.frogogotest.data.Api.ApiService;
import com.example.denisdemin.frogogotest.data.Api.RetrofitInit;
import com.example.denisdemin.frogogotest.data.Api.model.User;
import com.example.denisdemin.frogogotest.ui.mainActivity.view.IView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Presenter implements IPresenter {

    private IView mView;

    private Retrofit retrofit;

    private ApiService apiService;

    private CompositeDisposable compositeDisposable;

    private Resources resources;

    public Presenter(IView mView, Resources resources) {
        this.mView = mView;
        this.resources = resources;
        retrofit = RetrofitInit.getRetroObject();
        apiService = retrofit.create(ApiService.class);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getUserList(final boolean isSwipeRefresh) {
        compositeDisposable.add(apiService.getUserList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (!isSwipeRefresh) {
                            mView.showProgressBar();
                        }
                        mView.hideErrorButton();
                        mView.hideFab();
                    }
                }).subscribe(new Consumer<Response<List<User>>>() {
                    @Override
                    public void accept(Response<List<User>> listResponse) throws Exception {
                        mView.setListData(listResponse.body());
                        mView.showFab();
                        respondToItemClick();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showSnackBar("Ошибка");
                        mView.hideProgressBar();
                        mView.showErrorButton();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideProgressBar();
                    }
                }));
    }

    @Override
    public void dispatchUser(User user) {
        if(user.getId()!=null){
            patchUser(user);
        }else{
            postUser(user);
        }
    }

    @Override
    public void postUser(User user) {
        compositeDisposable.add(apiService.createNewUser(user).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showProgressBar();
                        mView.hideErrorButton();
                        mView.hideFab();
                    }
                }).subscribe(new Consumer<Response<User>>() {
                    @Override
                    public void accept(Response<User> userResponse) throws Exception {
                        if(userResponse.code()==201){
                            mView.showSnackBar(resources.getString(R.string.create_success));
                            getUserList(false);
                        }else{
                            Log.d("ErrorDebig",userResponse.message());
                            mView.showSnackBar(resources.getString(R.string.create_failed));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showSnackBar(resources.getString(R.string.create_failed));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideProgressBar();
                        mView.showFab();
                    }
                }));
    }

    @Override
    public void patchUser(User user) {
        compositeDisposable.add(apiService.editUser(user.getId(),user).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showProgressBar();
                        mView.hideErrorButton();
                        mView.hideFab();
                    }
                }).subscribe(new Consumer<Response<User>>() {
                    @Override
                    public void accept(Response<User> userResponse) throws Exception {
                        if(userResponse.code()==200){
                            mView.showSnackBar(resources.getString(R.string.patch_success));
                            getUserList(false);
                        }else{
                            mView.showSnackBar(resources.getString(R.string.patch_failed));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showSnackBar(resources.getString(R.string.patch_failed));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideProgressBar();
                        mView.showFab();
                    }
                }));
    }

    @Override
    public void onFabClicked() {
        mView.showDialog(null,true);
    }

    @Override
    public void respondToItemClick() {
        compositeDisposable.add(mView.itemClicks().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        mView.showDialog(user,false);
                    }
                }));
    }

    @Override
    public void respondToDialogSave() {
        compositeDisposable.add(mView.dialogSave().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        dispatchUser(user);
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
