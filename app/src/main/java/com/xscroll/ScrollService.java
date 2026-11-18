package com.xscroll;

import android.accessibilityservice.AccessibilityService;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;

/** Receives scroll events from the X app only (see res/xml/scroll_service.xml). */
public class ScrollService extends AccessibilityService {
    private final Counter counter = new Counter();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent e) {
        // dy == 0 drops horizontal scrolls (tab pager, image carousels) that would confuse the item index.
        if (e.getEventType() != AccessibilityEvent.TYPE_VIEW_SCROLLED || e.getScrollDeltaY() == 0) return;
        counter.onScroll(e.getFromIndex(), e.getScrollDeltaY(),
                getResources().getDisplayMetrics().heightPixels, SystemClock.elapsedRealtime());
        Stats.add(this, counter);
    }

    @Override
    public void onInterrupt() {}
}
