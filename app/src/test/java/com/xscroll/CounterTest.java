package com.xscroll;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CounterTest {
    private static final int H = 2400;

    @Test
    public void shortItemsArePosts() {
        Counter c = new Counter();
        c.onScroll(0, 250, H, 1000);
        c.onScroll(1, 250, H, 1100);
        c.onScroll(3, 1000, H, 1200); // fling past 2 posts in one event
        assertEquals(3, c.posts);
        assertEquals(0, c.reels);
        assertEquals(1500, c.px);
        assertEquals(200, c.ms);
    }

    @Test
    public void screenTallItemsAreReels() {
        Counter c = new Counter();
        c.onScroll(0, 100, H, 900);
        c.onScroll(0, 1200, H, 1000);
        c.onScroll(1, 1100, H, 1100);
        assertEquals(1, c.reels);
        assertEquals(0, c.posts);
    }

    @Test
    public void listSwitchAndBackScrollAndPausesAreNotCounted() {
        Counter c = new Counter();
        c.onScroll(2, 100, H, 1000);
        c.onScroll(40, 100, H, 1100);   // switched to another list
        c.onScroll(39, -300, H, 1200);  // scrolled back up
        c.onScroll(40, 300, H, 100_000); // back down to an already-seen item, after a long pause
        assertEquals(0, c.posts + c.reels);
        assertEquals(200, c.ms);
        c.onScroll(41, 300, H, 100_100); // new item
        assertEquals(1, c.posts);
    }
}
