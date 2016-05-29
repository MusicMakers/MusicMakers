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
# **Team name: MusicMakers**

**Student names: Kenneth Tan Xin You / Yong Jia Wern**

** **

**Scope of our project:**

 

Takes in a piece of music without lyrics and generates a new piece of music

Anybody who has a working browser and a piece of music

 

**Brief Description:**

 

We plan to create a web application that enables users to generate their own music content with the click of a button.

 

The idea is simple. Using our application, users will select songs of his/her choice and input it into our programme. Then with just click of a button, a completely new piece of song will be generated based on the songs the user's input.

 

Features:

    	1) 	Generating music content based on user's input.

 

Planned features:

    	1) 	Extending music input to music that has lyrics.

    	2) 	Ability to share music generated on popular social media websites (Facebook, Twitter, etc.)

**Execution Plan:**

We will be using the markov process to derive the new song with markov model generated from the inputs. As for the keys in the markov model, we thought of using chords, notes, or just segments of predetermined duration. As we research more about audio files, we realise that they are mostly in binary format. A mp3 file is divided into small blocks (frame) of constant length 0.026 sec. Initially, we thought of using these frames as the key. But Jia Wern come across the Jmusic library which may better assist us in processing the audio file. Thus, we have not decided which will be the better approach, more discussion is required here.

Stacks considered: Spring framework, Jmusic, App.js, Gulp

 

**Proposed Level of Achievement:**

Advanced - Apollo 11

 

We foresee that working with music instead of text will be more difficult in that processing music information using a programming language might not be very straightforward.

 

**Motivation:**

 

This idea stemmed from one of our Problem Sets that Kenneth and I had to complete in CS2020. In that problem set, we were supposed to generate a random text based on whatever text we input. Hence, we thought it would be a cool and challenging idea to extend this concept to something that is not text-based - music. We do not really hear much about applications that compose music for you, so we think that this idea is novel and would like to see how much we can accomplish in this 3 short months.

 

**User Stories:**

 

    	1) 	As a user, I want to be able to create my own music to perhaps impress my friends.

