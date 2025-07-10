Decorative Stands - Source Installation Guide
This mod uses the standard Minecraft Forge MDK (Mod Development Kit) setup.
It applies small patches to the vanilla MCP source code, allowing access to data and functions needed for modding.

These patches are based on "un-renamed" MCP source code (also known as SRG names). This means the patched code may not be easily readable unless deobfuscated through mappings.

Setup Instructions for Developers and Contributors
Step 1: Extract or clone the mod source folder
Open your command-line terminal and navigate to the folder where the project was extracted or cloned.

Step 2: Choose your preferred development environment
Eclipse:

Run the following command:
./gradlew genEclipseRuns

Open Eclipse.
Go to File > Import > Existing Gradle Project, then select the mod's folder.

Alternatively, you can run:
./gradlew eclipse

IntelliJ IDEA:

Open IntelliJ and select "Open Project."

Choose the build.gradle file inside the mod folder.

After opening, run:
./gradlew genIntellijRuns

If required, use "Refresh Gradle Project" inside IntelliJ's Gradle panel.
