# EventBus 1.3

Yet another Event Bus

The planned features are:
- Core event bus library
  - Subscribers - done
  - Producers - done
  - Tokens for subscribers (token can either be posted or supplied by the event method annotated with @Token)
- Multi threading support with delivering on the same thread which registered - done
- Timed events posting with repeated and cancel option - done (not completely covered by unit tests)
- Android main UI thread support - done
- AWT main loop support - done

The documentation will be added as soon as the development finishes ;)

Until that, please see the JavaDocs...

## Build system and development IDE

I like and use IntelliJ IDEA so the project is maintained in it.

The project, however, can be built by Gradle w/o any IDE. I am still learning Gradle so the build will be slowly improved...
