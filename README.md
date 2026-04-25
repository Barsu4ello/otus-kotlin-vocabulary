# Vocabulary Trainer

> 💬 Все комментарии будут заданы с данной пометкой для удобства проверки преподавателем(будут удалены перед защитой)

Vocabulary Trainer — это учебное backend-приложение для изучения иностранных слов.
Пользователь может добавлять слова в личный словарь и тренироваться с помощью тестов.
> 💬 В рамках проекта будет реализованы только CRUDS для объекта слово(Так как не вижу смысла пилить логику тестирования)

## Визуальная схема фронтенда

![img.png](imgs/front-design.png)

> 💬 Полный дизайн я пару лет назад делал тут(сам, ручками):  
> https://www.figma.com/design/wY7XfdGYXmF30YMbKtWVks/Goose-English?node-id=0-1&p=f&t=E9AcQvAnQIrJXGjr-0  - дизайн  
> https://www.figma.com/proto/wY7XfdGYXmF30YMbKtWVks/Goose-English?node-id=1-2&starting-point-node-id=1%3A2 - сразу прототип. Можете пощелкать. Там даже гусь двигается

## Документация
1. Маркетинг и аналитика  
i. [Маркетинг](docs/marketing.md) (💬 сделано нейронкой)  
ii.[Бизнес-документация](docs/business.md) (💬 сделано нейронкой)
2. Архитектура  
i. [ADR](docs/adr.md) (💬 сделано нейронкой)  
ii. [Описание API](docs/api.md)  (💬 сделано нейронкой)  
iii. [Архитектурные схемы](docs/arch-scheme.md) (💬 чуть переделал только первый слайд, остальное у Вас свистнул)  


## Структура проекта  
1. Модуль 1: Введение в Kotlin  
[m1l1-first](./lesson) - Вводное занятие, создание первой программы на Kotlin  
2. Модуль 2: Учебный проект  
[test-module](./vocabulary-be/test-module) - Тестовый модуль в качестве дз (💬 будет удален)

## Плагины Gradle сборки проекта  
[build-plugin](./build-plugin) Модуль с плагинами  
[BuildPluginJvm](./build-plugin/src/main/kotlin/BuildPluginJvm.kt) Плагин для сборки проектов JVM  
[BuildPluginMultiplarform](./build-plugin/src/main/kotlin/BuildPluginMultiplatform.kt) Плагин для сборки мультиплатформенных проектов