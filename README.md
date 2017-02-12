# tweetapi

> Tweet Api using which, tweets can be posted, users can follow other users
> Tweets can be displayed in reverse chronological order
> Users can see the feed from other users, displaying the tweets in reverse chronological order

Tweet API provides the following functionality:

#### Post a feed

``tweets/addTweet{userId}`` can be used to post a feed for the given user
 
> Sample request
 <pre>
    POST http://localhost:8080/tweets/addTweet/Jana
      {
        "text" : "sample tweet"
      }
  </pre>
  
#### Get my tweets
      
``tweets/{userId}`` can be used to fetch the feeds for the given user in reverse chronological order
       
> Sample request
  <pre>
    GET http://localhost:8080/tweets/Jana
  </pre>
        
#### Follow a user
  
``tweets/followUser/{userId}?followingUser=Mark`` can be used to follow another user
   
 > Sample request
 <pre>
     POST http://localhost:8080/tweets/followUser/{userId}?followingUser=Mark
 </pre>
    
#### Get my tweet feed from my following users
      
``tweets/feed/{userId}`` can be used to fetch the feeds from the following users in reverse chronological order
       
> Sample request
  <pre>
    GET http://localhost:8080/tweets/feed/Jana
  </pre>
  
### The API can be visualised based on the integrated swagger ui

> Run the application using the command ``mvn spring-boot:run``
> Open the swagger ui using the url ``http://localhost:8080/swagger-ui.html``
