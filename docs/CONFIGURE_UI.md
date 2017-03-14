# Display the Remixer Fragment

You can configure the `RemixerFragment` in the `Activity`'s `onCreate(Bundle)` method, after the call to `RemixerBinder.bind(this)`. You have 3 (not mutually-exclusive) options. You can see examples of how to do it below.

*Note, however, that configuring the UI is optional if you want to exclusively use the Firebase Remote Controller functionality.*

## Attach the Remixer Fragment to a Button
You need to call `RemixerFragment#attachToButton(FragmentActivity, Button)`

Your `Activity.onCreate` may look like this:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  //...
  remixerButton = (Button) findViewById(R.id.button);
  RemixerBinder.bind(this);
  RemixerFragment.newInstance().attachToButton(this, remixerButton);
}
```

## Attach the Remixer Fragment to a FAB
You need to call `RemixerFragment#attachToFab(FragmentActivity, FloatingActionButton)`

Your `Activity.onCreate` may look like this:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  //...
  remixerFab = (FloatingActionButton) findViewById(R.id.fab);
  RemixerBinder.bind(this);
  RemixerFragment.newInstance().attachToFab(this, remixerFab);
}
```

## Attach the Remixer Fragment to a multi-touch gesture
You need to call `RemixerFragment#attachToGesture(FragmentActivity, Direction, int)`

Your `Activity.onCreate` may look like this:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  //...
  RemixerBinder.bind(this);
  RemixerFragment.newInstance().attachToGesture(
      this,
      Direction.UP,
      3 /* numberOfFingers */);
}
```

## Attach the Remixer Fragment to a shake
You need to call `RemixerFragment#attachToShake(FragmentActivity, double)` from `onResume()` and call `RemixerFragment#detachFromShake()` from `onPause()`

Your `Activity.onCreate` may look like this:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  //...
  RemixerBinder.bind(this);
  remixerFragment = RemixerFragment.newInstance();
}

@Override
protected void onResume() {
  super.onResume();
  remixerFragment.attachToShake(this, 20.0);
}

@Override
protected void onPause() {
  super.onPause();
  remixerFragment.detachFromShake();
}
```

## Or combine all of them

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  // ...
  remixerButton = (Button) findViewById(R.id.button);
  remixerFab = (FloatingActionButton) findViewById(R.id.fab);
  RemixerBinder.bind(this);

  remixerFragment = RemixerFragment.newInstance();
  remixerFragment.attachToGesture(this, Direction.UP, 3);
  remixerFragment.attachToButton(this, remixerButton);
  remixerFragment.attachToFab(this, remixerFab);
}

@Override
protected void onResume() {
  super.onResume();
  remixerFragment.attachToShake(this, 20.0);
}

@Override
protected void onPause() {
  super.onPause();
  remixerFragment.detachFromShake();
}
```
