# Forgified Loom

A fork of [Fabric Loom](https://github.com/FabricMC/fabric-loom) that supports the Forge modding toolchain.

Note that if ForgeGradle works fine for you, *use it*.
This is not meant to be a complete replacement for ForgeGradle,
and there are probably many bugs and limitations here that FG doesn't have.

## Usage

Starting with a Fabric project similar to the example mod,
switch your Loom to this fork, like with [Chocohead's Loom fork](https://github.com/Chocohead/Fabric-Loom/).

`settings.gradle`:
```diff
pluginManagement {
	repositories {
		jcenter()
		maven {
			name = 'Fabric'
			url = 'https://maven.fabricmc.net/'
		}
		gradlePluginPortal()
+		maven {
+			name = 'Forge'
+			url = 'https://files.minecraftforge.net/maven'
+		}
+		maven {
+			name = 'Jitpack'
+			url = 'https://jitpack.io'
+		}
	}
+	resolutionStrategy {
+		eachPlugin {
+			if (requested.id.id == "fabric-loom" && requested.version?.endsWith("-SNAPSHOT") != true) {
+				useModule("com.github.Juuxel.fabric-loom:fabric-loom:${requested.version}")
+			}
+		}
+	}
}
```
`build.gradle`:
```diff
plugins {
-	id 'fabric-loom' version '0.5-SNAPSHOT'
+	id 'fabric-loom' version '<commit hash here>'
}
```

Then you need to set `loom.forge = true` in your `gradle.properties`,
and add the Forge dependency:

```groovy
forge "net.minecraftforge:forge:1.16.4-35.1.7"
```

You also need to remove the Fabric Loader and Fabric API dependencies.
You should also remove any access wideners and replace them with a Forge AT.

### Mixins

Mixins are used with a property in the `loom` block in build.gradle:

```groovy
loom {
	mixinConfig = "mymod.mixins.json"
}
```

## Limitations

- Launching via IDE run configs doesn't work on Eclipse or VSCode.
- You have to use Gradle 5, like with FG. I've reused it as a library
  in some places. 
- The srg -> yarn remapper used for coremod class names is *really* simple,
  and might break with coremods that have multiple class names per line.
