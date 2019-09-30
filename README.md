# pdfreader-android

[![Maven Central](https://img.shields.io/maven-central/v/edu.umn.minitex.pdf/edu.umn.minitex.pdf.api.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22edu.umn.minitex.pdf%22)

Holds implementation of a PdfReader library for Android.

### Project Structure

This repository's project has multiple modules. `app` is a basic demo to demonstrate an implementation of the library.

The `api` module defines a common set of interfaces/classes that all reader implementations will use.

`pdfviewer` is an implementation of an [Android wrapper of the Pdfium renderer](https://github.com/barteksc/AndroidPdfViewer). 

All implementations should handle types defined in the `api` module.

### Usage

The library can be used by an Activity that implements types from the `api` package. 

#### 1. Add `api` and any implementations you will be using to `build.gradle`

```groovy
dependencies {    
    // TODO: Change this to work with nexus

    // pdf viewer
    api 'com.github.barteksc:android-pdf-viewer:3.1.0-beta.1'
}
```

#### 2. Load desired reader into a FrameLayout, (`pdf_reader_fragment_holder` here).
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        
        if (savedInstanceState != null) {
           // retrieve data and handle configuration changes
        } else {
           // Get the new instance of the reader implementation you want to load here.
           val readerFragment = PdfViewerFragment.newInstance()

           this.supportFragmentManager
               .beginTransaction()
               .replace(R.id.pdf_reader_fragment_holder, readerFragment, "READER")
               .commit()
        }
}
```

#### 3. Implement `PdfFragmentListenerType` in your reader Activity.

The Fragment will ask for metadata from the hosting Activity and you provide it here. Currently the library only accepts an InputStream, but this can be extended in the future.

```kotlin
    override fun onReaderWantsInputStream(): InputStream {
        return assets.open(assetPath)
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

    // Save this off to pass to a Table of Contents Fragment if needed
    override fun onReaderLoadedTableOfContents(tableOfContentsList: ArrayList<TableOfContentsItem>) {    
        this.tableOfContentsList = tableOfContentsList
    }

    override fun onReaderWantsTableOfContentsFragment() {
        // Get the new instance of the [TableOfContentsFragment] you want to load here.
        val readerFragment = TableOfContentsFragment.newInstance()

        this.supportFragmentManager
            .beginTransaction()
            .replace(R.id.pdf_reader_fragment_holder, readerFragment, "READER")
            .addToBackStack(null)
            .commit()
    }
```

#### 4. If chosen implementation requires and supports it, also implement the `TableOfContentsFragmentListenerType` to replace the reader with a Table of Contents Fragment

```kotlin
    override fun onTableOfContentsWantsItems(): ArrayList<TableOfContentsItem> {
        return this.tableOfContentsList
    }

    override fun onTableOfContentsWantsTitle(): String {
        return getString(R.string.table_of_contents_title)
    }

    override fun onTableOfContentsWantsEmptyDataText(): String {
        return getString(R.string.table_of_contents_empty_message)
    }

    override fun onTableOfContentsItemSelected(pageSelected: Int) {
        // the reader fragment should be on the backstack and will ask for the page index when `onResume` is called
        this.documentPageIndex = pageSelected        
        onBackPressed()
    }
```


### Dependencies

The `pdfreader` api package depends on an [Android wrapper of the Pdfium renderer](https://github.com/barteksc/AndroidPdfViewer).

This is via api's `build.gradle`. `api` is required over `implementation` otherwise you will get a "Supertypes of the following classes cannot be resolved..." build error. See [this SO answer for more details](https://stackoverflow.com/a/44419574).

```groovy
dependencies {    
    // pdf viewer
    api 'com.github.barteksc:android-pdf-viewer:3.1.0-beta.1'
}
```


This work is funded through IMLS Grant # LG-70-16-0010.
