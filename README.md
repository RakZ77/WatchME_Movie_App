# WatchMe 🎬

A Netflix-style Android movie browser app built with Java, powered by the TMDB API, with Supabase backend and Room local persistence.

**Package:** `kh.edu.rupp.watchme`

---

## Features

- **Home Screen** — Sectioned layout with multiple movie categories (Popular, Top Rated, Now Playing, etc.) using nested RecyclerViews
- **Movie Details** — Full detail view with runtime, genres, cast, reviews, and an About tab inside a ViewPager2 tabbed interface
- **Cast Tab** — Browse the full cast for any movie, pulled from TMDB credits endpoint
- **Search** — Auto-search with 500ms debounce, infinite scroll pagination, and fallback to the discover endpoint when the query is empty
- **Watchlist** — Locally persisted, user-specific watchlist backed by Room; survives offline
- **Reviews** — Post and read movie reviews stored in Supabase, displayed per movie

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| Architecture | MVVM (ViewModel + Repository + LiveData) |
| Networking | Retrofit + OkHttp |
| Image Loading | Picasso |
| Local Storage | Room |
| Backend | Supabase (auth + reviews/profiles) |
| Movie Data | TMDB API |
| Serialization | Gson (`@SerializedName`) |

---

## Architecture

The app follows strict MVVM:

```
UI (Fragment / Activity)
    └── ViewModel (extends ViewModel)
            └── Repository
                    ├── TMDbService (Retrofit)
                    └── SupabaseService (Retrofit)
```

- `ViewModel` never extends `AndroidViewModel` unless `Application` context is specifically needed
- `Repository` methods return a **new** `MutableLiveData` instance per call (not a shared class-level field) to prevent data cross-contamination across concurrent calls
- `Fragment` observes via `ViewModelProvider`

---

## Project Structure

```
app/
└── src/main/java/kh/edu/rupp/watchme/
    ├── activity/
    │   └── DetailsAboutMovieActivity.java
    ├── adapter/
    │   ├── SectionAdapter.java          # Outer vertical RecyclerView
    │   ├── CategoryMovieAdapter.java    # Inner horizontal per section
    │   └── CastAdapter.java
    ├── fragment/
    │   ├── HomeFragment.java
    │   ├── SearchFragment.java
    │   ├── WatchlistFragment.java
    │   ├── CastFragment.java
    │   └── ReviewFragment.java
    ├── viewmodel/
    │   └── MovieViewModel.java
    ├── repository/
    │   └── MovieRepository.java
    ├── model/
    ├── network/
    │   ├── RetrofitClient.java
    │   ├── TMDbService.java
    │   └── SupabaseService.java
    ├── db/
    │   └── (Room database, DAOs, entities)
    └── utils/
        ├── SessionManager.java
        └── TokenRefreshHelper.java
```

---

## Setup

### Prerequisites

- Android Studio Hedgehog or later
- Java 11+
- TMDB API key
- Supabase project (URL + anon key)

### Configuration

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/watchme.git
   ```

2. Create a `local.properties` or `secrets.xml` file and add your keys:
   ```xml
   <string name="tmdb_api_key">YOUR_TMDB_API_KEY</string>
   <string name="supabase_url">YOUR_SUPABASE_URL</string>
   <string name="supabase_anon_key">YOUR_SUPABASE_ANON_KEY</string>
   ```

3. Open the project in Android Studio and sync Gradle.

4. Run on an emulator or physical device (API 24+).

---

## Key Implementation Notes

### TMDB API

- List endpoints (`/movie/popular`, etc.) return only `genre_ids` — a separate `/movie/{id}` call is required for full details including `runtime` and full `genres`
- Credits are fetched via `/movie/{id}/credits`

### Supabase

- All API calls are wrapped in `TokenRefreshHelper.refresh()` to handle JWT expiration (401 errors) gracefully
- Relationship queries use explicit foreign key hints (e.g., `profiles!review_user_id_fkey`) to avoid PGRST201 ambiguity errors

### Room

- Watchlist entries use a composite primary key `{id, userId}` so each user has an independent list
- Use `fallbackToDestructiveMigration()` during development; increment the database version with every entity change

### Session Management

`SessionManager` stores the access token and `userId` in `SharedPreferences` after login. These are used across all features that require user identity.

---

## Known Patterns & Pitfalls

- **Observer leaks in adapters** — When fetching data inside a RecyclerView adapter, a `currentMovieId` tag check prevents stale observers from firing on scroll
- **`TextView.setText()` with integers** — Raw `int` values are always converted to `String` before passing to `setText()` to avoid `Resources$NotFoundException`
- **RecyclerView** — Always set a `LayoutManager`; never assign the same adapter instance to two RecyclerViews; item layout height should be `wrap_content`

---

## Dependencies

```groovy
// Networking
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:okhttp:4.11.0'

// Image loading
implementation 'com.squareup.picasso:picasso:2.8'

// Room
implementation 'androidx.room:room-runtime:2.6.1'
annotationProcessor 'androidx.room:room-compiler:2.6.1'

// ViewModel & LiveData
implementation 'androidx.lifecycle:lifecycle-viewmodel:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata:2.7.0'

// ViewPager2
implementation 'androidx.viewpager2:viewpager2:1.0.0'
```
---

## Our app looks
<img width="2897" height="3016" alt="Night Mode Screens" src="https://github.com/user-attachments/assets/4c690ddd-6435-4cf9-9e2d-79c15f152dac" />

---

## License

This project is for educational purposes at RUPP (Royal University of Phnom Penh).
