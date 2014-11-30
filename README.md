Audio Match
==========

Audio recognition algorithm implemented in Java, see the explanation here:  

It can tell whtere two audio files contain one or more segments sounds alike
## Dependencies:

I've only tested this on Unix systems.

* ['Java1.6']

##Compile:
Use Makefile, under project directory type: 
$ make
$ chmod +x ./dam
$ ./dam -f <pathname> -f <pathname>
$ ./dam -d <pathname> -d <pathname>
$ ./dam -f <pathname> -d <pathname>
$ ./dam -d <pathname> -f <pathname>



## How does it work?

