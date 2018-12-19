# Planets

Please read this document before playing. It is preferred to read it at [the github page](https://github.com/Dysta/Java-Project/blob/master/README.md).

This is a licensed project, any reuse of our code should include a citation.

You can find the project's Javadoc [at this link](http://planets.rhythmgamers.net/javadoc/).

## Intro

Planets is a Real-Time Strategy game (RTS) based in a randomly generated solar system. You start with one planet and your goal is to defeat all your enemies.

This project is a small open source student project game built with the JavaFX library.

## How to play

At the start of the game, you and other players are assigned a random color. Your default color is light blue, and you can find which one is your planet by using `CTRL+A` which will select it.

To select one or more planets, the controls are :
- Left click - Selects the clicked planet
- CTRL + Left click - Selects the clicked planet whithout deselecting previously selected planets
- CTRL + A - Select all planets

When you have selected one or more planets, you can left click another planet to start a Mission.
- Enemy planet - Attack
- Friendly planet - Convoy

By default, all ships stored in the selected planets will be assigned to the given mission, but you can change the percentage either with the scroll wheel or with the UP and DOWN arrows.

When the mission has started, squads of optimal size (related to the planet's display size) will be sent in waves. You can select one or more squads with `Left Click` and `CTRL + Left Click` to change their destination.

There are several ship types :
- The basic ship, fast and reliable.
- The burst ship, very fast but quite weak.
- The mother ship, expensive and powerful but slow.

Each planet has its own storage space and its own production type. The hangars can be used for any ship type as convenience but each ship will take one, whatever its size might be.

## Compiling

First of all, you need a working installation of [JavaFX](https://docs.oracle.com/javafx/2/installation/jfxpub-installation.htm). Without going into details, the recommended IDEs are Eclipse Oxygen and NetBeans.

After opening the project in your favorite IDE, you will need to add `resources` as a Source Package, as well as either `src_base` and/or `src_advanced` depending on the version you want to test if it is not already added.

You can run the `Tests` of the desired version if you really want to make sure you are using a stable state of the game. If the tests are failing, please checkout a tagged commit.
