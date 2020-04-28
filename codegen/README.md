## repl

repl used for convenience encode and decode, download abi-codegen-0.2.0 from [release page](https://github.com/Lbqds/eth-abi/releases)

usage:

```
$ ./abi-codegen-0.2.0 -i
```

start repl mode, and only have 3 command now:

```
>>load absolute-path-abi-file
```

```
>>encode selector [params]
```

```
>>decode selector params
```

for example([Token](https://github.com/Lbqds/eth-abi/tree/master/examples/src/main/scala/examples/token)):

```
>>load /absolute-path/Token.abi
```

output:

```
load completed
```

```
>>encode approve 0x0C256268f13CACeF89e15c7512c5fCf3f6775680 100
```

output:

```
0000000000000000000000000c256268f13cacef89e15c7512c5fcf3f67756800000000000000000000000000000000000000000000000000000000000000064
```

```
decode totalSupply 0000000000000000000000000000000000000000000000000000000000000064
```

output:

```
(0000000000000000000000000000000000000000000000000000000000000064)
```

ctrl-d to quit

thanks to [ammonite-terminal](https://github.com/lihaoyi/Ammonite/tree/master/terminal/src), you can use ctrl-r/h/b/f/a/e etc.

TODO: support tuple, event
