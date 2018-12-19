# Planets

Please read this document before playing. It is preferred to read it at [the github page](https://github.com/Dysta/Java-Project/blob/master/README.md).
This is a licensed project, any reuse of our code should include a citation.

## Intro

Planets is a Real-Time Strategy game (RTS) based in a randomly generated solar system. You start with one planet and your goal is to defeat all your enemies.

This project is a small open source student project game built with the JavaFX library.

## How to play

At the start of the game, you and other players are assigned a random color. You can use the control `CTRL+A` (used to select every owned planet) to find which one is yours.

To select one or more planets, the controls are :
- Left click - Selects the clicked planet
- CTRL + Left click - Selects the clicked planet whithout deselecting previously selected planets
- CTRL + A - Select all planets

When you have selected one or more planets, you can left click another planet to start a Mission.
- Enemy planet - Attack
- Friendly planet - Convoy

By default, all ships stored in the selected planets will be assigned to the given mission, but you can change the percentage either with the scroll wheel or with the UP and DOWN arrows.

When the mission has started, squads of optimal size (related to the planet's actual size) will be sent in waves. You can select one or more squads with `Left Click` and `CTRL + Left Click` to change their destination.

## Compiling

First of all, you need a working installation of [JavaFX](https://docs.oracle.com/javafx/2/installation/jfxpub-installation.htm). Without going into details, the recommended IDEs are Eclipse Oxygen and NetBeans.

After opening the project in your favorite IDE, you will need to add `resources` as a Source Package, as well as either `src_base` and/or `src_advanced` depending on the version you want to test if it is not already added.

You can run the `Tests` of the desired version if you really want to make sure you are using a stable state of the game. If the tests are failing, please checkout a tagged commit.
