![logo](https://raw.githubusercontent.com/lemcoder/TinyComposer/refs/heads/main/iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/AppIcon-40%403x.png)
# Tiny Composer

[![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-blue.svg?logo=kotlin)](http://kotlinlang.org)

> This is my submission for the [Kotlin Student Coding Competition](https://kotlinconf.com/contest/). The [300 word essay](https://raw.githubusercontent.com/lemcoder/TinyComposer/refs/heads/main/Essay.md) is included in this repository.

Tiny Composer is a Kotlin Multiplatform app that helps users create simple musical pieces using artificial intelligence. The project is designed to showcase the power of Kotlin for music generation and provide an intuitive user experience suitable for both beginners and experienced creators.

https://github.com/user-attachments/assets/857d793e-12c5-4976-8b8d-ae5412dbf552


## How does it work?
The app uses [Gemini AI](https://deepmind.google/technologies/gemini/) to generate chord progressions and melodies that are easily understood and parsed. Once the chord progression is generated, Tiny Composer uses a custom-built Kotlin library to convert the progression into MIDI messages. The app then plays the music using [soundfont sample-based audio synthesis](https://en.wikipedia.org/wiki/SoundFont).

Platform support:
| Android |iOS |
|--|--|
|:heavy_check_mark:|:heavy_check_mark:|

## Features
* Create chord progressions and melodies by hand or using AI
* Playback indicator for timing accuracy
* Change playback instruments
* Save projects and export to WAV
* Persist project files on device

## How to Use?

App consists of 5 screens:

* Project List screen
* Project Creation screen
* Project Details screen
* Project Options screen
* AI Generate screen

To access main functionalities of the app follow these instructions:

#### Create Project
To create a project press the `+` icon on project list. Then input the project name, tempo (in BPM) and time signature. When you are ready press `save` button. You will be redirected to project list screen again once project is created.

#### Delete Project
To delete project swipe from right to left on project list item when on project list screen. The icon of a garbage bin will be shown. Press the garbage bin and the project will be deleted. You can undo this operation by pressing `Undo` on snackbar at the bottom of the screen.

#### Load sample project
When the project list is empty the option to load sample project will be shown. Once You press the button the sample project will be added to projects lists. You can now access the sample project by clicking on it's list item

#### Open project
To open project click on its list item while on projects list screen.
> Note! To save project You need to go back to projects lists. Closing the app before going back to projects list will result in lost work!

#### Add new chord or note 
Once on project details screen You can switch between chords and melody modes by selecting the tab on top of the screen. When in chords mode the add button in bottom right of the screen will display wheel picker that can be used to select the base note of a chord. Once selected new major chord will be added to project. When in melody mode the will picker selection will result in adding new note to the project

#### Remove chord or note
To remove chord or note long press on chord/note item in main section of the screen

#### Duplicate chord or note
To duplicate chord or note double-tap the chord/note item in main section of the screen

#### Edit chord or note
To edit chord or note press on it's item in main section of the screen. You will be presented with a bottom sheet when You can edit properties of chord/note. Editable properties are:
| Chord                                                                        | Note                                                   |
|------------------------------------------------------------------------------|--------------------------------------------------------|
| Octave: The octave that chord will be played in                              | Octave: The octave that note will be played in         |
| Volume: The volume of chord                                                  | Volume: The volume of note                             |
| Duration: Indicates how long chord should be played for                      | Duration: Indicates how long note should be played for |
| Chord type: Used for selection of chord type (eg. minor or dominant seventh) |                                                        |

#### Play or stop audio
To play or stop audio press the play button in the bottom of the project details screen

#### Change project options
To display project options press the hamburger menu in top right of the screen. You will be presented with input to change the project tempo as well as two other inputs responsible for changing the SoundFont preset for melody and chords. Presets are loaded from the sound font
> Advanced: To load custom sound font file replace the [font.sf2](https://github.com/lemcoder/TinyComposer/blob/main/shared/src/commonMain/composeResources/files/font.sf2) file. SoundFonts up to 50MB should work fine.
Options are saved automatically.

#### Generate chords or melody
To use AI to generate chords or melody press the AI Generate button in bottom left corner of project details screen. You will be presented with four options to generate chords and melody and the prompt window. Once you write your prompt press the `confirm` button and wait for generation to complete. Once it's completed you will be navigated back to project details screen.

#### Export project to WAV file
Once in project options screen You can export the project as WAV file. To do this press the `export to .wav` button. Once file is generated the system file sharing tray will be displayed allowing You to make a decision on what to do with generated file.

## Building
This project does not require any additional steps to build. To run app on device follow these instructions:
| Android                                                                                                                                      | iOS                                                                                   |
|----------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------|
| Clone the project and open it inside AndroidStudio, then run gradle `build` task.  Once the build is finished You can run the app on device. | Clone the project and open the iosapp.xcproj in XCode. Select device and run the app. |

### Technologies used

Languages: <b>Kotlin, Swift</b>

Platform: <b>Android, iOS</b>

Library dependencies:
| Library                                            | What is it used for?                                   |
|----------------------------------------------------|--------------------------------------------------------|
| [Kotlinx IO](https://github.com/Kotlin/kotlinx-io)               | Filesystem IO operations                               |
| [Kotlinx Coroutines](https://github.com/Kotlin/kotlinx.coroutines)       | Concurrency and Threading                              |
| [MikroSoundFont](https://github.com/lemcoder/MikroSoundFont)         | (My own) SoundFont audio synthesis and MIDI operations |
| [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform) | User interface                                         |
| [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)    | JSON serialization/parsing                             |
| [Compottie](https://github.com/alexzhirkevich/compottie)        | Lottie animations                                      |
| [Koin](https://github.com/InsertKoinIO/koin)               | Dependency Injection                                   |
| [Generative AI](https://github.com/PatilShreyas/generative-ai-kmp)  | Gemini AI Multiplatform SDK                            |


### License
This project is licensed under the MIT License. See the LICENSE file for more details.

### Contact
For any questions or feedback, please reach out to:

Email: lemanski.dev@gmail.com
