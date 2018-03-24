#args format:

#ROUTE FORMAT: <IATA_CODE_FROM>-<IATA_CODE_TO>
#DATE FORMAT: M/d/yy;

#sorting types: bestflight, price, or duration

#ticket types:
	#oneway: "oneway <ROUTE> <DATE> <sortType>"
	#roundtrip: "roundtrip <ROUTE> <DATE_DEPART> <DATE_RETURN> <sortType>"
	#multicity: "multicity <ROUTE1> <DATE1> <ROUTE2> <DATE2> ... <sortType>"

#examples:
	#"oneway BOS-LAX 6/2/18 bestflight"
	#"roundtrip JFK-ORD 5/18/18 6/1/18 price"
	#"multicity BOS-LHR 6/8/18 LHR-CDG 6/15/18 CDG-BOS 6/20/18 duration"


#change this
args = "roundtrip BOS-LAX 4/2/18 4/10/18 bestflight"

#do not change this
command: "java -jar FlightPriceTracker.widget/FlightPriceTracker.jar " + args

refreshFrequency: "5 hrs"

render: (output) -> """
	<div class = "container">
		#{output}
	</div>

"""

afterRender: (_) -> 
	@refresh()


update: (output) ->
	$(".container").html(output)

style: """
	top: 15px
	left: 25px

	.trip
		background-color: rgba(0, 55, 122, 0.7)
		border: 5px solid black
		border-radius: 50px
		font-family: Futura
		color: white
		text-align: center
		padding: 5px

	#route
		font-size: 25px
		padding-left: 20px
		padding-right: 20px

	#separator
		padding-bottom: 5px

	#date
		font-size: 20px

	#headingSep
		border: 0
		height:2px

	hr
		border-color: black
		border-style: dotted

	.tickets
		background-color: rgba(87, 183, 220, 0.7)
		border: 5px solid black
		border-radius: 50px
		height: 200px
		padding: 10px
		text-align: center
		overflow-y: scroll
		overflow-x: hidden

	a
		text-decoration: none
		color: inherit

	.ticket
		color: rgb(28, 28, 28)
		padding-top: 5px
		font-family: Monaco
		font-size: 12px

	.price
		margin: auto
		font-family: Gill Sans
		font-size: 30px
		text-align: center
		border: 1px dashed black
		padding:5px
		width: 25%


	.flight
		margin:auto

	.airline
		padding-left: 10px


	.time
		padding-left: 10px
		padding-right: 10px

	.numStops
		text-align: center

	.axis
		font-family: Geneva
		font-size: 15px

	.duration
		line-height: 15px
		text-align: center

	.departure, .arrival, .stops
		position: relative
		top: 16px
		height: 60px

	.city
		position: relative
		right: -17px

	.error
		background-color: rgba(213, 0, 45, 0.7)
		font-family: Monaco
		font-size: 12px
		padding: 10px
		border: 5px solid black
		border-radius: 10px

"""

