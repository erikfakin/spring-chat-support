# Support Live Chat Application Built With Spring Boot & React - WebSocket 
This is a Spring Boot/React application that provides some basic features for a support service live chat.
It uses WebSocket to send notifications which are handled by the frontend.
It has a client side and a dashboard. A user who needs support enters its name and email, after which a new chatroom is created. He/she can then chat with the support.
In the dashboard the support can view all the chatrooms and respond to the client.


## Demo
You can check out a live version of the application at <a href="https://chat.erikfakin.eu" target="_blank">Live Support Chat</a>


## Features
- Live chat
- Notification based chat
- Message seen status with time
- Chatroom online/offline status
- Simple and intuitive design


## Motivation
I made this project to explore WebSocket communication. I chose to build a support service live chat because it is a good way to applicate the features of WebSocket.


## Challenges
The first challenge I had to solve was live communication. I chose to approach this problem by implementing a 'notification based' system with WebSocket. After receiving a new notification the frontend side of the application updates its state based on the notification type using regular fetch requests.


## Usage
To start the application you can run `npm run` for the frontend part and run the backend Spring Boot application.

To make a production build you can run `mvn clean install`. The command will automatically run 'npm run build' and copy the files in the necessary location, packaging all in a fat jar that can then be run on a server.


## Features to add
- User authentication/authorization for support users
- Search all chatrooms by client user
- Chatroom pagination