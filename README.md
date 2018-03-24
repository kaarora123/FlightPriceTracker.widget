# FlightPriceTracker.widget

FlightPriceTracker.widget is an Übersicht widget that displays flight information about a given trip. Users specify the required arguments in the index.coffee file and data about the top three flights found will be displayed. 

HTMLUnit is used to scrape data from Kayak. Unfortunately, because HTMLUnit is not perfect with loading JavaScript yet, the top three flights may not be completely accurate all the time. I've noticed that data is most accurate when the trip specified is oneway or roundtrip and the dates given are less than 3 months from the current date. 

### Installation
- Download [Übersicht](http://tracesof.net/uebersicht/) 
- Install Java 8 
- Download [FlightPriceTracker.widget](https://github.com/kaarora123/FlightPriceTracker.widget/blob/master/FlightPriceTracker.widget.zip) and add
it to your widgets folder.

### Usage
- Unzip the zip file
- Open [index.coffee](https://github.com/kaarora123/FlightPriceTracker.widget/blob/master/FlightPriceTracker.widget/index.coffee) and type in your arguments (make sure to read the comments!)
- Save the file (It will take a few seconds for the widget to show up because the scraping takes some time)
- Go to Übersicht "Preferences" and make sure you have an "Interaction Shortcut" specified
- Hold down the interaction shortcut and mouse over the widget to scroll through the flights.
- Clicking on a flight (while holding the shortcut) will take you to a booking link.

<p align="center">
  <img src="https://github.com/kaarora123/FlightPriceTrackre.widget/blob/master/screenshot.png?raw=true"/>
</p>

You can change the styling of the widget in [index.coffee](https://github.com/kaarora123/FlightPriceTracker.widget/blob/master/FlightPriceTracker.widget/index.coffee).

<hr/>

Please feel free to leave any suggestions or report any issues.
Thanks!


