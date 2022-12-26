# YoutubePlaylistWatcher

A tv app made for the elderly that cannot navigate YouTube and want to watch a collection of the same videos. App allows users to add new folders, videos and playlists which are saved in Firebase Realtime Database and Users log/sign in managed through firebase auth with auto Log in if user already logged. Implemented a YouTube using YouTube player library to play videos with basic tv controller functionality, videos can be skipped with controller (longer pressed the faster seconds skipped) . Also includes resume functionality. Used YouTube Data API to retrieve all necessary data.

The main goal and purpose of this app was to make navigation and interaction as simple as possible, as this app was desgined for elderly people.

Thanks for reading; // :)


## App Functionality and Look:

Note: All app navigation and interaction has implemented for the remote control.

#### Log In/Out, Sign up through firebase auth:

![ezgif com-gif-maker (5)](https://user-images.githubusercontent.com/46162359/209477326-e60d5467-5959-4780-a0ab-33c603c2a29c.gif)

#### Directory, Videos and playlist adding (Saved in Realtime Database, used Youtube Api to get all relevant data):

###### Adding playlist Link And Folders

![ezgif com-gif-maker (6)](https://user-images.githubusercontent.com/46162359/209499291-6509f954-705b-4ea1-bf65-4bc65db1e4b8.gif)

###### Adding Video Link - Video file name optional

![Project Name (2)](https://user-images.githubusercontent.com/46162359/209498948-11730070-694f-4dbc-8cb8-e631f2102128.gif)

#### Youtube Video Player
###### Implmented Remote Controller functionalities such as skipping video, video skip quicker the longer pressed. Videos are also resumed.

![ezgif com-gif-maker (7)](https://user-images.githubusercontent.com/46162359/209500090-72b80ce3-d90f-44d5-8a21-d3a5eed04eb3.gif)
