package com.xscroll;

/** Turns X scroll events into distance, posts/reels passed and scroll time. Pure logic so it is unit-testable. */
final class Counter {
    static final long GAP_MS = 30_000; // a pause longer than this is not counted as scroll time
    static final int MAX_JUMP = 10;    // bigger index jumps in one event = switched lists (feed <-> reels), not scrolling

    // Totals since the last flush to storage.
    long px, posts, reels, ms;

    private int lastIndex = -1, maxIndex = -1; // maxIndex: furthest item reached, so re-scrolled items aren't recounted
    private long pxSinceItem, lastTime;

    /** fromIndex = first visible list item, dy = vertical pixels moved, screenH = screen height px, now = monotonic ms. */
    void onScroll(int fromIndex, int dy, int screenH, long now) {
        int d = Math.abs(dy);
        px += d;
        pxSinceItem += d;
        if (lastTime > 0 && now - lastTime < GAP_MS) ms += now - lastTime;
        lastTime = now;

        if (fromIndex < 0) return;
        if (lastIndex < 0 || Math.abs(fromIndex - lastIndex) > MAX_JUMP) {
            maxIndex = fromIndex; // first event, or switched lists
        } else if (fromIndex > maxIndex) {
            int n = fromIndex - maxIndex;
            // Reels pager moves about one full screen per item; feed posts are much shorter.
            if (pxSinceItem / n > screenH * 0.8) reels += n; else posts += n;
            maxIndex = fromIndex;
        }
        if (fromIndex != lastIndex) pxSinceItem = 0;
        lastIndex = fromIndex;
    }
}
