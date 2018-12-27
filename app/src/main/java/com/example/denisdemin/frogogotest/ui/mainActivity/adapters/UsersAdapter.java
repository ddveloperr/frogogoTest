package com.example.denisdemin.frogogotest.ui.mainActivity.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.denisdemin.frogogotest.R;
import com.example.denisdemin.frogogotest.data.Api.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private List<User> userList = new ArrayList<>();

    private PublishSubject<User> itemClicks = PublishSubject.create();

    private PublishSubject<Integer> longItemClicks = PublishSubject.create();

    public UsersAdapter() {
        observeLongClicks();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user,viewGroup,false),itemClicks,longItemClicks);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder viewHolder, int position) {
        viewHolder.bind(userList.get(position));
    }

    @Override
    public int getItemCount() {
        if (userList != null && userList.size() > 0) {
            return userList.size();
        } else {
            return 0;
        }
    }

    public void swapAdapter(List<User> userList){
        this.userList.clear();
        this.userList.addAll(userList);
        notifyDataSetChanged();
    }

    @SuppressLint("CheckResult")
    private void observeLongClicks(){
        longItemClicks.subscribe(position -> {
            userList.get(position).setExtraVisible(!userList.get(position).isExtraVisible());
            notifyItemChanged(position);
        });
    }

    public Observable<User> observeClicks(){return itemClicks;}
}
