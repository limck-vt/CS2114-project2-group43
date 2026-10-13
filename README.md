Team 43: Liam, Isaac, Nathaniel

Passing the Plunger is a chore-rotation app for college roommates. All data is stored in the provided generic LinkedChain<T>.

How to Compile and Run
- Install the Extension Pack for Java
- Run the program: open src/ChoreApp.java and click Run
- Run the tests: open the Testing panel and click Run All Tests

Reflection

What changed from our specification?
- Added a Chore class (name + priority), because storing chores as plain Strings left nowhere to keep their priority
- chores changed from LinkedChain<String> to LinkedChain<Chore>, so the chain now holds 3 element types: String, Chore, ChoreAssignment
- Added addChore(chore, priority), while addChore(chore) still works and defaults to Medium
- Added getLastMessage() so choreManager sets the error messages and ChoreApp prints them
- Added choreManager(Random) so the tests get a predictable schedule
- Added small helpers: getRoommateCount(), getChoreCount(), getAssignmentCount(), hasRoommate()
- getAssignmentsByPriority returns null for an invalid priority
- createSchedule is O(n) instead of the spec's O(n²), because it calls toArray once instead of get(i) in a loop

What changed in the LinkedChain?
- Nothing.

What happened to our unknowns?
- More chores than roommates: solved, since some roommates get multiple chores
- More roommates than chores: solved, since some roommates get none that week
- Sorting by priority: solved. The chain isn't sorted, and the schedule is grouped by priority when it's displayed.
- Removing while looping: solved by copying the chain to an array first
- Still open: if a roommate has more than one chore, only their first one can be swapped

What doesn't work yet?
- Removing a roommate leaves their chores unassigned until the schedule is created again
- New roommates or chores don't show up until the schedule is created again
- Roommate names are case-sensitive ("john" and "John" count as different people)
- No stretch goals yet

Next Steps

Finish the MVP
1. Reassign a removed roommate's chores to the others (Liam)
2. Let the user pick which chore to swap (Nathaniel)
3. Add new roommates and chores to an existing schedule (Isaac)
4. Make roommate names case-insensitive (Liam)

Stretch goals
- Swap requests: already done
- Chore history: Isaac
- Reminder messages: Nathaniel
- Overdue flags: dropped since the app runs week by week with no due dates

New tests needed
- removeRoommate: chores get reassigned, and removing the last roommate is handled
- Swapping a specific chore, plus a roommate who doesn't have that chore
- getHistory(): after create, swap, and rotate, and when the history is empty
- Reminders: a roommate with chores, a roommate with none, and no schedule yet
- New ChoreApp menu options, including bad input

Attribution

LinkedChain.java | CS 2114 Staff 
Chore.java | Liam
ChoreAssignment.java | Nathaniel
ChoreManager.java | Isaac
ChoreApp.java | Isaac
LinkedChainTest.java | Liam
ChoreTest.java | Liam
ChoreAssignmentTest.java | Nathaniel
ChoreManagerTest.java | Isaac
ChoreAppTest.java | Nathaniel

GenAI note
- We used Claude from Anthropic to assist in finding gaps with testing, ensuring out tests cover what they need to. We wrote the tests ourselves but had it give suggestions on what else we need to add.
- Earlier, we used ChatGPT to assist with our spec sheet formatting, and review all suggestions it gave and then implemented them if applicable.
