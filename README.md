# X Scroll Meter

Android app that measures how much you scroll in the X app: distance in meters, posts and reels passed, and active scroll minutes, with a 7-day history.

## How it works

An Accessibility Service subscribed only to scroll events from `com.twitter.android`. It cannot read window content, so no post text ever reaches the app. Each event gives the pixels moved and the first visible list item:

- **Distance**: pixels converted to meters with the screen's density.
- **Posts vs reels**: an item that took about a full screen of scrolling to pass is a reel; shorter items are posts. Items you scroll back over are not recounted.
- **Scroll time**: time between scroll events; pauses over 30 s are not counted.

Data stays on the phone (SharedPreferences). No network permission.

## Install

1. Download the APK from [Releases](../../releases) and install it.
2. Open the app, tap **Tracking OFF · tap to enable**, and turn on X Scroll Meter.
3. On Android 13+, if the switch is greyed out: App info → ⋮ → **Allow restricted settings**, then retry.

Requires Android 9 (API 28) or newer.

## Build

Needs JDK 21 and the Android SDK (platform 36).

```
./gradlew test assembleDebug
```

## Limits

- Posts vs reels is a heuristic; a post with a very tall image may count as a reel.
- If X stops reporting scroll deltas to accessibility services, counts stay at 0.
