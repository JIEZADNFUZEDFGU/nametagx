# NameTagX

![NameTagX Banner](https://i.ibb.co/tPvXTh4S/nametagx-1.png)

**NameTagX** is a modern, powerful and fully configurable **NameTag management plugin** for **Minecraft 1.21+** servers running **Paper or Spigot**.

It is designed to be **future-proof**, **high-performance**, and **easy to use**, without relying on NMS or packets.

---

## ✨ Features

![Customization Banner](https://i.ibb.co/QFKWWSnz/nametagx-2.png)

* Full NameTag customization (prefix, suffix, color)
* Group-based system with priorities
* Permission-based management (LuckPerms compatible)
* Custom per-player tags
* Real-time updates (no relog required)
* Public API for developers
* YAML-based configuration
* Compatible with TAB, chat and scoreboard
* 100% Bukkit API (NO NMS)

---

## 🔐 Permissions & Groups

![Permissions Banner](https://i.ibb.co/9D39W69/nametagx-3.png)

NameTagX uses a **priority-based group system**:

* Higher priority groups override lower ones
* Groups are linked to permissions
* Players can receive temporary or permanent tags

### Example permissions

```
nametag.admin
nametag.reload
nametag.group.owner
nametag.group.vip
```

Compatible with **LuckPerms**, **PermissionsEx**, and other permission plugins.

---

## ⚡ Performance & Stability

![Performance Banner](https://i.ibb.co/Kz2XBHq7/nametagx-4.png)

* No NMS (future-proof)
* No packet injection
* Minimal scoreboard usage
* Internal caching system
* Async-safe updates
* Designed for large servers

Tested on **Minecraft 1.21** with excellent TPS stability.

---

## 📦 Requirements

* Java **21**
* Paper **1.21+** or Spigot **1.21+**
* Permission plugin (recommended: LuckPerms)

---

## 📥 Installation

1. Download `NameTagX.jar`
2. Place it in the `plugins/` folder
3. Restart your server
4. Configure `groups.yml`
5. Assign permissions

---

## ⚙️ Configuration Example

```yaml
groups:
  admin:
    priority: 100
    prefix: "§c[ADMIN] "
    suffix: ""
    color: RED
    permission: nametag.group.admin
```

⚠️ **Minecraft limitation:** Prefix + suffix are limited to **16 visible characters** (color codes excluded).

---

## 🧩 Commands

| Command                         | Description            |
| ------------------------------- | ---------------------- |
| `/nametag reload`               | Reload configuration   |
| `/nametag set <player> <group>` | Set a player tag       |
| `/nametag reset <player>`       | Reset player tag       |
| `/nametag list`                 | List available groups  |
| `/nametag info [player]`        | Show tag info          |
| `/nametag language <lang>`      | Change plugin language |

Aliases: `/ntag`, `/nt`

---

## 🌍 Languages

* 🇬🇧 English (en_US)
* 🇫🇷 French (fr_FR)
* 🇪🇸 Spanish (es_ES)
* 🇩🇪 German (de_DE)

You can easily add your own translations.

---

## 🧑‍💻 Developer API

```java
NameTagX plugin = (NameTagX) Bukkit.getPluginManager().getPlugin("NameTagX");
NameTagAPI api = plugin.getAPI();

api.resetPlayerTag(player);
```

Maven dependency:

```xml
<dependency>
  <groupId>fr.nametagx</groupId>
  <artifactId>NameTagX</artifactId>
  <version>1.0.0</version>
  <scope>provided</scope>
</dependency>
```

---

## 🛠️ Troubleshooting

* Make sure players have the correct permissions
* Avoid using multiple NameTag plugins
* Use `/nametag reload` after configuration changes

---

## 🤝 Contributing

Contributions are welcome!

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Open a Pull Request

---

## 📄 License

MIT License – free to use, modify and distribute.

---

## 📞 Support

* [Discord](https://discord.gg/v4eyXvSrex) 

---

⭐ If you like **NameTagX**, consider starring the repository!
