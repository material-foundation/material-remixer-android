# 1.0

- First supported release, yay!
- FirebaseRemoteControllerSync is now completely functional and documented
  - Fixed an issue with FirebaseRemoteControllerSync not persisting settings across restarts.
- Added a sharing options drawer to the Remixer UI where the user can choose to share the Remote controller link or disable sharing altogether
- Styling is done!
- Added a more real-looking demo.
- Added the ability to attach the Remixer Fragment to a FAB

# 0.6.6

- Fix Proguarding issue with bad instructions for onboarding.

# 0.6.5

- Test improvements for Robolectric, styles are now supported
- First attempt at styling (for tests, not final version).
- Paddings are standardized now.
- Close button added.
- Sync to remote controller implemented. Still not fully ready or supported. Documentation incoming.



# 0.6.0

## Breaking changes

- Numbers are now backed by floats. So if you're using `RangeVariableMethod` or `IntegerListVariableMethod`, the callbacks will take `Float` instead of `Integer`
  - Furthermore, `IntegerListVariableMethod` has been split into `NumberListVariableMethod` and `ColorListVariableMethod`.
	- Colors are still backed by Integers
	- No more base type changes are expected, but sorry about this one, this had to be done and it had been a while under work.
	- Triggers are gone.
