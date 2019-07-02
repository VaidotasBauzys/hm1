# Intro  

Simple service which allows money transfers between accounts

### Prerequisites

Things you need to install

```
Java 12 - lower versions, not tested
```

## Building application

```
Goto project dir and run:
./gradlew build
```

## Running application

```
In project dir run:
./gradlew launch
```

## Trying application

With curl get/post to localhost:4567 using source and target paths. Eg. :

To create user:
```
POST
link: localhost:4567/users
Json:
{
    "id": "1012", 
    "firstName": "Lew",
    "lastName": "Mankind"
}
```

To add amount:
```
POST
link: localhost:4567/account/add
Json:
{
    "userId": "1012", 
    "amount": "100"
}
```

To fetch user information:
```
localhost:4567/users/1012
link: localhost:4567/account/add
```
 
 To transfer amount (two users with initiated amount needed):
 ```
 POST
 link: localhost:4567/account/transfer
{
    "fromUserId": "1012", 
    "toUserId": "1013", 
    "amount": "50"
}
 ```
