# Distributed_Shared_Calendar

## Description

The application is able to work on different sites that is connected to each other, the program maintains a calendar that is shared to all sites in the network using Wuu-Bernstein Algorithm. The internal mechanism for this algorithm is to maintain replicated log and dictionary for each site. 

You'll be able to "schedule", "cancel" appointments with other users, and will be able to "view" all appointments or your appoinments set on the Calendar. The history of the Calendar is open for everyone by reading "log".

## Installation

You'll need different sites connected to each other to use the application and share your appointments and calendar. We tested the program on docker with multiple containers, which simulates a environment with multiple sites in a network.

Git pull the whole directory for all sites you want to make shared appointments. For each site, go to the directory and run the build.sh to set up the environment using the following command on your command window.
```
./build.sh
```

Go to \bin directory and run the program.
```
./run.sh
```

Now you are all set to use the application!

## Usage

### schedule
You can shedule an appointment with other users using the command
```
schedule <name> <day> <start_time> <end_time> <participants>
```
### cancel
Cancelling an appointment
```
cancel <name>
```
### view
To view all appointments set on the calendar
```
view
```
### myview
To view all appointments involving this site
```
myview
```
### log
To view the history of the Calendar
```
log
```
