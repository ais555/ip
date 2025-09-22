# john chatter - User Guide

welcome to john chatter, a chatbot that helps you manage your various tasks

## Adding basic todos

Adds a todo to your list.

Format: `todo NAME`

Examples: 

`todo cs2103t project`

`todo cook dinner`

## Adding tasks with a deadline

Adds a task with a deadline to your list.

Format: `deadline NAME /by WHEN`

Examples:

`deadline ma1522 homework /by monday`

`deadline return books /by 22-9-2025`


## Adding events with a start and end time

Adds a task with a deadline to your list.

Format: `event NAME /from START /to END`

Examples:

`event exams /from 6-10-2025 /to 11-10-2025`

`event recess week /from 22-9-2025 /to 28-9-2025`

## View your list of tasks

Displays a list of your tasks with their relevant info.

Format: `list`

## Delete

Removes a task from your list by its index.

Format: `delete INDEX`

Examples: 

`delete 1`

`delete 2`

## Mark complete

Marks a task as complete using the index of the task on the list.

Format: `mark INDEX`

Examples:

`mark 1`

`mark 2`

## Mark incomplete

Marks a task as incomplete using the index of the task on the list.

Format: `unmark INDEX`

Examples:

`unmark 1`

`unmark 2`

## Tag

Adds a tag to categorise a task. Each task can have multiple tags.

Format: `tag INDEX TAG_NAME`

Examples:

`tag 1 school`

`tag 1 important`

`tag 2 fun`

## Untag

Removes an existing tag from a task.

Format: `untag INDEX TAG_NAME`

Examples: 

`untag 1 school`

`untag 2 fun`

## Find by name

Display a filtered version of your list with tasks with names that match the keywords.

If more than one keyword is given, tasks must match ALL keywords.

Format: `find KEYWORD OPTIONAL_KEYWORD OPTIONAL_KEYWORD ...`

Examples: 

`find homework`

`find math homework`

## Find by tag

Display a filtered version of your list with tasks that have the specified tag(s).

If more than one tag is given, tasks must match ALL tags.

Format: `findtag TAG OPTIONAL_TAG OPTIONAL_TAG ...`

Examples:

`findtag school`

`findtag school impt`

