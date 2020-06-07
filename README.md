# SeeWay-HacktheNorthEast-

## Inspiration

Did you know that the average ***cost per truck accident is around $14,000?***

That is a huge amount of loss! 

Another fact that really struck us is that up to ***53% of truck drivers reported physical & psychological issues*** - which inadvertently leads to a higher chances of accidents. 

During transportation of goods, we also realized that the loss incurred due to the ***damage caused to the products is at least 150% of the price of the goods being shipped.*** What a quite extravagant amount of money to be borne by a company! 

And in our current state of a global pandemic, it is crucial to ensure the flow of essential goods to all. But did you know that in our hometown of Tamil Nadu in India, ***out of 6000 cases recorded, 15,00 cases were linked to a vegetable market!***

People are scared to now go get essential and the most basic necessities- fruits & vegetables!

These facts really made us think on how we can employ technology to create a wholesome solution that helps retailers/suppliers/traders to run their businesses better and safe. 

That is why we have come up with our product - **Seeway**.  We really want to help businesses - especially, the local suppliers/traders  in India who have not yet witnessed a digital transformation in their business

**We strive to empower our customers build the right future. We aim to be your partner in ensuring the safety of your people as well as your goods. By joining us, you have taken the first step towards understanding your employees as well as ensuring safety for your customer.**

After all, the current situation made it crystal clean to all of us of what actually matters in life and food/supplies is definitely top of the list!


## What it does

Our product - Seeway - is a holistic system that allows drivers and managers(retailers/suppliers/traders) to manage and control their day to day work of moving goods from one place to another. 

It has the following modules:
1. **Accident Prevention** - We install a webcam in the dashboard of the truck as well a proximity sensor. We use the datapoints(incl of speed of truck) to figure out how likely it is for a driver to meet with an accident. We used OpenCV and Keras API to build our ML model and Thingspeak for capturing data from our IoT sensors. An alarm will be sent to our app in order to wake the driver up and also notify the manager.

2. **Commodity Monitoring**- We install a humidity/temperature sensor, shock sensor, vibration sensor, & smoke sensor in the back of the truck where the commodities reside during transport. We use the data points from these sensors and analyse them to ensure that the commodities are in the right environment settings. If not, an alarm will be sent to the driver through our app as well as a notification sent to the manager. 
3. **Driver Health Check-IN** - Drivers can now upload a medical document containing results of their illness , in our case due to the current situation, COVID-19 tests. The image will be uploaded into our database where Google will then extract text and see if the particular driver has been tested negative or positive. Accordingly, the drivers need to constantly upload medical results every month so as to ensure safety for him/her as well as others. The manager can have an overview of all the health statuses of his/her drivers.
4. **Informed Maps**- Drivers and Managers have overview of a map in the app that shows the red zones. There is also information regarding the state tax he/she is in during transit. There is also overview of the distance covered and time duration to reach the destination.
5. **Analytics Dashboard:**- Managers have an overall view of the trends seen in the environment of the items in transit as well as drivers in a comprehensive web app.
6. **Currency Rates**- Managers can easily see the trends of the economy as well as the present currency rates.
7. **Customer Base**- The app has also links to third-party market places like Ali Baba, Market India & so on - these links are helpful for local traders/suppliers using our app since most of them are not aware of the popular online market places out there. 

## How we built it

* We primarily used Android Studio to develop our Android App. 
* We used Firebase as our real-time database to store all our data points. 
* ML-Kit was used for text extraction and Keras API was used to build our ensemble model. 
* OpenCV was used for Image processing of video of driver to see if he was sleepy or not. 
* Dash was used to develop our Health Dashboard that shows overview of data to Manager. 
* Material UI was used to design our interface.
* Thingspeak cloud was used to receive our data from sensors and process streaming points. 


## Challenges we ran into

1. Hardware components were really hard to get due to the COVID-19 situation. But we have written the codes nevertheless with the sensors we had. 
2. 36 hours was a tight deadline for us - since we were doing it virtually and also both of us are 1000s of miles apart - attaining virtual collaboration in the first day was hard.
3. Much more smaller challenges here and there - but most importantly, we came through it all!

## Accomplishments that we're proud of
1. Getting our BlinkRate detection model working was a great feeling! OpenCV is truly awesome!
2. Using Firebase ML-Kit was an intriguing experience since we had never used it before - using the text extraction was hard at first but then we got it to work. 
3. Meeting a wonderful bunch of people! :)
4. Learning as much as we could from the mentors who were kind to guide us through everything. 

## What we learned

One of the key takeaways from this experience is us realizing that local business really need to be empowered by technology. We really understood the problems in essential businesses run in India today. Especially by those who are local such as the kiranas. 

One of our team member's (Sakthisreee) grandfather is a chillies wholesaler and we had talked to him to get idea on how we could empower his business. One thing to know before moving ahead - traders like Sakthisree's grandfather don't possess a computer or use any other advanced medium in their stores. Also, the drivers they employ come in sick due to chillies-based illnesses and they get overworked a lot. Yet, so far, these businesses have run as usual, but with a certain amount of disadvantages. Taking such a situation, imagine what would happen if we gave a technological make-over for all such businesses! It would be great for them as well for the country's economy. 

Apart from this, we learnt a lot technically - how to use ML Kit, get an Android App up & running and also, cool stuff like pitching!

## What's next for Seeway

From what you read before , it is clear that we really want to give opportunities for the local traders and suppliers to utilize tech in their business. We aim to further improve our platform and app and add more comprehensive functionalities - adding in native languages as well apart from English.

Our next upgrade would be to make a digital twin of the supply chain operations so that the process is mapped from beginning to end and exclusive control is given to the managers in charge. Digitization also results in data - this data can be used for analytics and looped intelligence. 
