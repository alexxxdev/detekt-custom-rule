# detekt-custom-rule

[![](https://jitpack.io/v/alexxxdev/detekt-custom-rule.svg)](https://jitpack.io/#alexxxdev/detekt-custom-rule)
[![Build Status](https://travis-ci.com/alexxxdev/detekt-custom-rule.svg?branch=master)](https://travis-ci.com/alexxxdev/detekt-custom-rule)

Add it in your build.gradle
```
detektPlugins "com.github.alexxxdev:detekt-custom-rule:0.5.1"
```

Add it in your config detekt
```
CodeStyle:
  RxJavaSubscription:
    active: true
  BlankLineBeforeFunctionDeclarationRule:
    active: true
    autoCorrect: true
    ignoreFunctionalInterfaces : true|false (defaultValue = false)
    ignoreAnonymousObjects : true|false (defaultValue = false)
  NoBlankLineInValueParameterListRule:
    active: true
    autoCorrect: true
  BlankLineBeforeFirstPropertyDeclarationRule:
    active: true
    autoCorrect: true
```
