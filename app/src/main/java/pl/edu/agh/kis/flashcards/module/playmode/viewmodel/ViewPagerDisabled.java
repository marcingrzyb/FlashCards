package pl.edu.agh.kis.flashcards.module.playmode.viewmodel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.viewpager.widget.ViewPager;

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
        if (!lockPage) {
            lockPage = this.getCurrentItem() == this.getAdapter().getCount() - 1;
        }
        if (lockPage) {
            FlashCardCollectionAdapter adapter = (FlashCardCollectionAdapter) this.getAdapter();
            adapter.getSessionSummary().processData();
        }
        return lockPage;
    }

}
