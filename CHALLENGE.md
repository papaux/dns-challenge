# Challenge

This application has been created by our intern.

The app does have some bugs described below.

Your task is to troubleshoot and resolve the following issues.

## 1. No message produced

The messages are not being produced to the Kafka `dns-records-analytics` output topic when sending messages to the API.

## 2. Duplicate messages

At some point, the app was working, but it was producing duplicate records in the analytics topic when it was restarted. 

Obviously, this is not desired.

Fix the code to avoid the duplicate records at startup.

## 3. Enhancement

Now that the application is working, the intern is asking you to enhance it with a new feature to track the DNS response code. To simplify, we are only going to support a subset of the response codes (see below).

In addition to the existing fields, the `dns-records-analytics` topic should contain the following fields:

- `rcode`: the response code captured as a string ("NXDOMAIN")
- `success`: a boolean value indicating if the request was a succes (true) or if there was any error (false)

Your task is to modify the code and implement the required changes to make it work.

### DNS Response Codes

- `"NOERROR"`: Indicates a success
- `"SERVFAIL"`: Indicates an internal server error
- `"NXDOMAIN"`: The domain does not exist
