# Torpid
Project Torpid

---

# Description

Torpid is a disk drive sleep prevention utility, designed primarily for keeping external HDD's awake. It works by writing an empty text file named `Torpid_Keep_Alive_File.TXT` to the drive in a loop specified by a time interval.

---

# Requirements

1. **Windows**
2. **Java 11** Runtime or Greater

---

# Features (Current)

* Ability to select multiple drives at once using shift-click or control-click
* Ability to set the time interval between keep alive writes (default is 2.5 seconds)
* Simple user interface that is informative
* Utilises multithreading, one thread per selected drive

---

# Features (Upcoming)

* Linux support

---

# Usage

1. Double-click the `Torpid.jar` file provided, or open a terminal and type `java -jar Torpid.jar`
2. Select the desired drives to keep awake from the left list by left clicking, shift-clicking or control-clicking, to select one or multiple drives
3. Set the desired time interval if you want to modify it, or leave it with the default 2.5 seconds
4. Click the Start button
5. The Start button turns into the Stop button after it's pressed, so press the now Stop button to stop it
