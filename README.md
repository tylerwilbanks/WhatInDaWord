# What in da word!?

This is a [wordle clone](https://www.nytimes.com/games/wordle/index.html) I made to get my feet wet with the newly
emerging kotlin multiplatform ecosystem.

**Desktop**, **android**, and **iOS** platforms are supported.

# 📱 Android and iOS

You can find the android and ios versions on their respective app
stores [via my website.](https://www.minutesock.com/what_in_da_word.html)

# 🔑 Key features

* **Daily Mode**: This mode works the same way wordle works, where the word is the same for everyone that day (according
  to device date).
* **Sharing**: You can share your game results the same way you can with wordle.
    * Example share text:

```
      What in da word!?
      Date: 2024-12-21 📅
      Mode: Daily 📆
      3/6
      0m 38s
      🟨⬛⬛🟨⬛
      ⬛⬛🟨🟨🟩
      🟩🟩🟩🟩🟩
``` 

* **Infinity Mode**: This works the same way as daily mode except you may play over and over again, not limited by the
  day!
* **Timer**: Each game is timed to see how fast you can solve the puzzle, which you can see in the sessions tab and in
  the shareable text.
* **Definitions**: The app fetches definitions from a [free dictionary api](https://dictionaryapi.dev/) and displays
  them to you after game completion.
* **Dictionary**: The app keeps track of *unlocked* words as you play, where you can view all the definitions for that
  word and all your game sessions for that word.

# 🖥️ Desktop

To run the desktop version:

1. clone this project
2. open it in android studio
3. open the cmd prompt
4. ensure your current path is at the project root and type `./gradlew run`.
5. Enjoy!

# 🤓 Nerdy Details

* 👤 This is a [compose multiplatform](https://www.jetbrains.com/compose-multiplatform/) project, so all ui is shared.
* I only had to write 1 line of swift:

```
ComposeView().ignoresSafeArea(edges: .all)
```

* 💾 Local database: I used [SqlDelight](https://sqldelight.github.io/sqldelight/2.0.2/).
    * I tried using [Room](https://developer.android.com/training/data-storage/room/) first, but had difficulty setting
      it up/writing tests with it and the alpha kmp version made me abandon ship to a more mature sqldelight.
    * SqlDelight is wonderful. I love the paradigm shift of generating kotlin from sql instead of generating sql from
      kotlin.
    * SqlDelight leaves something to be desired in the object mapping department, to where I had a tough time using
      `JOIN`s and mapping those results to kotlin objects.
* 💉 Dependency Injection: I used [Koin](https://insert-koin.io/).
    * I prefer manual dependency injection over dependency injection frameworks, but koin did make the whole android
      context easier to deal with.
    * I enjoy this library and used it for clean architecture style data sources and http client to make testing
      easier (or if I want to swap out one of these implementations later).
* 🌐 Http Client: I used [Ktor](https://ktor.io/)
  with [Kotlinx Serialization](https://kotlinlang.org/docs/serialization.html)
    * This is how I fetch definitions from [that free dictionary api](https://dictionaryapi.dev/)
    * This was a delightful experience. I ran into 0 issues working with these.
* 💾 Local datastore: I used [Androidx Datastore](https://developer.android.com/topic/libraries/architecture/datastore)
    * With this, I was able to store shared preferences for toggling dark mode, showing the startup animation, using
      system theme for light/dark mode instead of dark mode override.
    * It was very easy to observe these preferences, create delegates out of them, and even make my own reactive
      composable for reactive app theme changes:

```
@Composable
fun <T> rememberPreference(
    dataStoreDelegate: DataStoreDelegate<T>,
    defaultOverride: T? = null
): State<T> {
    return dataStore
        .data
        .map { preferences: Preferences ->
            preferences[dataStoreDelegate.key] ?: defaultOverride ?: dataStoreDelegate.defaultValue
        }
        .collectAsStateWithLifecycle(defaultOverride ?: dataStoreDelegate.defaultValue)
}
```

* 🧭 Navigation: I used [Compose Navigation](https://developer.android.com/develop/ui/compose/navigation)
    * In the past, this library left something to be desired with routes, but now with type-safe routing, this was
      actually kinda pleasant to work with. 🌟
* 💫 Shared Element Transition: I managed to get in a shared element transition when on the dictionary screen, navigating
  to dictionary detail, the word travels to the next location.
* 🍫 App Bar Navigation: Dynamic bottom app bar/side navigation rail based on screen size and device rotation!