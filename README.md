Audio Match
==========
I once searched Audio Matches algorithms on google, most of the solutions used 
FFT to convert the audio samples into Frequency domains  and then calculate the
[fingerprints](http://en.wikipedia.org/wiki/Acoustic_fingerprint) on the frequency domain. 
Examples include:
* ['Shazam'](http://www.ee.columbia.edu/~dpwe/papers/Wang03-shazam.pdf)
* ['Microsoft'](http://developer.nokia.com/community/wiki/Sound_pattern_matching_using_Fast_Fourier_Transform_in_Windows_Phone)
* ['S.V.Rice'](http://www.comparisonics.com/AES_ID31.pdf)

[shazam](http://www.ee.columbia.edu/~dpwe/papers/Wang03-shazam.pdf) Describe a algorithm which use an FFT over small windows of time in the audio's samples to
create a [spectrogram](http://en.wikipedia.org/wiki/Spectrogram) of the audio. Below is a spectrogram example refered from [WILLDREVO](http://willdrevo.com/fingerprinting-and-audio-recognition-with-python/) But the FFT and peaks fingerprints have a higher time complexity, you need calculate the FFT for each time windows and count spectrograms peaks in  the frequency domain.
![spectrogram](plot/Spec.png)

Instead of FFT aproach, I think the amplitudes in time domain is sufficient enough to distinguish the audio files.As the following wave graph shows, the average amplitude of each time window is different. So for a audio file, we can get a list of average amplitudes and compare these to one of other audio files just like compare two line plot.
![wave](plot/audioWav.png)
![avePlot](plot/aveAmp.png)



##Compile:
Use Makefile, under project directory type: 
$ make
$ chmod +x ./dam
$ ./dam -f <pathname> -f <pathname>
$ ./dam -d <pathname> -d <pathname>
$ ./dam -f <pathname> -d <pathname>
$ ./dam -d <pathname> -f <pathname>



## How does it work?

