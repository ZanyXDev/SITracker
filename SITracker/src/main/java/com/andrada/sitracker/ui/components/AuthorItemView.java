/*
 * Copyright 2016 Gleb Godonoga.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andrada.sitracker.ui.components;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrada.sitracker.R;
import com.andrada.sitracker.bitmap.CheckableAvatarFlipDrawable;
import com.andrada.sitracker.contracts.IsNewItemTappedListener;
import com.andrada.sitracker.db.beans.Author;
import com.andrada.sitracker.events.AuthorCheckedEvent;
import com.andrada.sitracker.ui.widget.CheckedRelativeLayout;
import com.andrada.sitracker.util.DateFormatterUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.jetbrains.annotations.NotNull;

import de.greenrobot.event.EventBus;

@EViewGroup(R.layout.authors_list_item)
public class AuthorItemView extends CheckedRelativeLayout {

    @ViewById
    TextView author_title;
    @ViewById
    TextView author_update_date;
    @ViewById
    ImageButton author_updated;

    @ViewById
    ImageView author_image;
    private boolean mIsNew = false;
    private boolean mPreviousNewState = false;
    private IsNewItemTappedListener mListener;

    private CheckableAvatarFlipDrawable avatarFlipDrawable;

    private int avatarDimen;

    public AuthorItemView(@NotNull Context context) {
        super(context);
        this.setBackgroundResource(R.drawable.authors_list_item_selector_normal);
        avatarFlipDrawable = new CheckableAvatarFlipDrawable(context.getResources(), 250);
        avatarDimen = (int) getResources().getDimension(R.dimen.avatar_image_height);
    }

    @AfterViews
    void afterViews() {
        author_image.setImageDrawable(avatarFlipDrawable);

        avatarFlipDrawable.getAvatarDrawable().setDecodeDimensions(avatarDimen, avatarDimen);
        this.delegatedTouchViews.put(
                ViewConfig.wholeRight(),
                author_updated);

        this.delegatedTouchViews.put(
                ViewConfig.wholeLeft(),
                author_image);
    }

    public void setListener(IsNewItemTappedListener listener) {
        mListener = listener;
    }

    public void bind(@NotNull Author author) {

        if (getContext() != null) {
            avatarFlipDrawable.getAvatarDrawable().bind(getContext(), author.getName(), author.getAuthorImageUrl());
        }

        mIsNew = author.getNew();
        author_updated.setTag(author);
        author_title.setText(author.getName());
        author_update_date.setText(
                DateFormatterUtil.getFriendlyDateRelativeToToday(author.getUpdateDate(),
                        getResources().getConfiguration().locale));
        setOldNewStates();
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        avatarFlipDrawable.flipTo(!checked);
    }

    @Override
    protected void onDelegatedTouchViewClicked(@NotNull View view) {
        if (mListener != null && view.getId() == R.id.author_updated && mIsNew) {
            mIsNew = false;
            setOldNewStates();
            mListener.onIsNewItemTapped(view);
        } else if (view.getId() == R.id.author_image) {
            this.toggle();
            Author auth = (Author) author_updated.getTag();
            if (auth != null) {
                EventBus.getDefault().post(new AuthorCheckedEvent(auth.getId(), this));
            }
        }
    }

    @Override
    protected void onDelegatedTouchViewDown(View view) {
        if (mIsNew && view.getId() == R.id.author_updated) {
            author_updated.setImageResource(R.drawable.star_selected_focused);
        }
    }

    @Override
    protected void onDelegatedTouchViewCancel(View view) {
        //If we are not new, just ignore everything
        if (mIsNew && view.getId() == R.id.author_updated) {
            author_updated.setImageResource(R.drawable.star_selected);
        }
    }

    private void setOldNewStates() {
        //TODO if new - make author title bold and update date text color in theme primary
        if (mIsNew && !mPreviousNewState) {
            author_title.setTypeface(null, Typeface.BOLD);
            author_update_date.setTypeface(null, Typeface.BOLD);
            author_update_date.setTextColor(getResources().getColor(R.color.accent_blue));
            author_updated.setImageResource(R.drawable.star_selected);
            mPreviousNewState = true;
        } else if (!mIsNew && mPreviousNewState) {
            author_title.setTypeface(null, Typeface.NORMAL);
            author_update_date.setTypeface(null, Typeface.NORMAL);
            author_update_date.setTextColor(getResources().getColor(R.color.body_text_1));
            author_updated.setImageResource(R.drawable.star_unselected);
            mPreviousNewState = false;
        }
    }
}
