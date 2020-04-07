# BASE

## Coursework 4: London Property Marketplace
The BASE Property Viewer is a simple application that allows users to view and explore details of properties that are available to rent in London. 
### Structure

* Initial window upon running the application contains multiple panels.
    * This initial window (to be referred to as the main window) will have functionality to move between different panels.
       The task sheet suggests we do this with forwards/backwards buttons and specifies their functionality, but we will use 'tab' buttons and will justify this in our report.
    * There are two slider bars that allows users to pick their desired rental price range. 
    * Swapping between panels is initially disabled until a price range is selected. This is because the other panels will use the users selections in showing information. We intend on including more selections.
* **Panel 1: Welcome**
    * This panel is the initial panel described above. It should include instructions on how to use the application and allow them to make their selections before moving to other panels.
* **Panel 2: The Map**
    * The goal of this panel is to geographically represent the data.
    * The geography is split up into London's boroughs.
    * The darker the colour, the more properties are available in that borough. When a user clicks a borough, a new window pops up and displays a list of all properties available in that borough.
    * Users can mark listings as favourites.
    * Google Maps integration to show the location of each listing.
    * When a property is selected from this list, more information should be made available in a new window.
* **Panel 3: Statistics**
    * This panel shows statistics in a new window.
* **Panel 4: Account Panel**
    * This panel allows the user to view, compare and share the various properties that they have favourited.
*  **Unit Testing**
    * We have performed unit testing on the ApplicationController class using JUnit 4.
