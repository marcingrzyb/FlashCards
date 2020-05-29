package pl.edu.agh.kis.flashcards.module.playmode.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.Objects;

public class ViewPagerDisabled extends ViewPager {

    private boolean lockPage;

    public ViewPagerDisabled(@NonNull Context context) {
        super(context);
        lockPage = false;
    }

    public ViewPagerDisabled(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        lockPage = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!lockPage()) {
            return super.onInterceptTouchEvent(ev);
        } else {
            if (MotionEventCompat.getActionMasked(ev) == MotionEvent.ACTION_MOVE) {
                //Do nothing
            } else {
                if (super.onInterceptTouchEvent(ev)) {
                    super.onTouchEvent(ev);
                }
            }
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!lockPage()) {
            return super.onTouchEvent(ev);
        } else {
            return MotionEventCompat.getActionMasked(ev) != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev);
        }
    }

    private boolean lockPage() {
        FlashCardCollectionAdapter adapter = (FlashCardCollectionAdapter) this.getAdapter();
        if (!lockPage) {
            lockPage = this.getCurrentItem() == this.getAdapter().getCount() - 1;
        }
        if (isaBoolean(adapter)) {
            adapter.getSessionSummary().processData();
        }
        return lockPage;
    }

    private boolean isaBoolean(FlashCardCollectionAdapter adapter) {
        return this.getCurrentItem() == this.getAdapter().getCount() - 2
                && Objects.nonNull(adapter.getSessionSummary());
    }

}
