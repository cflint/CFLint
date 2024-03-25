![CFLint](/src/main/resources/logos/CFLint-logo.jpg)

# Building CFLint

## Quick Start

    git clone https://github.com/cflint/CFLint.git
    cd CFLint
    git checkout dev
    gradlew clean build
    CFLint will be built to build\libs

## Using Eclipse

    Start eclipse
    Help>Eclipse Marketplace
    Install 'BuildShip' for Gradle integration

### Eclipse - Import existing project

    File>New>Other>Gradle>Gradle Project
    Name the project, uncheck 'Use default location', browse to your git clone.

### OR, Eclipse - check out with eGit

    Window>Show View>Git Repositories
    past the git url for CFLint
    CFLint>Branches>Remote Tracking>origin/dev  
    right click on origin/dev and checkout.

### Eclipse - Refresh and Build Eclipse CFLint project

    Right click on CFLint in the project navigator,
    Gradle>Refresh Gradle Project.
    (use when the build.gradle file changes)

### Eclipse - Running JUnit tests

    Right click on src/test/java/com/cflint/integration/TestFiles.java
    Run As>JUnit Test
    This will run most the majority of the unit tests.

## Adding a JUnit test

The best way to add a new test to the CFLint test suite is to add it to:

    src\test\resources\com\cflint\tests\

as pure CF code.  Give the file a name that describes the scenario you are testing and a `.cfm` or `.cfc` extension.  The first time you run TestFiles, the build will create
a file with the same name as your input, but with an extension of `.expected.txt`.  Future runs will only pass if the actual results match the 
expected file.
For CFLint, this is better practice than writing the JUnit test yourself.

## Contributing

Contributions are welcome.  Here's how you make your first code or test contributions:

* Fork CFLint on GitHub.
* Make your changes in a local branch created from the `dev` branch.
* Push your changes to your GitHub repo.
* Create a pull request targeting the `dev` branch of the main repository.

Also see [CONTRIBUTING.md](/CONTRIBUTING.md)
