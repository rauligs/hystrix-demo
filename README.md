# Demo Hystrix

Before you start the Demo.
---

1. Start mockserver `docker run -d -p 1080:1080 -p 1090:1090 jamesdbloom/mockserver`

Start the Demo application
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/hystrix-demo-1.0-SNAPSHOT.jar server`

Resources
---

1. http://localhost:8080/no-hystrix
2. http://localhost:8080/using-hystrix

Expected responses
---

- On DemoApplication startup will be set up expectations for the Resources calls out with delay of 3 seconds to the
Mockserver.

- Expected message back is `{ message: "response from third party" }` and each resource will enrich this response with its name as

1. http://localhost:8080/no-hystrix -> { "message": "response from third party", "resource": "no hystrix" }
1. http://localhost:8080/using-hystrix -> { "message": "response from third party", "resource": "using hystrix" }

Hystrix dashboard
---

1. Run `docker pull kennedyoliveira/hystrix-dashboard` if  you don't have the image.
2. Run `docker run --rm -ti -p 7979:7979 kennedyoliveira/hystrix-dashboard`


## Hystrix: Make your application resilient

https://github.com/Netflix/Hystrix/wiki/How-it-Works

### In the event of failure of external services

1. Provide timely responses to the users (instead of nasty errors or application freeze or not responding). Support feedback!
2. Give detailed error
3. Fallbacks can be provided
4. Hooks for IT operations for monitoring to pin-point the problematic service.
5. Prevent overloading of the problematic service to avoid domino effects.
6. Automatic recovery once the problematic service is online again.

*Source: https://ahus1.github.io/hystrix-examples/manual.html

### Default configuration

1. Timeout for every request to an external system (default: 1000 ms)
2. Limit of concurrent requests for external system (default: 10)
3. Circuit breaker to avoid further requests (default: when more than 50% of all requests fail)
4. Retry of a single request after circuit breaker has triggered (default: every 5 seconds)
5. Interfaces to retrieve runtime information on request and aggregate level (linked to realtime dashboard)
