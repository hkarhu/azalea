# azalea
Memory game for Android


LIBGDX: https://libgdx.badlogicgames.com/documentation.html#gettingstarted

Install: (https://wiki.archlinux.org/index.php/android)

Enable multilib @ /etc/pacman.conf
yaourt -S --noconfirm android-sdk android-sdk-platform-tools android-sdk-build-tools 
groupadd sdkusers && gpasswd -a <user> sdkusers
chown -R :sdkusers /opt/android-sdk/ && chmod -R g+w /opt/android-sdk/

#quick switch group on this terminal
newgrp sdkusers

Select platform and install it:
https://en.wikipedia.org/wiki/Android_version_history
yaourt -S android-platform-<VERSION>

Install eclipse plugin: https://dl-ssl.google.com/android/eclipse/

Import in Eclipse: File -> Import -> Gradle -> Gradle Project

Fix assets path:
Go to Run => Run Configurations.. => choose DesktopLauncher, Arguments Tab => Working Directory => Others then browse to yourproject-android/assets/ and 
click Apply => Run

BUILDING:

