# #develop#

 TODO: Enumerate changes.


# 0.6.0

## Breaking changes

- Numbers are now backed by floats. So if you're using `RangeVariableMethod` or `IntegerListVariableMethod`, the callbacks will take `Float` instead of `Integer`
  - Furthermore, `IntegerListVariableMethod` has been split into `NumberListVariableMethod` and `ColorListVariableMethod`.
	- Colors are still backed by Integers
	- No more base type changes are expected, but sorry about this one, this had to be done and it had been a while under work.
	- Triggers are gone.
