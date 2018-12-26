package com.example.denisdemin.frogogotest.ui.mainActivity.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.denisdemin.frogogotest.R;
import com.example.denisdemin.frogogotest.data.Api.model.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.subjects.PublishSubject;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class UserViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view_user_name)
    TextView textViewUserName;

    @BindView(R.id.text_view_user_email)
    TextView textViewEmail;

    @BindView(R.id.image_view_profile_pic)
    AppCompatImageView imageViewProfilePic;

    @BindView(R.id.text_view_created)
    TextView textViewCreated;

    @BindView(R.id.text_view_updated)
    TextView textViewUpdated;

    @BindView(R.id.relative_layout_extra)
    RelativeLayout relativeLayoutExtra;

    private PublishSubject<User> clickSubject;

    private View rootView;

    public UserViewHolder(@NonNull View itemView, PublishSubject<User> clickSubject) {
        super(itemView);
        this.clickSubject = clickSubject;
        this.rootView = itemView;
        ButterKnife.bind(this, itemView);
    }

    void bind(final User user) {
        textViewUserName.setText(rootView.getResources().getString(R.string.item_fullName,user.getLastName(),user.getFirstName()));
        textViewEmail.setText(user.getEmail());
        textViewCreated.setText(rootView.getResources().getString(R.string.item_created,user.getCreatedAt()));
        textViewUpdated.setText(rootView.getResources().getString(R.string.item_updated,user.getUpdatedAt()));
        if (user.getAvatarUrl()==null || user.getAvatarUrl().equals("")) {
            imageViewProfilePic.setImageResource(R.drawable.ic_default_profile_pic);
        }else{
            Picasso.with(itemView.getContext())
                    .load(user.getAvatarUrl())
                    .error(R.drawable.ic_default_profile_pic)
                    .transform(new CropCircleTransformation())
                    .into(imageViewProfilePic);
        }
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSubject.onNext(user);
            }
        });
        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(relativeLayoutExtra.getVisibility()==View.VISIBLE){
                    relativeLayoutExtra.setVisibility(View.GONE);
                }else{
                    relativeLayoutExtra.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
    }
}