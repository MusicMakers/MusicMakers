---
title: MusicMakers Readme
layout: post
author: MusicMakers
permalink: https://docs.google.com/document/d/1ibLBYhWZTW0mX0cEHLhvqKe1cPpWKY1XwfyqrdN73No/edit?usp=sharing
tags:
- created using gabriel - the markdown angel
source-id: 1ibLBYhWZTW0mX0cEHLhvqKe1cPpWKY1XwfyqrdN73No
published: true
---
Team name: MusicMakers

**Student names: Kenneth Tan Xin You / Yong Jia Wern**

**Advisor: Shen Xinyu**

**Mentor: Laurence Putra**

# **Brief Description:**

We, MusicMakers, plan to create a web application that enables users to generate their own music content with the click of a button.

 

The idea is simple. Using our application, users will select songs of his/her choice and input it into our programme. Then with just click of a button, a completely new piece of song will be generated based on the songs the user's input. Anybody who has a working browser and music will be able to use our application to compose a new song.

**Motivation:**

 

This idea stemmed from one of our Problem Sets that Kenneth and I had to complete in CS2020. In that problem set, we were supposed to generate a random text based on whatever text we input. Hence, we thought it would be a cool and challenging idea to extend this concept to something that is not text-based - music. We do not really hear much about applications that compose music for you, so we think that this idea is novel and would like to see how much we can accomplish in this 3 short months.

# **Execution Plan:**

**Before we started on the project:**

For the music generating portion, we thought of using the markov process to derive the new song with markov model generated from the inputs. The basic idea behind the markov model is that it will first choose a key to work with. It could be words, in the case for texts, or in our case, notes. For our case, it will then analyse how the notes progress, and analyse how frequent a note comes after another. Of course, the keys in the markov model need not just be notes. We thought of using chords or just segments of predetermined duration. 

**What we have done so far:**

**May:**

As we research more about audio files, we realise that they are mostly in binary format. A mp3 file is divided into small blocks (frame) of constant length 0.026 sec. Initially, we thought of using these frames as the key. Jia Wern came across the Jmusic library which may better assist us in processing the audio file and began learning its functionalities.

Stacks considered: Spring framework, Jmusic, App.js, Gulp

**June:**

For the music generating portion, we found out that the JMusic library indeed has many functionalities. In particular, the one functionality that we thought could help us tremendously is the read.audio() method. Essentially, this method enables us to take in an uncompressed music file and it will output a floating point array. We tried to apply the markov model to these floats, but the output music file we got was merely noise. Upon consultation with our mentor, he suggested that instead of using the traditional markov model of analysing how frequent one float comes after another, we could analyse chunks of floating point numbers instead. We made the necessary adjustments, but to no avail; the output music file is either noise or just silence.

While researching on the functionalities of JMusic, we also learnt that Java has a package javax.sound.midi. We decided to give this approach a shot, while understanding the limitations of this approach, namely that we can only take a midi music file and output a midi music file. It could be a problem as midi files might not be very common nowadays. Nevertheless, considering that our previous approach did not seem to yield any good result, we decided to give this a shot, using the same markov model. Since this midi approach allows us to extract the notes and the rhythm of the input music, we decided to use the markov model to analyse these two features and output a midi file based on these features. We managed to get a midi file that actually had music!

**July: **

The main focus for this month is to put everything together, ie making sure that our website works properly and is able to output a music file. We will discuss more of which of the improvements (mentioned below) we might consider implementing.

**Limitations of algorithm:**

1. Requires input music file to be a midi file, and output file will also be a midi file.

2. Algorithm can only read in one part, and is unable to read chords (ie when more than one note is played at the same time) from input midi file.

3. Output of midi file is currently only limited to piano, which may be boring.

 

**Improvements:**

Below are some possible improvements:

1. Since the method that works requires the input music file to be a midi file, we might consider having a conversion application (found online) to ease conversion of music file.

2. Include an option for users to not be required to have their own music file. We could simply have a few pre-programmed music files on the application itself, and users can choose to use these midi files. Hence, users may be limited to only be able to input midi files which include instruments that can only play one note at a time (eg no piano).

3. Include ability for users to choose their preferred choice of tempo, length of song they would like to generate, instrument of choice, and also how closely they want the song to sound like to the original song. 

4. Include ability for programme to analyse and output chords.

5. Include a link for users to share their music with their friends.

6. Beautify the web application

**Resources:******Git Branching Model - [http://nvie.com/posts/a-successful-git-branching-model/](http://nvie.com/posts/a-successful-git-branching-model/)

Git Mistake Correction - [https://www.kernel.org/pub/software/scm/git/docs/user-manual.html#fixing-mistakes](https://www.kernel.org/pub/software/scm/git/docs/user-manual.html#fixing-mistakes) Dropzone (considering) - [http://shrestha-manoj.blogspot.sg/2014/07/dropzonejs-spring-mvc-hibernate.html](http://shrestha-manoj.blogspot.sg/2014/07/dropzonejs-spring-mvc-hibernate.html)

