BukkitMonitor
=========

BukkitMonitor is a java GUI for admins. It shows data pulled realtime from the server.

Compiling
---------

You need to have Maven installed (http://maven.apache.org). Once installed,
simply run in saved directory:

    mvn clean package install
    
Maven will automatically download dependencies for you.

Contributing
------------

We happily accept contributions. The best way to do this is to fork
BukkitMonitor on GitHub, add your changes, and then submit a pull request. We'll
look at it and merge it into BukkitMonitor if everything
works out.

Your submissions have to be licensed under the GNU General Public License v3.

General Concepts
----------------

The entry point for all of BukkitMonitor is in `com.hsun324.montior.MonitorPlugin`.
This is where the event classes are registered and deregistered. The events
themselves are found in the `com.hsun324.monitor.Events` class.

Each user has a PlayerTracker that handles the output and customization of their
descriptions, including  their position and events. Each world also has a WorldTracker
that handles the output and customization of their descriptions, including their current
time and name.

The `MainWindow` class in the `com.hsun324.monitor.swing` package handles displaying and
updating certain data and visuals. The `WindowInterface` class redirects events and console
updates. Icons shown in the `Events` and `Console` panels are saved in the
`com.hsun324.monitor.icons` package. Classes refer to IconsList to retrieve references to
the icons. The icons are part of the led-icons icon set (http://led24.de/iconset/). Read the
LED-ICONS README.