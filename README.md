# azalea
Memory game for Android

LIBGDX: https://libgdx.badlogicgames.com/documentation.html#gettingstarted

Install: (https://wiki.archlinux.org/index.php/android)

Enable multilib @ /etc/pacman.conf

Install required things:
```
yaourt -S --noconfirm android-sdk android-sdk-platform-tools android-sdk-build-tools
```

Fix some rights issues: 
```
groupadd sdkusers
gpasswd -a <user> sdkusers
chown -R :sdkusers /opt/android-sdk/
chmod -R g+w /opt/android-sdk/

#switch group on this terminal, otherwise you need to reboot
newgrp sdkusers
```

Select platform and install it:
https://en.wikipedia.org/wiki/Android_version_history
```
yaourt -S android-platform-<VERSION>
```

Install eclipse plugin: https://dl-ssl.google.com/android/eclipse/

Import in Eclipse: File -> Import -> Gradle -> Gradle Project

Fix assets path:
Go to Run => Run Configurations.. => choose DesktopLauncher, Arguments Tab => Working Directory => Others then browse to yourproject-android/assets/ and 
click Apply => Run

# BUILDING:

## Android:
``gradlew android:assembleRelease``

package will be in:
android/build/outputs/apk/android-release-unsigned.apk

## Desktop:
``gradlew desktop:dist``

package will be in:
desktop/build/libs/desktop-1.0.jar