## Tiny Composer (AI-Powered Simple Music Composition App)

### Overview
Tiny Composer is a Kotlin multiplatform app that helps users create simple musical pieces using artificial intelligence. By generating chord progressions in a human-readable format, Tiny Composer transforms musical concepts into actual MIDI audio using soundfonts, empowering musicians and enthusiasts to compose music easily.

The project is designed to showcase the power of Kotlin for music generation and provide an intuitive user experience suitable for both beginners and experienced creators.

### How It Works
The app uses Gemini AI to generate chord progressions that are easily understood and parsed. Each chord is defined with the following structure:

```ChordName:Beats```

* ChordName: Specifies the chord (e.g., Dmaj7, G7, Am7, Cmaj7)
* Beats: Specifies the duration (in beats) that the chord should play for

#### MIDI Playback
Once the chord progression is generated, Tiny Composer uses a custom-built Kotlin library to convert the progression into MIDI messages. The app then plays the music using a soundfont for high-quality audio rendering.

### Features
* Create chord progressions by hand or using AI
* Add notes such as lyrics
* Save projects and export to WAV

### How to Use
Open the App: Launch the app on your device.  
Generate Chord Progression: Tap on the "Generate" button to let the AI create a musical piece.  
Preview Music: Listen to the generated progression using the built-in soundfont player.  
Export WAV: Save or share your creation as a WAV file for distribution.

### Technologies used

Language: Kotlin

AI Engine: Gemini  
MIDI Library: Custom MIDI parsing and message generation library in Kotlin  
Audio Rendering: Custom Soundfont-based playback library based on TinySoundFont

Platform: Android/iOS

### Dependencies
~!stub

### Installation
~!stub

### License
This project is licensed under the MIT License. See the LICENSE file for more details.

### Contact
For any questions or feedback, please reach out to:

Email: ~!stub
Slack: ~!stub
```Thank you for checking out Gemini! We hope it inspires you to create beautiful music effortlessly!``` 