# Decorative Stands - Source Installation Guide

This mod uses the standard **Minecraft Forge MDK** (Mod Development Kit) setup.  
It applies small patches to the vanilla MCP source code, allowing access to data and functions needed for modding.

> These patches are based on _"un-renamed"_ MCP source code (also known as **SRG names**).  
> This means the patched code may not be easily readable unless deobfuscated through mappings.

---

## 🔧 Setup Instructions for Developers and Contributors

### Step 1: Extract or Clone the Mod Source Folder
Open your command-line terminal and navigate to the folder where the project was extracted or cloned.

---

### Step 2: Choose Your Preferred Development Environment

#### 🛠️ Eclipse

1. Run the following command:
   ```bash
   ./gradlew genEclipseRuns
