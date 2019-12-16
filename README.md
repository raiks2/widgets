# Back-end for the application manipulating widgets

## Principles

This application is built with the "hexagon" architecture (aka ports and adapters) in mind.
Consequently, layers are not articulated, there is only the core and the surrounding infrastructure.
However, distinction is made between the domain (application-agnostic) and application-specific parts
that are placed in separate packages.

Controllers sit on the program boundary and convert user input into calls to the use cases
implemented as application services. Those orchestrate domain objects and speak to the infrastructure
via interfaces in a decoupled way.
