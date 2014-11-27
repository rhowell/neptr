# NEPTR - Never Ending Program Transmogrifying Robot

NEPTR, is really freakin cool, especially for a non-sentient little fella.

NEPTR lives for one reaason, and one reason alone:  To make your software deployment workflow easier.  Initially, he talks to HipChat and Go Continuous Delivery server.  While it's only a little bit quicker than using the Go client directly, the biggest advantage is the increased visibilty within your team.  He'll initiate builds, communicate project status, setup and tear down environments via puppet, chef, bash scripts, really any command that he has permission to run.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2014 Ronnie Howell
