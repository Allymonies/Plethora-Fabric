# Plethora-Fabric

Plethora-Fabric is a port of [Plethora](https://github.com/SquidDev-CC/plethora) for Fabric 1.18+. The port is still
ongoing.

## Port status
### Handheld modules
- [x] Kinetic augment
- [x] Frickin' laser beam
- [x] Block scanner
- [x] Entity sensor
- [ ] Keyboard
- [x] Neural interface
  - [ ] Peripheral API
  - [x] Module API

### Peripheral modules
- [ ] Manipulator
- [ ] Chat recorder
- [ ] Overlay glasses
- [ ] Introspection nodule
- [ ] Kinetic augment
- [x] Frickin' laser beam
- [ ] Block scanner
- [x] Entity sensor
- [ ] Redstone integrator

### Other features
- [ ] Minecart computer
- [x] Turtle upgrades
- [ ] Pocket upgrades
- [ ] Vehicle upgrades
- [x] Cost system
  - [ ] Config
- [ ] Module blacklist & method configuration
- [ ] Recipe book advancements

### Integration
- [x] API
- [ ] ComputerCraft integration (?)
- [ ] Vanilla integration (?)

# Plethora (original Forge readme)

Plethora is a ComputerCraft/CC:Tweaked peripheral provider for Minecraft 1.8.9+. It aims to provide both metadata and
peripherals for vanilla Minecraft and most mainstream mods.

Plethora also adds a series of "modules" to the game. These modules can be used by the player with varying success.
They really come in to their element when put in a manipulator, providing a series of methods which allow
interacting with your environment. This includes:
- Introspection: investigating the current player's inventory (and ender chest)
- Scanner: scans blocks in an area, gathering metadata about them.
- Sensor: scans entities in an area. Like the scanner this allows getting metadata.
- Frickin' laser beam. It fires lasers.
- Chat recorder: allows listening to chat messages, capturing and sending messages as the owning player.
- Kinetic augment: allows remote access to your muscles, making them even stronger than before.

If you've ever wanted to embed a computer in your skull then today is your lucky day. Plethora provides a neural
interface which can be attached to your head, or some unsuspecting animal or monster. Right clicking the entity with a
neural controller allows you to interact with it. You can insert modules (which will be wrapped as peripherals) and
manipulate them with the built-in computer. Building a cyborg army has never been so easy.

You can also add a kinetic augment to the neural interface. This allows controlling the host entity in various ways.

## Requirements
- Minecraft with recent version of Forge
- [CC: Tweaked](http://minecraft.curseforge.com/projects/cc-tweaked)

## Documentation
There is pretty comprehensive documentation on [the Plethora website](https://squiddev-cc.github.io/plethora/). This
contains tutorials, explanations of several fundamental concepts and thoroughly explained examples.

You can also create a HTML dump of all methods by running the command `/plethora dump out.html`. When run in a single
player world this will save a file in the active directory (normally `.minecraft` or your modpack's folder). This
documentation is also [available online](http://squiddev-cc.github.io/plethora/methods.html).

## Images
![](https://squiddev-cc.github.io/plethora/images/squids-laser.png)

> You know, I have one simple request. And that is to have ~~sharks~~ squid with frickin' laser beams attached to their heads!

![](https://squiddev-cc.github.io/plethora/images/modules.png)

> Various modules available
