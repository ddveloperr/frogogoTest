package com.example.denisdemin.frogogotest.ui.mainActivity.adapters;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
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

    @BindView(R.id.view_full_divider)
    View fullDivider;

    private PublishSubject<User> clickSubject;

    private PublishSubject<Integer> longClickSubject;

    private View rootView;

    private Resources resources;

    public UserViewHolder(@NonNull View itemView, PublishSubject<User> clickSubject,PublishSubject<Integer> longClickSubject) {
        super(itemView);
        this.clickSubject = clickSubject;
        this.rootView = itemView;
        this.longClickSubject = longClickSubject;
        resources = rootView.getResources();
        ButterKnife.bind(this, itemView);
    }

    void bind(User user) {
        textViewUserName.setText(resources.getString(R.string.item_fullName,user.getLastName(),user.getFirstName()));
        textViewEmail.setText(user.getEmail());
        textViewCreated.setText(resources.getString(R.string.item_created,user.getCreatedAt()));
        textViewUpdated.setText(resources.getString(R.string.item_updated,user.getUpdatedAt()));
        relativeLayoutExtra.setVisibility(user.extraViewVisibility());
        fullDivider.setVisibility(user.extraViewVisibility());
        if (user.getAvatarUrl()==null || user.getAvatarUrl().equals("")) {
            imageViewProfilePic.setImageResource(R.drawable.ic_default_profile_pic);
        }else{
            Picasso.with(rootView.getContext())
                    .load(user.getAvatarUrl())
                    .error(R.drawable.ic_default_profile_pic)
                    .transform(new CropCircleTransformation())
                    .into(imageViewProfilePic);
        }

        rootView.setOnClickListener(view -> clickSubject.onNext(user));
        rootView.setOnLongClickListener(view -> {
            longClickSubject.onNext(getAdapterPosition());
            return false;
        });
    }
}