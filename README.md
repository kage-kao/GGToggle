# GGToggle Plugin v2.0 для PowerNukkitX 2.0

> Плагин для выдачи игрокам права переключать свой игровой режим с выживания на творческий и наоборот.
---
### Скачать

- **Готовый JAR:** [GGToggle.jar](https://raw.githubusercontent.com/kage-kao/GGToggle/main/GGToggle.jar)
- **Исходники ZIP:** [GGToggle-source.zip](https://raw.githubusercontent.com/kage-kao/GGToggle/main/GGToggle-source.zip)

### Требования

- PowerNukkitX 2.0.0-SNAPSHOT или выше
- Java 21+

### Установка

1. Скачай `GGToggle.jar`
2. Положи в папку `plugins/` твоего сервера
3. Перезапусти сервер
---
## Команды:

### Для админов (ОП):

| Команда | Описание |
|---------|----------|
| `/gg <ник>` | Выдать игроку право менять режим |
| `/gg remove <ник>` | Забрать право у игрока |
| `/gg list` | Показать список игроков с правом |

### Для игроков:

| Команда | Описание |
|---------|----------|
| `/gm` | Переключить свой режим (Выживание ↔ Творческий) |

### Как это работает

1. Админ пишет `/gg nickname555` — даёт право игроку (даже если тот никогда не заходил)
2. Игрок `nickname555` заходит на сервер и пишет `/gm` — переключает свой режим
3. Если нужно забрать право: `/gg remove nickname555`

### Примеры

```
/gg Steve          → Стиву выдано право менять режим
/gg remove Steve   → У Стива забрано право
/gg list           → Показать всех с правом
/gm                → Переключить свой режим (для игрока)
```

### Файлы данных

Список игроков сохраняется в `plugins/GGToggle/players.yml`

### Права доступа

| Право | По умолчанию | Описание |
|-------|--------------|----------|
| `ggtoggle.admin` | op | Управление правами игроков (/gg) |
| `ggtoggle.use` | true | Использование /gm (но нужно быть в списке) |

### Компиляция из исходников

```bash
# Скачай PowerNukkitX
mkdir libs
curl -L -o libs/powernukkitx.jar "https://github.com/PowerNukkitX/PowerNukkitX/releases/download/snapshot/powernukkitx-shaded.jar"

# Скомпилируй (нужен JDK 21+)
mkdir -p bin
javac -cp libs/powernukkitx.jar -d bin/ src/com/example/ggtoggle/GGToggle.java

# Собери JAR
cp plugin.yml bin/
cd bin && jar cvf GGToggle.jar com/ plugin.yml
```
