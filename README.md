# AsciiImage

This is a Java application made using JavaFX and Gradle that will convert normal png files into ascii versions. You can then export this into a text file or another png. To first run the application either use the shell or batch script at the top of the zip file. All you gotta do is select a png image and then specify the amount of pixels per character and click ```Convert```.

You are able to pan and zoom around the photo as you would expect.

The ```Switch``` button is used to switch between the original image and the ascii one so it will be blank if you switch before converting.

The reason this project is special is I have created the PNG decoder/reader myself. Also my github actions are dapper with it.

If you're on a mac or linux you might have to chmod the shell script/java exe to run it I dunno how to fix that.
## Images
### Gui
![](images/gui.png)
### Before
![](images/celeste.png)
### After
![](images/ascii.png)

## Building from source
To build from source you first are going to want to `git clone` the repo. To run the project use `./gradlew run`. To build the project into an image use `./gradlew jlink` or `./gradlew jlinkZip` for a zip file. I wouldn't recommend trying to just use `./gradlew build` unless you have JavaFX installed and configured properly.

## Contributing
If you come across any issues please file an issue or pull request that would be greatly appreciated.
