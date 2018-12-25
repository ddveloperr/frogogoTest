package com.example.denisdemin.frogogotest.ui.mainActivity.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.denisdemin.frogogotest.R;
import com.example.denisdemin.frogogotest.data.Api.model.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private List<User> userList = new ArrayList<>();

    private PublishSubject<User> itemClicks = PublishSubject.create();

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user,viewGroup,false),itemClicks);
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

    public Observable<User> observeClicks(){return itemClicks;}
}
