package com.example.denisdemin.frogogotest.ui.mainActivity.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.denisdemin.frogogotest.R;
import com.example.denisdemin.frogogotest.data.Api.model.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.subjects.PublishSubject;

public class UserViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view_user_name)
    TextView textViewUserName;

    @BindView(R.id.text_view_user_email)
    TextView textViewEmail;

    @BindView(R.id.image_view_profile_pic)
    AppCompatImageView imageViewProfilePic;

    private PublishSubject<User> clickSubject;

    private View rootView;

    public UserViewHolder(@NonNull View itemView, PublishSubject<User> clickSubject) {
        super(itemView);
        this.clickSubject = clickSubject;
        this.rootView = itemView;
        ButterKnife.bind(this, itemView);
    }

    void bind(final User user) {
        textViewUserName.setText(user.getFirstName());
        textViewEmail.setText(user.getEmail());
        if (user.getAvatarUrl()==null || user.getAvatarUrl().equals("")) {
            Picasso.with(itemView.getContext())
                    .load(R.drawable.ic_default_profile_pic)
                    .into(imageViewProfilePic);
        }else{
            Picasso.with(itemView.getContext())
                    .load(user.getAvatarUrl())
                    .error(R.drawable.ic_default_profile_pic)
                    .into(imageViewProfilePic);
        }
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSubject.onNext(user);
            }
        });
    }
}