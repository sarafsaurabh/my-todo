# Pre-work - MyTodo

MyTodo is an android app that allows building a todo list and basic todo tasks management functionality including adding new tasks, editing and deleting an existing task.


Submitted by: Saurabh Sraf

Time spent: 4 hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove tasks** from the todo list
* [x] User can **tap a todo task in the list and bring up an edit screen for the todo task** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo tasks** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo tasks [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo tasks in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo tasks (and display within listview task)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing tasks
* [x] Add support for selecting the priority of each todo task (and display in listview task). (This is achieved using color-coded due dates)
* [x] Tweak the style improving the UI / UX, play with colors, images or backgrounds
* [x] Anything else that you can get done to improve the app functionality!

The app has all the functionality mentioned above. It also has a calendar widget to select the due date for a todo task. Additionally the todo tasks are displayed along with the days left for the task due date with color codes. Items which are past due are colored in red, tasks for future are green and tasks due soon are yellow in color. The app features a clean UI with a new app icon.


## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/D7bd7Ad.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).


## License

    Copyright 2015 Saurabh Saraf

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
