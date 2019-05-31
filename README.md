# pdfreader-android
Holds implementation of a PdfReader library for Android.

### Project Structure

This repository's project has two modules: `api` and `app`. The library lives in the `api` module and `app` provides a basic app that implements the library.

### Usage

The library can be used by an activity that implements the `api`'s `PdfFragmentListenerType` interface to pass in data the fragment needs and respond to published events.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        .
        .
        .
        this.readerFragment = PdfReaderFragment.newInstance()

        this.supportFragmentManager
            .beginTransaction()
            .replace(R.id.pdf_reader_fragment_holder, this.readerFragment, "READER")
            .commit()
}
```

Currently the library only accepts an InputStream, but this can be extended in the future.
```kotlin
    override fun onReaderWantsInputStream(): InputStream {       
        var fileStream = assets.open(this.assetPath)
        return fileStream
    }

    override fun onReaderWantsTitle(): String {
        return this.documentTitle
    }

    override fun onReaderWantsCurrentPage(): Int {
        return this.documentPageIndex
    }

    override fun onReaderPageChanged(pageIndex: Int) {
        this.documentPageIndex = pageIndex
    }
```

### Dependencies

The pdfreader api package depends on an [Android wrapper of the Pdfium renderer](https://github.com/barteksc/AndroidPdfViewer).

This is via api's `build.gradle`. `api` is required over `implementation` otherwise you will get a "Supertypes of the following classes cannot be resolved..." build error. See [this SO answer for more details](https://stackoverflow.com/a/44419574).

```groovy
dependencies {    
    // pdf viewer
    api 'com.github.barteksc:android-pdf-viewer:3.1.0-beta.1'
}
```
