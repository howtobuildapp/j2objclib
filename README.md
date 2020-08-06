# j2objclib
Make using [j2objc](https://developers.google.com/j2objc/) in your project more easily.

## j2objc
[j2objc](https://developers.google.com/j2objc/) is good at data and business processing.
If your app has complex business logic or data processing,
j2objc would help you to share these code between iOS and android.
Maintaning one copy of logic code is much easier to keep it robust, and also much cheaper.

## thread
All the libs act as static libraries to the app. The code is called by app and executed in the calling thread.
If the lib starts new thread, it should send "SwitchToMainThreadRequest"  to app. And then do callbacks in the main
looper/runloop to prevent threading problems. So follow the principle: "Run as much as possible in main thread. 
If the task is complicated and time-consuming, make it async and do callback in main thread."

## view model
The shared code contains two parts, "global service" and "view model". Global services are almost singletons.
View models implement the defined interfaces, provide data to app view pages and process data submitted from app view pages.
There is no auto-binding between views and view models. View pages manually call the view model methods. If these methods are async operations, 
view models will notify views when the operation completed. This is the way views communicate with view models.
The more view models do, the more you benift from j2objc. 

## request-response and executors
To maximize the responsibility of view model, all the business logic is implemented in the view model. 
So view models may call other app services. All tasks these j2objc java code can not deal are wrapped as requests 
and sended to app. There are registered executors to deal certain requests and return responses.
As that way, the main business flow can be done in the view models and global services. The platform specific views 
only deal with the interaction with users.

## services: storage(cache) \ networking
Services are almost global singletons. They can be called by app directly or other services or view models.
Storage and networking services are basically required by every app. You can abstract as much as possible services
that can be shared between iOS and android.

network: You can implement http networking service in java HttpURLConnection API. Or you can implement networking 
 using the executor model. Wrap the request and send it to app, and handle the request in the app adapter, and then
 send the response back to networking service for further processing. See demo project for details.
 
storage: You can store data with java file api. Or you can use platform abilities like sqlite or other techniques.
As networking service, you can also wrap the storing operation as a request, and send it to app.
As accessing data from disk with memory cache is fast enough for most scenario, 
the key-value storage service is implemented in sync api.

## platform: timer\lifecycle\notification\mainthread
app <--> shared code <--> app adapter
Platform abilities can be exposed to shared java code. Thus shared code can do as much as possible.
These managers are expected used by service singletons. If usual objects call these register methods, they must call
 these unregister methods before they are released. Otherwise there are "memory leak" problems.  

Timer: You could use java timer api with 'SwitchToMainThreadRequest'. There is also a TimerManager that 
should used by service singletons. App must call the tickFromMainThread method in main thread every second to
make TimerManager working properly. 

lifecycle: Services sometimes should respond to app lifecycle event, e.g. initialization when app started,
 cleaning up when app entered into background. Service singletons these are interested in lifecycle events
 should implement AppLifecycleInterface and register in AppLifecycleManager. App must call AppLifecycleManager's 
 methods and then AppLifecycleManager calls the registered services.
 
notification: Notifications are based on iOS notification center or some android event bus lib. Notifications
can be used from app to service, or service to service, or service to app(app register in notification center or event bus). 
See the demo project. App must handle the NotificationRequest. 

main thread: App must handle the SwitchToMainThreadRequest. App do callback in main thread in the next loop.  
