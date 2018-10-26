# Clipper for Pocket

## Run from source

### 1. Prepare local host

Note: To build and run this application from source, you need your own Pocket Application. Create one for free in the [Pocket Api Docs](https://getpocket.com/developer/apps/new)!

Within your Pocket Application details, save the `consumer key` for later.

Then you need a `redirect uri`. From Pocket's API docs:

> Once you have the consumer key for the platform you are supporting, the application must register a URL scheme to receive login callbacks. By default, this is "pocketapp" plus your application's ID (which you can find at the beginning of the consumer key before the hyphen). So if your consumer key is 42-abcdef, your app ID is 42, and your URL scheme will be "pocketapp42".

Once you got your `consumer key` and `redirect uri`, you need to persist them as properties in your **local** `gradle.properties`. Usually it can be found at (if it doesn't exist, just create it):
- Windows: `C:\Users\<Your Username>\.gradle\gradle.properties`
- Mac: `/Users/<Your Username>/.gradle/gradle.properties`
- Linux: `/home/<Your Username>/.gradle/gradle.properties`

Add those lines to `gradle.properties` and replace its placeholder with your values:

```
ClipperForPocket_PocketApiCustomerKey="YOUR_CUSTOMER_KEY"
ClipperForPocket_PocketApiRedirectScheme="YOUR_REDIRECT_URI"
ClipperForPocket_PocketApiRedirectUri="YOUR_REDIRECT_URI:authorizationFinished"
```
